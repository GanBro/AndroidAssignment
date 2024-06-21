package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category"; // 新增的列

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT);"; // 添加 category 列

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addProductToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_CATEGORY, product.getCategory()); // 添加 category 列的值
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public List<Product> getAllCartProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)) // 获取 category 列的值
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void deleteCartProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
