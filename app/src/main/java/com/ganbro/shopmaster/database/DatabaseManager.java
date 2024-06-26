package com.ganbro.shopmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 8; // 更新到最新版本

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
    public static final String TABLE_ADDRESSES = "addresses"; // 新增

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
    public static final String COLUMN_ADDRESS = "address"; // 新增
    public static final String COLUMN_PHONE = "phone"; // 新增

    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_VIDEO_DESCRIPTION = "video_description";
    public static final String COLUMN_LIKES_COUNT = "likes_count";
    public static final String COLUMN_COMMENTS_COUNT = "comments_count";
    public static final String COLUMN_COLLECTS_COUNT = "collects_count";

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
                    COLUMN_USER_ID + " INTEGER" +
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
                    COLUMN_COLLECTS_COUNT + " INTEGER" +
                    ");";

    private static final String TABLE_CREATE_ADDRESSES =
            "CREATE TABLE " + TABLE_ADDRESSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT" +
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
        Log.d("DatabaseCreation", "数据库表已创建。");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE videos_temp (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIDEO_URL + " TEXT, " +
                    COLUMN_VIDEO_DESCRIPTION + " TEXT, " +
                    COLUMN_LIKES_COUNT + " INTEGER, " +
                    COLUMN_COLLECTS_COUNT + " INTEGER" +
                    ");");

            // 将旧表中的数据迁移到临时表
            db.execSQL("INSERT INTO videos_temp (" +
                    COLUMN_ID + ", " +
                    COLUMN_VIDEO_URL + ", " +
                    COLUMN_VIDEO_DESCRIPTION + ", " +
                    COLUMN_LIKES_COUNT + ", " +
                    COLUMN_COLLECTS_COUNT +
                    ") SELECT " +
                    COLUMN_ID + ", " +
                    COLUMN_VIDEO_URL + ", " +
                    COLUMN_VIDEO_DESCRIPTION + ", " +
                    COLUMN_LIKES_COUNT + ", " +
                    COLUMN_COLLECTS_COUNT +
                    " FROM " + TABLE_VIDEOS + ";");

            // 删除旧表
            db.execSQL("DROP TABLE " + TABLE_VIDEOS + ";");

            // 将临时表重命名为正式表
            db.execSQL("ALTER TABLE videos_temp RENAME TO " + TABLE_VIDEOS + ";");
        }

        if (oldVersion < 8) {
            db.execSQL(TABLE_CREATE_ADDRESSES); // 创建地址表
            Log.d("DatabaseUpgrade", "地址表已创建。");
        }

        // 如有其他版本升级逻辑，也可以在此处添加
    }
}
