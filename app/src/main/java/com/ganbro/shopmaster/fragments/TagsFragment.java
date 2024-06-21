package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;

public class TagsFragment extends Fragment {

    private GridLayout gridLayoutTags;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);

        gridLayoutTags = view.findViewById(R.id.grid_layout_tags);

        // 示例标签数据
        String[] tags = {"尚硅谷", "JAVA", "Android", "HTML5", "PHP", "UI", "Activity", "Fragment", "Button",
                "TextView", "JNI", "NDK", "手机影音", "硅谷社交", "硅谷商城", "硅谷金融", "自定义控件", "硅谷"};

        int totalTags = tags.length;
        int columnCount = gridLayoutTags.getColumnCount();
        int rowCount = (int) Math.ceil((double) totalTags / columnCount);
        gridLayoutTags.setRowCount(rowCount);

        // 动态添加标签
        for (String tag : tags) {
            TextView tagView = new TextView(getContext());
            tagView.setText(tag);
            tagView.setPadding(16, 8, 16, 8);
            tagView.setTextSize(16);
            tagView.setBackgroundResource(R.drawable.tag_background); // 设置标签背景
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            tagView.setLayoutParams(params);
            gridLayoutTags.addView(tagView);
        }

        return view;
    }
}
