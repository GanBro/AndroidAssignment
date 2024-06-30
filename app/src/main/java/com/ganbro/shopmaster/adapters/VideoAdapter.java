package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.VideoDao;
import com.ganbro.shopmaster.models.Video;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String TAG = "VideoAdapter";
    private Context context;
    private List<Video> videoList;
    private VideoDao videoDao;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
        this.videoDao = new VideoDao(context);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final Video video = videoList.get(position);

        // 从数据库中获取视频的最新状态
        Video dbVideo = videoDao.getVideoById(video.getId());
        if (dbVideo != null) {
            video.setLikesCount(dbVideo.getLikesCount());
            video.setCollectsCount(dbVideo.getCollectsCount());
            video.setLiked(dbVideo.isLiked());
            video.setCollected(dbVideo.isCollected());
        }

        String videoUrl = video.getVideoUrl();
        Log.d(TAG, "Setting video URL: " + videoUrl);

        Uri videoUri = Uri.parse(videoUrl);
        holder.videoView.setVideoURI(videoUri);

        holder.videoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        });

        holder.videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            Log.e(TAG, "Error playing video: " + what + ", " + extra);
            return true;
        });

        // Set other video details
        holder.videoDescription.setText(video.getDescription());
        holder.btnLike.setText(String.valueOf(video.getLikesCount()));
        holder.btnCollect.setText(String.valueOf(video.getCollectsCount()));

        // Set initial button states
        updateLikeButton(holder.btnLike, video.isLiked());
        updateCollectButton(holder.btnCollect, video.isCollected());

        // Like button click listener
        holder.btnLike.setOnClickListener(v -> {
            boolean isLiked = video.isLiked();
            int likesCount = video.getLikesCount();

            if (isLiked) {
                likesCount--;
                isLiked = false;
            } else {
                likesCount++;
                isLiked = true;
            }

            video.setLikesCount(likesCount);
            video.setLiked(isLiked);

            holder.btnLike.setText(String.valueOf(likesCount));
            videoDao.updateLikesCount(video.getId(), likesCount, isLiked);
            updateLikeButton(holder.btnLike, isLiked);
        });

        // Collect button click listener
        holder.btnCollect.setOnClickListener(v -> {
            boolean isCollected = video.isCollected();
            int collectsCount = video.getCollectsCount();

            if (isCollected) {
                collectsCount--;
                isCollected = false;
            } else {
                collectsCount++;
                isCollected = true;
            }

            video.setCollectsCount(collectsCount);
            video.setCollected(isCollected);

            holder.btnCollect.setText(String.valueOf(collectsCount));
            videoDao.updateCollectsCount(video.getId(), collectsCount, isCollected);
            updateCollectButton(holder.btnCollect, isCollected);
        });
    }

    private void updateLikeButton(MaterialButton btnLike, boolean isLiked) {
        if (isLiked) {
            btnLike.setIconResource(R.drawable.ic_like); // Set to the liked drawable
            btnLike.setIconTintResource(R.color.red);
            btnLike.setTextColor(btnLike.getContext().getResources().getColor(R.color.red));
        } else {
            btnLike.setIconResource(R.drawable.ic_like_disabled); // Set to the unliked drawable
            btnLike.setIconTintResource(R.color.textColorSecondary);
            btnLike.setTextColor(btnLike.getContext().getResources().getColor(R.color.textColorSecondary));
        }
    }

    private void updateCollectButton(MaterialButton btnCollect, boolean isCollected) {
        if (isCollected) {
            btnCollect.setIconResource(R.drawable.ic_collect); // Set to the collected drawable
            btnCollect.setIconTintResource(R.color.red);
            btnCollect.setTextColor(btnCollect.getContext().getResources().getColor(R.color.red));
        } else {
            btnCollect.setIconResource(R.drawable.ic_collect_disabled); // Set to the uncollected drawable
            btnCollect.setIconTintResource(R.color.textColorSecondary);
            btnCollect.setTextColor(btnCollect.getContext().getResources().getColor(R.color.textColorSecondary));
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView videoDescription;
        MaterialButton btnLike;
        MaterialButton btnCollect;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            videoDescription = itemView.findViewById(R.id.video_description);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnCollect = itemView.findViewById(R.id.btn_collect);
        }
    }
}
