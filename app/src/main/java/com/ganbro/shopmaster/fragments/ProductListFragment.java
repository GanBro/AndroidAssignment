package com.ganbro.shopmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ganbro.shopmaster.ProductAdapter;
import com.ganbro.shopmaster.ProductDetailActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;

import java.util.List;

public class ProductListFragment extends Fragment {

    private ListView lvProductList;
    private AppDatabase db;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = AppDatabase.getInstance(context);
        productList = db.productDao().getAll();
        productAdapter = new ProductAdapter(context, productList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        lvProductList = view.findViewById(R.id.lv_product_list);
        lvProductList.setAdapter(productAdapter);

        lvProductList.setOnItemClickListener((parent, view1, position, id) -> {
            Product selectedProduct = productList.get(position);
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product_id", selectedProduct.getId());
            startActivity(intent);
        });

        return view;
    }
}
