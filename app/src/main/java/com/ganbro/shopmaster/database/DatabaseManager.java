package com.ganbro.shopmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 12; // 更新到最新版本

    private static DatabaseManager instance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_VIDEOS = "videos";
    public static final String TABLE_ADDRESSES = "addresses";
    public static final String TABLE_ORDER = "orders";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IS_RECOMMENDED = "is_recommended";
    public static final String COLUMN_IS_IN_CART = "is_in_cart";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_VIDEO_DESCRIPTION = "video_description";
    public static final String COLUMN_LIKES_COUNT = "likes_count";
    public static final String COLUMN_COMMENTS_COUNT = "comments_count";
    public static final String COLUMN_COLLECTS_COUNT = "collects_count";

    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_ORDER_TOTAL = "order_total";

    private static final String TABLE_CREATE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IS_RECOMMENDED + " INTEGER, " +
                    COLUMN_IS_IN_CART + " INTEGER, " +
                    COLUMN_IS_FAVORITE + " INTEGER, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_ORDER_STATUS + " TEXT" +
                    ");";

    private static final String TABLE_CREATE_FAVORITES =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_IS_RECOMMENDED + " INTEGER);";

    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE" +
                    ");";

    private static final String TABLE_CREATE_VIDEOS =
            "CREATE TABLE " + TABLE_VIDEOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIDEO_URL + " TEXT, " +
                    COLUMN_VIDEO_DESCRIPTION + " TEXT, " +
                    COLUMN_LIKES_COUNT + " INTEGER, " +
                    COLUMN_COLLECTS_COUNT + " INTEGER" +
                    ");";

    private static final String TABLE_CREATE_ADDRESSES =
            "CREATE TABLE " + TABLE_ADDRESSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT" +
                    ");";

    private static final String TABLE_CREATE_ORDERS =
            "CREATE TABLE " + TABLE_ORDER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_STATUS + " TEXT, " +
                    COLUMN_ORDER_TOTAL + " REAL" +
                    ");";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_PRODUCTS);
        db.execSQL(TABLE_CREATE_FAVORITES);
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_VIDEOS);
        db.execSQL(TABLE_CREATE_ADDRESSES);
        db.execSQL(TABLE_CREATE_ORDERS);
        Log.d("DatabaseCreation", "数据库表已创建。");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE " + TABLE_PRODUCT + " ADD COLUMN " + COLUMN_ORDER_STATUS + " TEXT;");
        }
        // 添加其他版本升级逻辑
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseDowngrade", "处理数据库降级从版本 " + oldVersion + " 到版本 " + newVersion);
    }
}
