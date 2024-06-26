package com.ganbro.shopmaster.models;

public class Video {
    private int id;
    private String videoUrl;
    private String description;
    private int likesCount;
    private int collectsCount;
    private boolean liked;
    private boolean collected;

    public Video(int id, String videoUrl, String description, int likesCount, int collectsCount) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.description = description;
        this.likesCount = likesCount;
        this.collectsCount = collectsCount;
        this.liked = false; // Default value
        this.collected = false; // Default value
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCollectsCount() {
        return collectsCount;
    }

    public void setCollectsCount(int collectsCount) {
        this.collectsCount = collectsCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}