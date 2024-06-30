package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ganbro.shopmaster.models.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoDao {

    private static final String TAG = "VideoDao";
    private SQLiteDatabase db;

    public VideoDao(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
        // 删除下面两行，防止每次创建时清除和重新初始化数据
        // clearVideosTable();
        // initializeVideos();
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
        values.put(DatabaseManager.COLUMN_IS_LIKED, video.isLiked() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_COLLECTED, video.isCollected() ? 1 : 0);
        db.insert(DatabaseManager.TABLE_VIDEOS, null, values);
    }

    public void updateLikesCount(int videoId, int likesCount, boolean isLiked) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_LIKES_COUNT, likesCount);
        values.put(DatabaseManager.COLUMN_IS_LIKED, isLiked ? 1 : 0);
        db.update(DatabaseManager.TABLE_VIDEOS, values, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
    }

    public void updateCollectsCount(int videoId, int collectsCount, boolean isCollected) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_COLLECTS_COUNT, collectsCount);
        values.put(DatabaseManager.COLUMN_IS_COLLECTED, isCollected ? 1 : 0);
        db.update(DatabaseManager.TABLE_VIDEOS, values, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
    }

    public Video getVideoById(int videoId) {
        Cursor cursor = db.query(DatabaseManager.TABLE_VIDEOS, null, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Video video = new Video(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_LIKES_COUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COLLECTS_COUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_LIKED)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_COLLECTED)) == 1
            );
            cursor.close();
            return video;
        }
        return null;
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
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COLLECTS_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_LIKED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_COLLECTED)) == 1
                );
                videoList.add(video);
                Log.d(TAG, "Loaded video with URL: " + video.getVideoUrl());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videoList;
    }

    public void initializeVideos() {
        addVideo(new Video(0, "android.resource://com.ganbro.shopmaster/raw/video_0", "不要对我冷冰冰", 82000, 5520, false, false));
        addVideo(new Video(1, "android.resource://com.ganbro.shopmaster/raw/video_1", "好好好这么玩是吧#蝴蝶步", 45000, 4270, false, false));
        addVideo(new Video(2, "android.resource://com.ganbro.shopmaster/raw/video_2", "反季节战神#nonono#蒙口羽皇", 60000, 4482, false, false));
        addVideo(new Video(3, "android.resource://com.ganbro.shopmaster/raw/video_3", "这舞真的好快乐#girlfriend#大77编舞", 42000, 3841, false, false));
    }
}
