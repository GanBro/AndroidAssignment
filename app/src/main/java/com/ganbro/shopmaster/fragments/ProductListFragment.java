package com.ganbro.shopmaster.fragments;

import android.content.Intent;
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
import com.ganbro.shopmaster.activities.ProductDetailActivity;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;
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

        // 从数据库中获取产品列表
        ProductDao productDao = new ProductDao(getContext());
        productList = productDao.getAllProducts();

        productAdapter = new ProductAdapter(getContext(), productList, false);
        recyclerViewProducts.setAdapter(productAdapter);

        // 设置商品点击事件
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                startActivity(intent);
            }
        });

        return view;
    }
}
