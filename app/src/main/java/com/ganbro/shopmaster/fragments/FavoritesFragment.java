package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerViewFavorites;
    private ProductAdapter productAdapter;
    private ProductDao productDao;
    private TextView categoryAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerViewFavorites = view.findViewById(R.id.recycler_view_favorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        productDao = new ProductDao(getActivity());
        List<Product> favoriteProducts = productDao.getAllFavorites();

        productAdapter = new ProductAdapter(getActivity(), favoriteProducts, false);
        recyclerViewFavorites.setAdapter(productAdapter);

        categoryAll = view.findViewById(R.id.category_all);
        categoryAll.setOnClickListener(v -> {
            List<Product> allFavorites = productDao.getAllFavorites();
            productAdapter.setProductList(allFavorites);
        });

        // 其他类别按钮的设置...

        return view;
    }
}
