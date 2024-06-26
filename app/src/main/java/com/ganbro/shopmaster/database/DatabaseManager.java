package com.ganbro.shopmaster.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 16;

    private static DatabaseManager instance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_VIDEOS = "videos";
    public static final String TABLE_ADDRESSES = "addresses";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEMS = "order_items";

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
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_ORDER_ID = "order_id";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_VIDEO_DESCRIPTION = "video_description";
    public static final String COLUMN_LIKES_COUNT = "likes_count";
    public static final String COLUMN_COLLECTS_COUNT = "collects_count";
    public static final String COLUMN_IS_LIKED = "is_liked";
    public static final String COLUMN_IS_COLLECTED = "is_collected";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_COMMENTS = "comments";

    public static final String COLUMN_ORDER_TOTAL = "order_total";
    public static final String COLUMN_CREATE_TIME = "create_time";

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
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_ORDER_STATUS + " TEXT" +
                    ");";

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
                    COLUMN_COLLECTS_COUNT + " INTEGER, " +
                    COLUMN_IS_LIKED + " INTEGER, " +
                    COLUMN_IS_COLLECTED + " INTEGER, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_COMMENTS + " TEXT" +
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
                    COLUMN_ORDER_TOTAL + " REAL, " +
                    COLUMN_CREATE_TIME + " INTEGER, " +
                    COLUMN_USER_EMAIL + " TEXT" +
                    ");";

    private static final String TABLE_CREATE_ORDER_ITEMS =
            "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_ID + " INTEGER, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ID + ")" +
                    ");";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_PRODUCTS);
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_VIDEOS);
        db.execSQL(TABLE_CREATE_ADDRESSES);
        db.execSQL(TABLE_CREATE_ORDERS);
        db.execSQL(TABLE_CREATE_ORDER_ITEMS);
        Log.d("DatabaseCreation", "数据库表已创建。");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 16) {
            if (!isColumnExists(db, TABLE_PRODUCT, COLUMN_USER_EMAIL)) {
                db.execSQL("ALTER TABLE " + TABLE_PRODUCT + " ADD COLUMN " + COLUMN_USER_EMAIL + " TEXT;");
            }
            if (!isColumnExists(db, TABLE_ORDER, COLUMN_CREATE_TIME)) {
                db.execSQL("ALTER TABLE " + TABLE_ORDER + " ADD COLUMN " + COLUMN_CREATE_TIME + " INTEGER;");
            }
            if (!isColumnExists(db, TABLE_PRODUCT, COLUMN_ORDER_STATUS)) {
                db.execSQL("ALTER TABLE " + TABLE_PRODUCT + " ADD COLUMN " + COLUMN_ORDER_STATUS + " TEXT;");
            }
            if (!isColumnExists(db, TABLE_VIDEOS, COLUMN_USERNAME)) {
                db.execSQL("ALTER TABLE " + TABLE_VIDEOS + " ADD COLUMN " + COLUMN_USERNAME + " TEXT;");
            }
            if (!isColumnExists(db, TABLE_VIDEOS, COLUMN_COMMENTS)) {
                db.execSQL("ALTER TABLE " + TABLE_VIDEOS + " ADD COLUMN " + COLUMN_COMMENTS + " TEXT;");
            }
        }
    }

    private boolean isColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ");", null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex("name");
                while (cursor.moveToNext()) {
                    String name = cursor.getString(nameIndex);
                    if (columnName.equals(name)) {
                        return true;
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseDowngrade", "处理数据库降级从版本 " + oldVersion + " 到版本 " + newVersion);
    }
}
