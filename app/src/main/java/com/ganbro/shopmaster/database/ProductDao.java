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
        Log.d("ProductDao", "数据库已打开");
    }

    public void addProduct(Product product, int userId, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_NAME, product.getName());
        values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
        values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
        values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 0);
        values.put(DatabaseManager.COLUMN_IS_IN_CART, 0);
        values.put(DatabaseManager.COLUMN_USER_ID, userId);
        values.put(DatabaseManager.COLUMN_USER_EMAIL, userEmail);
        values.put(DatabaseManager.COLUMN_ORDER_STATUS, ""); // 默认无订单状态
        long result = db.insert(DatabaseManager.TABLE_PRODUCT, null, values);
        Log.d("ProductDao", "添加产品结果: " + result);
    }

    public void addProductToFavorites(int productId, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 1);
        int rowsUpdated = db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?", new String[]{String.valueOf(productId), userEmail});
        Log.d("ProductDao", "更新收藏状态，影响的行数: " + rowsUpdated);
    }

    public boolean isProductInFavorites(int productId, String userEmail) {
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_PRODUCT + " WHERE " + DatabaseManager.COLUMN_ID + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ? AND " + DatabaseManager.COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), userEmail});
        boolean exists = (cursor.getCount() > 0);
        Log.d("ProductDao", "产品是否在收藏中: " + exists);
        cursor.close();
        return exists;
    }

    public List<Product> getAllFavorites(String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_IS_FAVORITE + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {"1", userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询收藏产品，行数: " + cursor.getCount());
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

    public void addProductToCart(int productId, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_IS_IN_CART, 1);
        int rowsUpdated = db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?", new String[]{String.valueOf(productId), userEmail});
        Log.d("ProductDao", "更新购物车状态，影响的行数: " + rowsUpdated);
    }

    public boolean isProductInCart(int productId, String userEmail) {
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_PRODUCT + " WHERE " + DatabaseManager.COLUMN_ID + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ? AND " + DatabaseManager.COLUMN_IS_IN_CART + " = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), userEmail});
        boolean exists = (cursor.getCount() > 0);
        Log.d("ProductDao", "产品是否在购物车中: " + exists);
        cursor.close();
        return exists;
    }

    public List<Product> getAllCartItems(String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_IS_IN_CART + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {"1", userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询购物车产品，行数: " + cursor.getCount());
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

    public List<Product> getProductsByCategory(String category, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {category, userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询分类产品，分类: " + category + "，行数: " + cursor.getCount());
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

    public List<Product> getRecommendedProductsByCategory(String category, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_IS_RECOMMENDED + " = 1 AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {category, userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询推荐产品，分类: " + category + "，行数: " + cursor.getCount());
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

    public List<String> getAllCategories(String userEmail) {
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, new String[]{DatabaseManager.COLUMN_CATEGORY}, DatabaseManager.COLUMN_USER_EMAIL + " = ?", new String[]{userEmail}, DatabaseManager.COLUMN_CATEGORY, null, null);
        Log.d("ProductDao", "查询所有分类，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public List<Product> getAllProducts(String userEmail) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_USER_EMAIL + " = ?", new String[]{userEmail}, null, null, null);
        Log.d("ProductDao", "查询所有产品，行数: " + cursor.getCount());
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

    public void initializeProducts(int userId, String userEmail) {
        Log.d("ProductDao", "初始化产品数据");
        addProduct(new Product(0, "上衣1", 100.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述1", true, false), userId, userEmail);
        addProduct(new Product(0, "上衣2", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述2", false, false), userId, userEmail);
        addProduct(new Product(0, "上衣3", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述3", true, false), userId, userEmail);
        addProduct(new Product(0, "上衣4", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述4", true, false), userId, userEmail);
        addProduct(new Product(0, "下装1", 150.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "下装", "描述5", true, false), userId, userEmail);
        addProduct(new Product(0, "外套1", 200.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "外套", "描述6", false, false), userId, userEmail);
        addProduct(new Product(0, "配件1", 50.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "配件", "描述7", true, false), userId, userEmail);
        addProduct(new Product(0, "包包1", 300.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "包包", "描述8", false, false), userId, userEmail);
    }


    public Product getProductById(int id, String userEmail) {
        Product product = null;
        String selection = DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {String.valueOf(id), userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询产品，ID: " + id + "，用户Email: " + userEmail + "，行数: " + cursor.getCount());
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

    public List<Product> getOrderItemsByStatusAndUser(String status, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_ORDER_STATUS + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {status, userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询订单项，状态: " + status + "，用户Email: " + userEmail + "，行数: " + cursor.getCount());
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
}
