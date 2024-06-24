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

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_NAME, product.getName());
        values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
        values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
        values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription()); // 新增描述字段
        values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        db.insert(DatabaseManager.TABLE_PRODUCT, null, values);
    }

    public void addProductToFavorites(Product product) {
        if (!isProductInFavorites(product.getId())) {
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_ID, product.getId());
            values.put(DatabaseManager.COLUMN_NAME, product.getName());
            values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
            values.put(DatabaseManager.COLUMN_IMAGE_URL, product.getImageUrl());
            values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
            values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
            values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription()); // 新增描述字段
            values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
            db.insert(DatabaseManager.TABLE_FAVORITES, null, values);
        } else {
            Log.d("ProductDao", "Product already in favorites: " + product.getName());
        }
    }

    public boolean isProductInFavorites(int productId) {
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_FAVORITES + " WHERE " + DatabaseManager.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Product> getAllFavorites() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_FAVORITES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)), // 新增描述字段
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_CATEGORY + "=?", new String[]{category}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)), // 新增描述字段
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getRecommendedProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_CATEGORY + "=? AND " + DatabaseManager.COLUMN_IS_RECOMMENDED + "=?", new String[]{category, "1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)), // 新增描述字段
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<String> getAllCategories() {
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, new String[]{DatabaseManager.COLUMN_CATEGORY}, null, null, DatabaseManager.COLUMN_CATEGORY, null, null);
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("ProductDao", "Loaded categories: " + categoryList.toString());
        return categoryList;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)), // 新增描述字段
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void initializeProducts() {
        Log.d("ProductDao", "Initializing products...");
        addProduct(new Product(0, "上衣1", 100.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述1", true));
        addProduct(new Product(0, "上衣2", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述2", false));
        addProduct(new Product(0, "上衣3", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述3", true));
        addProduct(new Product(0, "上衣4", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", "描述4", true));
        addProduct(new Product(0, "下装1", 150.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "下装", "描述5", true));
        addProduct(new Product(0, "外套1", 200.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "外套", "描述6", false));
        addProduct(new Product(0, "配件1", 50.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "配件", "描述7", true));
        addProduct(new Product(0, "包包1", 300.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "包包", "描述8", false));
        // 继续添加更多的商品
        Log.d("ProductDao", "Products initialized.");
    }

    public Product getProductById(int id) {
        Product product = null;
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)), // 新增描述字段
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1
            );
        }
        cursor.close();
        return product;
    }
}
