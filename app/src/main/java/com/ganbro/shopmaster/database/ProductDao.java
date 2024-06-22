package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDao {

    private SQLiteDatabase db;

    public ProductDao(Context context) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductDatabaseHelper.COLUMN_NAME, product.getName());
        values.put(ProductDatabaseHelper.COLUMN_PRICE, product.getPrice());
        values.put(ProductDatabaseHelper.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(ProductDatabaseHelper.COLUMN_QUANTITY, product.getQuantity());
        values.put(ProductDatabaseHelper.COLUMN_CATEGORY, product.getCategory());
        values.put(ProductDatabaseHelper.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        db.insert(ProductDatabaseHelper.TABLE_PRODUCT, null, values);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCT, null, ProductDatabaseHelper.COLUMN_CATEGORY + "=?", new String[]{category}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getRecommendedProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCT, null, ProductDatabaseHelper.COLUMN_CATEGORY + "=? AND " + ProductDatabaseHelper.COLUMN_IS_RECOMMENDED + "=?", new String[]{category, "1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<String> getAllCategories() {
        Set<String> categories = new HashSet<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCT, new String[]{ProductDatabaseHelper.COLUMN_CATEGORY}, null, null, ProductDatabaseHelper.COLUMN_CATEGORY, null, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        List<String> categoryList = new ArrayList<>(categories);
        Log.d("ProductDao", "Loaded categories: " + categoryList.toString());
        return categoryList;
    }



    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IS_RECOMMENDED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void initializeProducts() {
        Log.d("ProductDao", "Initializing products...");
        addProduct(new Product(0, "上衣1", 100.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", true));
        addProduct(new Product(0, "上衣2", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", false));
        addProduct(new Product(0, "上衣3", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", true));
        addProduct(new Product(0, "上衣4", 120.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "上衣", true));
        addProduct(new Product(0, "下装1", 150.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "下装", true));
        addProduct(new Product(0, "外套1", 200.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "外套", false));
        addProduct(new Product(0, "配件1", 50.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "配件", true));
        addProduct(new Product(0, "包包1", 300.00, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "包包", false));
        // 继续添加更多的商品
        Log.d("ProductDao", "Products initialized.");
    }


}
