package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper {

    // 表和列的定义
    public static final String TABLE_CART = "cart";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category";

    public static final String TABLE_CART_CREATE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT);";

    public static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT);";

    private SQLiteDatabase db;

    public CartDatabaseHelper(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    // 收藏商品
    public void addProductToFavorites(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_CATEGORY, product.getCategory());
        db.insert(TABLE_FAVORITES, null, values);
    }

    // 获取所有收藏的商品
    public List<Product> getAllFavorites() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // 获取所有购物车中的商品
    public List<Product> getAllCartProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CART, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // 将商品添加到购物车
    public void addProductToCart(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_CATEGORY, product.getCategory());
        db.insert(TABLE_CART, null, values);
    }

    // 删除购物车中的商品
    public void deleteCartProduct(int id) {
        db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
