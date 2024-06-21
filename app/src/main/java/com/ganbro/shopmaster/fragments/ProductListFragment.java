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
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化产品列表
        productList = new ArrayList<>();
        productList.add(new Product(1, "商品1", 300.00, "url_to_image", 1));
        productList.add(new Product(2, "商品2", 287.00, "url_to_image", 1));
        productList.add(new Product(3, "商品3", 403.04, "url_to_image", 1));

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerViewProducts.setAdapter(productAdapter);

        return view;
    }
}
