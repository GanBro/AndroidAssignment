package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private SQLiteDatabase db;

    public ProductDao(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addProduct(Product product, int userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_NAME, product.getName());
        values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
        values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
        values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 0); // 默认商品不被收藏
        values.put(DatabaseManager.COLUMN_USER_ID, userId); // 添加 userId
        db.insert(DatabaseManager.TABLE_PRODUCT, null, values);
    }

    public void addProductToFavorites(int productId, int userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 1);  // 1 表示收藏夹中的商品
        db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_ID + "=?", new String[]{String.valueOf(productId), String.valueOf(userId)});
    }

    public boolean isProductInFavorites(int productId, int userId) {
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_PRODUCT + " WHERE " + DatabaseManager.COLUMN_ID + " = ? AND " + DatabaseManager.COLUMN_USER_ID + " = ? AND " + DatabaseManager.COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), String.valueOf(userId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Product> getAllFavorites(int userId) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_IS_FAVORITE + " = ? AND " + DatabaseManager.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {"1", String.valueOf(userId)};
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

    public List<Product> getProductsByCategory(String category, int userId) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {category, String.valueOf(userId)};
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
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getRecommendedProductsByCategory(String category, int userId) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_IS_RECOMMENDED + " = 1 AND " + DatabaseManager.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {category, String.valueOf(userId)};
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
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<String> getAllCategories(int userId) {
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, new String[]{DatabaseManager.COLUMN_CATEGORY}, DatabaseManager.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, DatabaseManager.COLUMN_CATEGORY, null, null);
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("ProductDao", "Loaded categories: " + categoryList.toString());
        return categoryList;
    }

    public List<Product> getAllProducts(int userId) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
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
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void initializeProducts(int userId) {
        Log.d("ProductDao", "Initializing products...");
        addProduct(new Product(0, "上衣1", 100.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述1", true, false), userId);
        addProduct(new Product(0, "上衣2", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述2", false, false), userId);
        addProduct(new Product(0, "上衣3", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述3", true, false), userId);
        addProduct(new Product(0, "上衣4", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述4", true, false), userId);
        addProduct(new Product(0, "下装1", 150.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "下装", "描述5", true, false), userId);
        addProduct(new Product(0, "外套1", 200.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "外套", "描述6", false, false), userId);
        addProduct(new Product(0, "配件1", 50.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "配件", "描述7", true, false), userId);
        addProduct(new Product(0, "包包1", 300.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "包包", "描述8", false, false), userId);
        Log.d("ProductDao", "Products initialized.");
    }

    public Product getProductById(int id, int userId) {
        Product product = null;
        String selection = DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(id), String.valueOf(userId)};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
            );
        }
        cursor.close();
        return product;
    }
}
