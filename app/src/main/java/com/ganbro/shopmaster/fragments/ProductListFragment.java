package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.database.ProductDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    private ListView listViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private ProductDatabaseHelper dbHelper;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        listViewProducts = view.findViewById(R.id.listview_products);
        dbHelper = new ProductDatabaseHelper(getActivity());

        productList = dbHelper.getAllProducts();
        productAdapter = new ProductAdapter(getActivity(), productList);
        listViewProducts.setAdapter(productAdapter);

        listViewProducts.setOnItemClickListener((parent, view1, position, id) -> {
            Product product = productList.get(position);
            ProductDetailFragment fragment = ProductDetailFragment.newInstance(product);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
