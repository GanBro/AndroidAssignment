package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper {

    private SQLiteDatabase db;

    public CartDatabaseHelper(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    // 获取所有购物车中的商品
    public List<Product> getAllCartProducts() {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_IS_IN_CART + " = ?";
        String[] selectionArgs = { "1" };
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        true
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // 将商品添加到购物车
    public void addProductToCart(Product product) {
        String selection = DatabaseManager.COLUMN_NAME + " = ? AND " + DatabaseManager.COLUMN_IS_IN_CART + " = ?";
        String[] selectionArgs = { product.getName(), "1" };
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // 如果商品已经在购物车中，更新数量
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY));
            int newQuantity = existingQuantity + product.getQuantity();
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_QUANTITY, newQuantity);
            db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)))});
        } else {
            // 如果商品不在购物车中，添加新商品
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_NAME, product.getName());
            values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
            values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
            values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
            values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
            values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
            values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
            values.put(DatabaseManager.COLUMN_IS_IN_CART, 1);
            db.insert(DatabaseManager.TABLE_PRODUCT, null, values);
        }
        cursor.close();
    }

    // 删除购物车中的商品
    public void deleteCartProduct(int id) {
        db.delete(DatabaseManager.TABLE_PRODUCT, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_IS_IN_CART + "=?", new String[]{String.valueOf(id), "1"});
    }

    // 更新购物车中的商品数量
    public void updateProductQuantity(int id, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_QUANTITY, quantity);
        db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_IS_IN_CART + "=?", new String[]{String.valueOf(id), "1"});
    }

    // 将商品添加到收藏表
    public void addProductToFavorites(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_NAME, product.getName());
        values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
        values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
        values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_IN_CART, 0);
        db.insert(DatabaseManager.TABLE_PRODUCT, null, values);
    }
}
