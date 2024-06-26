package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.VideoAdapter;
import com.ganbro.shopmaster.database.VideoDao;
import com.ganbro.shopmaster.models.Video;
import java.util.List;

public class DiscoverFragment extends Fragment {

    private static final String TAG = "DiscoverFragment";
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private VideoDao videoDao;
    private List<Video> videoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_videos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        videoDao = new VideoDao(getContext());

        // 初始化视频数据
        videoDao.initializeVideos();

        videoList = videoDao.getAllVideos();
        for (Video video : videoList) {
            Log.d(TAG, "Loaded video: " + video.getVideoUrl());
        }

        videoAdapter = new VideoAdapter(getContext(), videoList);
        recyclerView.setAdapter(videoAdapter);
    }
}
