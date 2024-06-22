package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CategoryAdapter;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private ListView listViewCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listViewCategories = view.findViewById(R.id.listView_categories);
        // 初始化分类列表数据
        List<String> categories = new ArrayList<>();
        categories.add("上衣");
        categories.add("下装");
        categories.add("外套");
        categories.add("配件");
        categories.add("包包");

        // 设置分类列表的适配器
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        listViewCategories.setAdapter(adapter);

        listViewCategories.setOnItemClickListener((parent, view1, position, id) -> {
            String category = categories.get(position);
            // 处理点击事件，显示对应的分类内容
            Fragment fragment = CommonCategoryFragment.newInstance(category);
            getFragmentManager().beginTransaction()
                    .replace(R.id.layout_content, fragment)
                    .commit();
        });

        return view;
    }
}
