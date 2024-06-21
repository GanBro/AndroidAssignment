package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
public class CategoryFragment extends Fragment {

    private ListView listViewCategories;
    private ArrayAdapter<String> categoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listViewCategories = view.findViewById(R.id.listView_categories);

        // 示例分类数据
        String[] categories = {"小裙子", "上衣", "下装", "外套", "配件", "包包", "妆扮", "居家好物", "办公文具", "数码周边", "游戏专区"};
        categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(categoriesAdapter);

        // 示例商品数据
        // 这里可以使用RecyclerView或者手动添加布局

        return view;
    }
}


