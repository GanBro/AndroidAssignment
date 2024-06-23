package com.ganbro.shopmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_FAVORITES = "favorites"; // 添加收藏表

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IS_RECOMMENDED = "is_recommended";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_IS_RECOMMENDED + " INTEGER);";

    public static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_IS_RECOMMENDED + " INTEGER);";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_FAVORITES_CREATE); // 创建收藏表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES); // 删除旧的收藏表
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES); // 删除旧的收藏表
        onCreate(db);
    }
}
