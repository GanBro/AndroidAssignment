package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ganbro.shopmaster.activities.ProductDetailActivity;
import com.ganbro.shopmaster.models.Product;
import com.ganbro.shopmaster.viewmodels.HomeViewModel;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayout productContainer = view.findViewById(R.id.product_container);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productContainer.removeAllViews();
                int productCount = products.size();
                for (int i = 0; i < productCount; i += 2) {
                    LinearLayout rowLayout = new LinearLayout(getContext());
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    rowLayout.setPadding(8, 8, 8, 8);

                    // 添加第一个商品视图
                    addProductCard(rowLayout, products.get(i));

                    // 检查是否有第二个商品，并添加视图
                    if (i + 1 < productCount) {
                        addProductCard(rowLayout, products.get(i + 1));
                    } else {
                        // 如果没有第二个商品，添加一个空白视图占位
                        View emptyView = new View(getContext());
                        emptyView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        rowLayout.addView(emptyView);
                    }

                    productContainer.addView(rowLayout);
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

        productCard.setOnClickListener(v -> {
            Log.d(TAG, "Product clicked: " + product.getId());
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });

        container.addView(productCard);
    }
}
