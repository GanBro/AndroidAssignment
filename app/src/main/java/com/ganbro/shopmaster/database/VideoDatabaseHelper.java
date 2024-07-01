package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ganbro.shopmaster.models.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        values.put(DatabaseManager.COLUMN_IS_LIKED, video.isLiked() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_COLLECTED, video.isCollected() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_USERNAME, video.getUsername());
        values.put(DatabaseManager.COLUMN_COMMENTS, new Gson().toJson(video.getComments())); // 将评论序列化为JSON字符串保存
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

    public void updateComments(int videoId, List<String> comments) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_COMMENTS, new Gson().toJson(comments)); // 更新评论
        db.update(DatabaseManager.TABLE_VIDEOS, values, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
    }

    public List<Video> getAllFavoriteVideos() {
        List<Video> videoList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_VIDEOS, null, DatabaseManager.COLUMN_IS_COLLECTED + " = 1", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_LIKES_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COLLECTS_COUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_LIKED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_COLLECTED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_USERNAME))
                );
                String commentsJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COMMENTS));
                List<String> comments = new Gson().fromJson(commentsJson, new TypeToken<List<String>>() {}.getType());
                video.setComments(comments);
                videoList.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videoList;
    }

    public Video getVideoById(int id) {
        Cursor cursor = db.query(DatabaseManager.TABLE_VIDEOS, null, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Video video = new Video(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_VIDEO_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_LIKES_COUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COLLECTS_COUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_LIKED)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_COLLECTED)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_USERNAME))
            );
            String commentsJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_COMMENTS));
            List<String> comments = new Gson().fromJson(commentsJson, new TypeToken<List<String>>() {}.getType());
            video.setComments(comments);
            cursor.close();
            return video;
        }
        return null;
    }
}
