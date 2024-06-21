package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.database.ProductDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CommonCategoryFragment extends Fragment {

    private RecyclerView recyclerViewRecommend;
    private RecyclerView recyclerViewCommon;
    private ProductAdapter recommendAdapter;
    private ProductAdapter commonAdapter;
    private List<Product> recommendProducts;
    private List<Product> commonProducts;

    private static final String ARG_CATEGORY = "category";

    public static CommonCategoryFragment newInstance(String category) {
        CommonCategoryFragment fragment = new CommonCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);

        recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        recyclerViewCommon = view.findViewById(R.id.recycler_view_common);

        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCommon.setLayoutManager(new LinearLayoutManager(getContext()));

        loadCategoryContent();

        return view;
    }

    private void loadCategoryContent() {
        if (getArguments() != null) {
            String category = getArguments().getString(ARG_CATEGORY);

            ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(getContext());
            recommendProducts = dbHelper.getProductsByCategory(category);
            commonProducts = dbHelper.getProductsByCategory(category); // 根据需要修改获取逻辑

            recommendAdapter = new ProductAdapter(getContext(), recommendProducts, true);
            commonAdapter = new ProductAdapter(getContext(), commonProducts, false);

            recyclerViewRecommend.setAdapter(recommendAdapter);
            recyclerViewCommon.setAdapter(commonAdapter);
        }
    }
}
