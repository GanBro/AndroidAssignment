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
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        cartProducts = cartDatabaseHelper.getAllCartProducts();

        cartAdapter = new CartAdapter(getActivity(), cartProducts);
        recyclerView.setAdapter(cartAdapter);

        return view;
    }
}
