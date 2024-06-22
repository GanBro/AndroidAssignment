package com.ganbro.shopmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 6;

    private Context context;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CartDatabaseHelper.TABLE_CREATE);
        db.execSQL(ProductDatabaseHelper.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CartDatabaseHelper.TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + ProductDatabaseHelper.TABLE_PRODUCT);
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CartDatabaseHelper.TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + ProductDatabaseHelper.TABLE_PRODUCT);
        onCreate(db);
    }

    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
    }
}
