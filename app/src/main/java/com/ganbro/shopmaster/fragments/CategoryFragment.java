package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.ProductDetailActivity;
import com.ganbro.shopmaster.adapters.CategoryAdapter;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.models.Product;
import com.ganbro.shopmaster.viewmodels.CategoryViewModel;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private RecyclerView recyclerViewRecommendProducts;
    private TextView textViewRecommend;
    private TextView textViewEmptyProducts;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProductAdapter recommendedProductAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories);
        recyclerViewProducts = view.findViewById(R.id.recycler_view_common);
        recyclerViewRecommendProducts = view.findViewById(R.id.recycler_view_recommend_products);
        textViewRecommend = view.findViewById(R.id.text_view_recommend);
        textViewEmptyProducts = view.findViewById(R.id.text_view_empty_products);

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewRecommendProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> categories) {
                if (categories != null && !categories.isEmpty()) {
                    categoryAdapter = new CategoryAdapter(getContext(), categories);
                    recyclerViewCategories.setAdapter(categoryAdapter);
                    categoryAdapter.setCategoryClickListener(new CategoryAdapter.CategoryClickListener() {
                        @Override
                        public void onCategoryClick(String category) {
                            loadCategoryData(category);
                        }
                    });
                    // 默认显示第一个分类
                    loadCategoryData(categories.get(0));
                } else {
                    Log.d("CategoryFragment", "No categories to display.");
                }
            }
        });

        categoryViewModel.getProductList().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products != null) {
                    if (!products.isEmpty()) {
                        productAdapter = new ProductAdapter(getContext(), products, false);
                        recyclerViewProducts.setAdapter(productAdapter);
                        recyclerViewProducts.setVisibility(View.VISIBLE);
                        textViewEmptyProducts.setVisibility(View.GONE);

                        productAdapter.setOnItemClickListener(product -> {
                            Log.d("CategoryFragment", "Product clicked: " + product.getId());
                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                            intent.putExtra("product_id", product.getId());
                            startActivity(intent);
                        });
                    } else {
                        recyclerViewProducts.setVisibility(View.GONE);
                        textViewEmptyProducts.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("CategoryFragment", "No products to display.");
                }
            }
        });

        categoryViewModel.getRecommendedProductList().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> recommendedProducts) {
                if (recommendedProducts != null && !recommendedProducts.isEmpty()) {
                    recommendedProductAdapter = new ProductAdapter(getContext(), recommendedProducts, true);
                    recyclerViewRecommendProducts.setAdapter(recommendedProductAdapter);
                    recyclerViewRecommendProducts.setVisibility(View.VISIBLE);
                    textViewRecommend.setVisibility(View.VISIBLE);

                    recommendedProductAdapter.setOnItemClickListener(product -> {
                        Log.d("CategoryFragment", "Recommended Product clicked: " + product.getId());
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("product_id", product.getId());
                        startActivity(intent);
                    });
                } else {
                    recyclerViewRecommendProducts.setVisibility(View.GONE);
                    textViewRecommend.setVisibility(View.GONE);
                    Log.d("CategoryFragment", "No recommended products to display.");
                }
            }
        });

        return view;
    }

    private void loadCategoryData(String category) {
        // 清空之前的推荐数据
        recyclerViewRecommendProducts.setAdapter(null);
        textViewRecommend.setVisibility(View.GONE);

        categoryViewModel.loadProductsByCategory(category);
        categoryViewModel.loadRecommendedProductsByCategory(category);
    }
}
