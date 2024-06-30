package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDatabaseHelper {

    private static final String TAG = "UserDatabaseHelper";
    private DatabaseManager dbManager;

    public UserDatabaseHelper(Context context) {
        dbManager = DatabaseManager.getInstance(context);
    }

    public void addUser(String email) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor cursor = db.query(DatabaseManager.TABLE_USERS, null, DatabaseManager.COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // 用户已存在
            Log.d(TAG, "用户已存在: " + email);
            cursor.close();
        } else {
            // 插入新用户
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_EMAIL, email);
            db.insert(DatabaseManager.TABLE_USERS, null, values);
            Log.d(TAG, "用户已插入: " + email);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
