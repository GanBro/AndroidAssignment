package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper {

    private static final String TAG = "OrderDatabaseHelper";
    private DatabaseManager dbManager;

    public OrderDatabaseHelper(Context context) {
        dbManager = DatabaseManager.getInstance(context);
        printAllOrders();
    }

    public long addOrder(String status, double total, List<Product> orderItems, String userEmail) {
        if (orderItems == null || orderItems.isEmpty()) {
            orderItems = new ArrayList<>();
        }

        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.beginTransaction();
        long orderId = -1;

        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseManager.COLUMN_ORDER_STATUS, status);
            orderValues.put(DatabaseManager.COLUMN_ORDER_TOTAL, total);
            orderValues.put(DatabaseManager.COLUMN_USER_EMAIL, userEmail);
            orderValues.put(DatabaseManager.COLUMN_CREATE_TIME, System.currentTimeMillis()); // 添加创建时间
            orderId = db.insert(DatabaseManager.TABLE_ORDER, null, orderValues);

            Log.d(TAG, "订单插入成功，订单ID: " + orderId);

            for (Product product : orderItems) {
                Log.d(TAG, "准备插入订单项: " + product.getName());
                ContentValues itemValues = new ContentValues();
                itemValues.put(DatabaseManager.COLUMN_ORDER_ID, orderId);
                itemValues.put(DatabaseManager.COLUMN_NAME, product.getName());
                itemValues.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
                itemValues.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
                itemValues.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
                itemValues.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
                itemValues.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
                long itemId = db.insert(DatabaseManager.TABLE_ORDER_ITEMS, null, itemValues);

                Log.d(TAG, "订单项插入成功，订单项ID: " + itemId + "，订单ID: " + orderId + "，商品名称: " + product.getName());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "订单插入失败: " + e.getMessage());
        } finally {
            db.endTransaction();
        }

        return orderId;
    }

    public List<Product> getOrderItemsByStatusAndUser(String status, String userEmail) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String query = "SELECT oi.* FROM " + DatabaseManager.TABLE_ORDER_ITEMS + " oi " +
                "JOIN " + DatabaseManager.TABLE_ORDER + " o ON oi." + DatabaseManager.COLUMN_ORDER_ID + " = o." + DatabaseManager.COLUMN_ID + " " +
                "WHERE o." + DatabaseManager.COLUMN_ORDER_STATUS + " = ? AND o." + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { status, userEmail };
        Log.d(TAG, "查询订单项，状态: " + status + ", 用户邮箱: " + userEmail);
        Cursor cursor = db.rawQuery(query, selectionArgs);

        List<Product> orderItems = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)));
                orderItems.add(product);
                Log.d(TAG, "加载到订单项: " + product.getName() + "，订单ID: " + product.getId());
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d(TAG, "总共加载到订单项数量: " + orderItems.size());
        return orderItems;
    }

    public void printAllOrders() {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String orderQuery = "SELECT * FROM " + DatabaseManager.TABLE_ORDER;
        Cursor orderCursor = db.rawQuery(orderQuery, null);

        if (orderCursor != null && orderCursor.moveToFirst()) {
            do {
                int orderId = orderCursor.getInt(orderCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID));
                String orderStatus = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ORDER_STATUS));
                double orderTotal = orderCursor.getDouble(orderCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ORDER_TOTAL));
                String userEmail = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_USER_EMAIL));
                Log.d(TAG, "订单ID: " + orderId + ", 状态: " + orderStatus + ", 总额: " + orderTotal + ", 用户邮箱: " + userEmail);

                String itemQuery = "SELECT * FROM " + DatabaseManager.TABLE_ORDER_ITEMS + " WHERE " + DatabaseManager.COLUMN_ORDER_ID + " = ?";
                Cursor itemCursor = db.rawQuery(itemQuery, new String[]{String.valueOf(orderId)});

                Log.d(TAG, "查询订单项，订单ID: " + orderId + ", 查询结果数: " + itemCursor.getCount());

                if (itemCursor != null && itemCursor.moveToFirst()) {
                    do {
                        String itemName = itemCursor.getString(itemCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME));
                        double itemPrice = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE));
                        int itemQuantity = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY));
                        Log.d(TAG, "  订单项: " + itemName + ", 价格: " + itemPrice + ", 数量: " + itemQuantity + "，订单ID: " + orderId);
                    } while (itemCursor.moveToNext());
                    itemCursor.close();
                } else {
                    Log.d(TAG, "没有找到订单项，订单ID: " + orderId);
                }
            } while (orderCursor.moveToNext());
            orderCursor.close();
        } else {
            Log.d(TAG, "没有找到订单");
        }
    }

    public void clearOrderDetails() {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DatabaseManager.TABLE_ORDER, null, null);
            db.delete(DatabaseManager.TABLE_ORDER_ITEMS, null, null);
            db.setTransactionSuccessful();
            Log.d(TAG, "所有订单详情已清除");
        } catch (Exception e) {
            Log.e(TAG, "清除订单详情失败: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
