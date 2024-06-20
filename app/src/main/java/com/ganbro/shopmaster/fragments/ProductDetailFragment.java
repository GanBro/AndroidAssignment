package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";

    private Product product;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);
        }

        ImageView imageViewProduct = view.findViewById(R.id.imageView_product);
        TextView textViewProductName = view.findViewById(R.id.textView_product_name);
        TextView textViewProductPrice = view.findViewById(R.id.textView_product_price);
        CheckBox checkBoxAddToCart = view.findViewById(R.id.checkBox_add_to_cart);
        Button buttonAddToCart = view.findViewById(R.id.button_add_to_cart);

        textViewProductName.setText(product.getName());
        textViewProductPrice.setText(String.format("$%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(imageViewProduct);

        buttonAddToCart.setOnClickListener(v -> {
            if (checkBoxAddToCart.isChecked()) {
                CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(getActivity());
                cartDbHelper.addProductToCart(product);
                Toast.makeText(getActivity(), "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please check the checkbox to add to cart", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
