package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Video;
import java.util.ArrayList;
import java.util.List;

public class VideoDatabaseHelper {

    private SQLiteDatabase db;

    public VideoDatabaseHelper(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addVideo(Video video) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_VIDEO_URL, video.getVideoUrl());
        values.put(DatabaseManager.COLUMN_VIDEO_DESCRIPTION, video.getDescription());
        values.put(DatabaseManager.COLUMN_LIKES_COUNT, video.getLikesCount());
        values.put(DatabaseManager.COLUMN_COLLECTS_COUNT, video.getCollectsCount());
        db.insert(DatabaseManager.TABLE_VIDEOS, null, values);
    }

    public void updateLikesCount(int videoId, int likesCount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_LIKES_COUNT, likesCount);
        db.update(DatabaseManager.TABLE_VIDEOS, values, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
    }

    public void updateCollectsCount(int videoId, int collectsCount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_COLLECTS_COUNT, collectsCount);
        db.update(DatabaseManager.TABLE_VIDEOS, values, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
    }

    public List<Video> getAllFavoriteVideos() {
        List<Video> videoList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_VIDEOS, null, DatabaseManager.COLUMN_COLLECTS_COUNT + " > 0", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_LIKES_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COLLECTS_COUNT))
                );
                videoList.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videoList;
    }
}
