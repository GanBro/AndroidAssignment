package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CommonCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name";

    public static CommonCategoryFragment newInstance(String categoryName) {
        CommonCategoryFragment fragment = new CommonCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);
        TextView textViewCategory = view.findViewById(R.id.text_view_category);
        RecyclerView recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        RecyclerView recyclerViewCommon = view.findViewById(R.id.recycler_view_common);

        if (getArguments() != null) {
            String categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            textViewCategory.setText(categoryName);
            setCategoryContent(view, categoryName, recyclerViewRecommend, recyclerViewCommon);
        }

        return view;
    }

    private void setCategoryContent(View view, String categoryName, RecyclerView recyclerViewRecommend, RecyclerView recyclerViewCommon) {
        // 示例商品数据
        List<Product> recommendProducts = new ArrayList<>();
        recommendProducts.add(new Product(1, "商品1", 300.00, "url_to_image", 1));
        recommendProducts.add(new Product(2, "商品2", 287.00, "url_to_image", 1));
        recommendProducts.add(new Product(3, "商品3", 403.04, "url_to_image", 1));

        List<Product> commonProducts = new ArrayList<>();
        commonProducts.add(new Product(4, "古风", 150.00, "url_to_image", 1));
        commonProducts.add(new Product(5, "和风", 160.00, "url_to_image", 1));
        commonProducts.add(new Product(6, "lolita", 170.00, "url_to_image", 1));
        commonProducts.add(new Product(7, "日常", 180.00, "url_to_image", 1));

        // 设置热门推荐 RecyclerView
        ProductAdapter recommendAdapter = new ProductAdapter(getContext(), recommendProducts);
        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRecommend.setAdapter(recommendAdapter);

        // 设置常用分类 RecyclerView
        ProductAdapter commonAdapter = new ProductAdapter(getContext(), commonProducts);
        recyclerViewCommon.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewCommon.setAdapter(commonAdapter);
    }
}
