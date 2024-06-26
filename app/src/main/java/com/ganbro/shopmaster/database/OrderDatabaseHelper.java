package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper {

    private DatabaseManager dbManager;

    public OrderDatabaseHelper(Context context) {
        dbManager = DatabaseManager.getInstance(context);
    }

    public long addOrder(String status, double total) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_ORDER_STATUS, status);
        values.put(DatabaseManager.COLUMN_ORDER_TOTAL, total);
        return db.insert(DatabaseManager.TABLE_ORDER, null, values);
    }

    public List<Product> getOrderItemsByStatus(String status) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String selection = DatabaseManager.COLUMN_ORDER_STATUS + " = ?";
        String[] selectionArgs = { status };
        Cursor cursor = db.query(DatabaseManager.TABLE_ORDER, null, selection, selectionArgs, null, null, null);

        List<Product> orderItems = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseManager.COLUMN_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_IMAGE_URL)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_QUANTITY)));
                product.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_CATEGORY)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_DESCRIPTION)));
                orderItems.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orderItems;
    }
}
