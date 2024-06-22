package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private ListView listViewCategories;
    private ArrayAdapter<String> categoryAdapter;

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
            Log.d("CommonCategoryFragment", "Category name from arguments: " + categoryName);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("CommonCategoryFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);

        // 设置RecyclerView
        recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        recyclerViewCommon = view.findViewById(R.id.recycler_view_common);
        listViewCategories = view.findViewById(R.id.recycler_view_categories);

        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCommon.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 设置为GridLayoutManager，每行2列

        // 从数据库中获取产品数据
        loadProductsFromDatabase();

        recommendAdapter = new ProductAdapter(getContext(), recommendProducts, true);
        commonAdapter = new ProductAdapter(getContext(), commonProducts, false);

        recyclerViewRecommend.setAdapter(recommendAdapter);
        recyclerViewCommon.setAdapter(commonAdapter);

        // 设置分类列表
        loadCategoriesFromDatabase();

        // 设置分类点击事件
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                Log.d("CommonCategoryFragment", "Selected category: " + selectedCategory);
                loadProductsFromDatabaseForCategory(selectedCategory);
            }
        });

        return view;
    }

    private void loadProductsFromDatabase() {
        Log.d("CommonCategoryFragment", "loadProductsFromDatabase called");
        ProductDao productDao = new ProductDao(getContext());
        // 获取推荐产品
        recommendProducts = productDao.getRecommendedProductsByCategory(categoryName);
        // 获取常用分类产品
        commonProducts = productDao.getProductsByCategory(categoryName);
        Log.d("CommonCategoryFragment", "Loaded products for category " + categoryName + ": recommendProducts=" + recommendProducts.size() + ", commonProducts=" + commonProducts.size());
    }

    private void loadProductsFromDatabaseForCategory(String category) {
        Log.d("CommonCategoryFragment", "loadProductsFromDatabaseForCategory called for category: " + category);
        ProductDao productDao = new ProductDao(getContext());
        recommendProducts = productDao.getRecommendedProductsByCategory(category);
        commonProducts = productDao.getProductsByCategory(category);

        recommendAdapter.setProductList(recommendProducts);
        commonAdapter.setProductList(commonProducts);
        recommendAdapter.notifyDataSetChanged();
        commonAdapter.notifyDataSetChanged();

        Log.d("CommonCategoryFragment", "Loaded products for category " + category + ": recommendProducts=" + recommendProducts.size() + ", commonProducts=" + commonProducts.size());
    }

    private void loadCategoriesFromDatabase() {
        Log.d("CommonCategoryFragment", "loadCategoriesFromDatabase called");
        ProductDao productDao = new ProductDao(getContext());
        List<String> categories = productDao.getAllCategories();
        Log.d("CommonCategoryFragment", "Loaded categories: " + categories.toString());
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(categoryAdapter);
    }
}
