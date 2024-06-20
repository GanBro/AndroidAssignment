package com.ganbro.shopmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private CheckBox cbAddToCart;
    private Button btnAddToCart;

    private AppDatabase db;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductPrice = findViewById(R.id.tv_product_price);
        cbAddToCart = findViewById(R.id.cb_add_to_cart);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        db = AppDatabase.getInstance(this);

        int productId = getIntent().getIntExtra("product_id", -1);
        product = db.productDao().findById(productId);

        if (product != null) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(String.valueOf(product.getPrice()));
            // 加载图片的逻辑
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAddToCart.isChecked()) {
                    // 处理添加到购物车的逻辑
                }
            }
        });
    }
}
