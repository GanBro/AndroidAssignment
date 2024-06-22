package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Product;
import com.ganbro.shopmaster.viewmodels.CategoryViewModel;
import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        final LinearLayout productContainer = view.findViewById(R.id.product_container);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productContainer.removeAllViews();
                for (Product product : products) {
                    addProductCard(productContainer, product);
                }
            }
        });

        return view;
    }

    private void addProductCard(LinearLayout container, Product product) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View productCard = inflater.inflate(R.layout.item_product, container, false);

        ImageView productImage = productCard.findViewById(R.id.imageView_product);
        TextView productName = productCard.findViewById(R.id.textView_product_name);
        TextView productPrice = productCard.findViewById(R.id.textView_product_price);

        productName.setText(product.getName());
        productPrice.setText(String.format("￥%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(productImage);

        container.addView(productCard);
    }
}
