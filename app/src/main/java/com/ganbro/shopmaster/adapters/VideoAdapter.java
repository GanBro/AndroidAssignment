package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Video;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String TAG = "VideoAdapter";
    private Context context;
    private List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
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
        holder.tvLikesCount.setText(String.valueOf(video.getLikesCount()));
        holder.tvCollectsCount.setText(String.valueOf(video.getCollectsCount()));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView videoDescription;
        TextView tvLikesCount;
        TextView tvCollectsCount;
        ImageView btnLike;
        ImageView btnCollect;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            videoDescription = itemView.findViewById(R.id.video_description);
            tvLikesCount = itemView.findViewById(R.id.tv_likes_count);
            tvCollectsCount = itemView.findViewById(R.id.tv_collects_count);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnCollect = itemView.findViewById(R.id.btn_collect);
        }
    }
}
