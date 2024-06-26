package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ganbro.shopmaster.models.Video;
import java.util.ArrayList;
import java.util.List;

public class VideoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_VIDEOS = "videos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_VIDEO_DESCRIPTION = "video_description";
    public static final String COLUMN_LIKES_COUNT = "likes_count";
    public static final String COLUMN_COMMENTS_COUNT = "comments_count";
    public static final String COLUMN_COLLECTS_COUNT = "collects_count";

    private static final String TABLE_CREATE_VIDEOS =
            "CREATE TABLE " + TABLE_VIDEOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIDEO_URL + " TEXT, " +
                    COLUMN_VIDEO_DESCRIPTION + " TEXT, " +
                    COLUMN_LIKES_COUNT + " INTEGER, " +
                    COLUMN_COMMENTS_COUNT + " INTEGER, " +
                    COLUMN_COLLECTS_COUNT + " INTEGER" +
                    ");";

    public VideoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_VIDEOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    public void addVideo(Video video) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VIDEO_URL, video.getVideoUrl());
        values.put(COLUMN_VIDEO_DESCRIPTION, video.getDescription());
        values.put(COLUMN_LIKES_COUNT, video.getLikesCount());
        values.put(COLUMN_COMMENTS_COUNT, video.getCommentsCount());
        values.put(COLUMN_COLLECTS_COUNT, video.getCollectsCount());
        db.insert(TABLE_VIDEOS, null, values);
        db.close();
    }

    public List<Video> getAllVideos() {
        List<Video> videoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VIDEOS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIKES_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMMENTS_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLLECTS_COUNT))
                );
                videoList.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return videoList;
    }
}
