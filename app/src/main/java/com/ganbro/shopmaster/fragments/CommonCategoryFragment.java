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
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class CommonCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name";
    private String categoryName;
    private RecyclerView recyclerViewRecommend;
    private RecyclerView recyclerViewCommon;
    private ProductAdapter recommendAdapter;
    private ProductAdapter commonAdapter;
    private List<Product> recommendProducts;
    private List<Product> commonProducts;

    public static CommonCategoryFragment newInstance(String categoryName) {
        CommonCategoryFragment fragment = new CommonCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);

        // 设置分类名称
        TextView categoryNameTextView = view.findViewById(R.id.text_view_category);
        categoryNameTextView.setText(categoryName);

        // 设置RecyclerView
        recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        recyclerViewCommon = view.findViewById(R.id.recycler_view_common);

        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCommon.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 设置为GridLayoutManager，每行2列

        // 从数据库中获取产品数据
        loadProductsFromDatabase();

        recommendAdapter = new ProductAdapter(getContext(), recommendProducts, true);
        commonAdapter = new ProductAdapter(getContext(), commonProducts, false);

        recyclerViewRecommend.setAdapter(recommendAdapter);
        recyclerViewCommon.setAdapter(commonAdapter);

        return view;
    }

    private void loadProductsFromDatabase() {
        ProductDao productDao = new ProductDao(getContext());
        // 获取推荐产品
        recommendProducts = productDao.getRecommendedProductsByCategory(categoryName);
        // 获取常用分类产品
        commonProducts = productDao.getProductsByCategory(categoryName);
    }
}
