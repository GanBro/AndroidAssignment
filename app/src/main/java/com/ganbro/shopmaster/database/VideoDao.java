package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Video;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class VideoDao {

    private static final String TAG = "VideoDao";
    private SQLiteDatabase db;

    public VideoDao(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
        clearVideosTable();  // 清空视频表
        initializeVideos();  // 重新添加初始化视频
    }

    private void clearVideosTable() {
        db.execSQL("DELETE FROM " + DatabaseManager.TABLE_VIDEOS);
        Log.d(TAG, "Video table cleared");
    }

    public void addVideo(Video video) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_VIDEO_URL, video.getVideoUrl());
        values.put(DatabaseManager.COLUMN_VIDEO_DESCRIPTION, video.getDescription());
        values.put(DatabaseManager.COLUMN_LIKES_COUNT, video.getLikesCount());
        values.put(DatabaseManager.COLUMN_COLLECTS_COUNT, video.getCollectsCount());
        long result = db.insert(DatabaseManager.TABLE_VIDEOS, null, values);
    }

    public List<Video> getAllVideos() {
        List<Video> videoList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_VIDEOS, null, null, null, null, null, null);
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
                Log.d(TAG, "Loaded video with URL: " + video.getVideoUrl());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videoList;
    }

    public void initializeVideos() {
        addVideo(new Video(0, "android.resource://com.ganbro.shopmaster/raw/video_0", "不要对我冷冰冰", 82000, 5520));
        addVideo(new Video(1, "android.resource://com.ganbro.shopmaster/raw/video_1", "好好好这么玩是吧#蝴蝶步", 45000, 4270));
        addVideo(new Video(2, "android.resource://com.ganbro.shopmaster/raw/video_2", "反季节战神#nonono#蒙口羽皇", 60000, 4482));
        addVideo(new Video(3, "android.resource://com.ganbro.shopmaster/raw/video_3", "这舞真的好快乐#girlfriend#大77编舞", 42000, 3841));
    }
}
