package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;

public class ProductDetailFragment extends Fragment {

    private ImageView ivProductImageDetail;
    private TextView tvProductNameDetail;
    private TextView tvProductPriceDetail;
    private CheckBox cbAddToCartDetail;
    private Button btnAddToCartDetail;

    private AppDatabase db;
    private Product product;

    public ProductDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        ivProductImageDetail = view.findViewById(R.id.iv_product_image_detail);
        tvProductNameDetail = view.findViewById(R.id.tv_product_name_detail);
        tvProductPriceDetail = view.findViewById(R.id.tv_product_price_detail);
        cbAddToCartDetail = view.findViewById(R.id.cb_add_to_cart_detail);
        btnAddToCartDetail = view.findViewById(R.id.btn_add_to_cart_detail);

        db = AppDatabase.getInstance(getActivity());

        int productId = getArguments().getInt("product_id", -1);
        product = db.productDao().findById(productId);

        if (product != null) {
            tvProductNameDetail.setText(product.getName());
            tvProductPriceDetail.setText(String.valueOf(product.getPrice()));
            // 加载图片的逻辑
        }

        btnAddToCartDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAddToCartDetail.isChecked()) {
                    // 处理添加到购物车的逻辑
                }
            }
        });

        return view;
    }
}
