package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.ganbro.shopmaster.R;
import java.util.Arrays;
import java.util.List;

public class CategoryFragment extends Fragment {

    private ListView listViewCategories;
    private List<String> categories = Arrays.asList("上衣", "下装", "外套", "配件", "包包", "鞋子", "饰品", "内衣", "运动服");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listViewCategories = view.findViewById(R.id.listView_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(adapter);

        // 设置默认选择第一个类别
        if (savedInstanceState == null) {
            displayCategory(0);
        }

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayCategory(position);
            }
        });

        return view;
    }

    private void displayCategory(int position) {
        String selectedCategory = categories.get(position);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, CommonCategoryFragment.newInstance(selectedCategory));
        transaction.commit();
    }
}
