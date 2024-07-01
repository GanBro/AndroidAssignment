package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeFragment";
    private List<Product> productList = new ArrayList<>();
    private LinearLayout productContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        productContainer = view.findViewById(R.id.product_container);

        // 获取搜索输入框和按钮
        EditText searchInput = view.findViewById(R.id.search_bar);
        ImageView searchButton = view.findViewById(R.id.ic_search);
        ImageView sortButton = view.findViewById(R.id.ic_sort);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productList = products != null ? products : new ArrayList<>();
                displayProducts(products);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu(v);
            }
        });

        return view;
    }

    private void displayProducts(List<Product> products) {
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

            // 检查是否有第二个商品并添加视图
            if (i + 1 < productCount) {
                addProductCard(rowLayout, products.get(i + 1));
            } else {
                // 如果没有第二个商品，添加一个空视图作为占位符
                View emptyView = new View(getContext());
                emptyView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                rowLayout.addView(emptyView);
            }

            productContainer.addView(rowLayout);
        }
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

    private void filterProducts(String query) {
        if (productList == null) {
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        displayProducts(filteredList);
    }

    private void sortProductsByPrice(boolean ascending) {
        if (productList == null) {
            return;
        }

        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return ascending ? Double.compare(p1.getPrice(), p2.getPrice()) : Double.compare(p2.getPrice(), p1.getPrice());
            }
        });

        displayProducts(productList);
    }

    private void showSortMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_ascending:
                        sortProductsByPrice(true);
                        return true;
                    case R.id.sort_descending:
                        sortProductsByPrice(false);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }
}
