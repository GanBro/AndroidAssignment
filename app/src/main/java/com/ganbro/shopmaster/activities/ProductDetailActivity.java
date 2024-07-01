package com.ganbro.shopmaster.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.OrderDetail;
import com.ganbro.shopmaster.models.OrderStatus;
import com.ganbro.shopmaster.models.Product;
import com.google.android.material.button.MaterialButton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private Button addToCartButton;
    private Button addToFavorites;
    private Button buyNow;
    private Button selectStyleButton;

    private int quantity = 1;
    private Product product;
    private static final String TAG = "ProductDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        addToFavorites = findViewById(R.id.add_to_favorites);
        buyNow = findViewById(R.id.buy_now);
        selectStyleButton = findViewById(R.id.select_style_button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail == null) {
            Log.d(TAG, "用户未登录，跳转到登录页面");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            loadProductDetails(productId, userEmail);
        } else {
            Toast.makeText(this, "无法加载商品详情", Toast.LENGTH_SHORT).show();
            finish();
        }

        ProductDao productDao = new ProductDao(this);
        if (productDao.isProductInFavorites(productId, userEmail)) {
            addToFavorites.setText("取消收藏");
        } else {
            addToFavorites.setText("收藏");
        }

        addToCartButton.setOnClickListener(v -> {
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(this);
            cartDbHelper.addProductToCart(product);
            Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        });

        addToFavorites.setOnClickListener(v -> {
            if (productDao.isProductInFavorites(product.getId(), userEmail)) {
                productDao.removeProductFromFavorites(product.getId(), userEmail);
                Toast.makeText(this, "商品已取消收藏", Toast.LENGTH_SHORT).show();
                addToFavorites.setText("收藏");
            } else {
                productDao.addProductToFavorites(product.getId(), userEmail);
                Toast.makeText(this, "商品已添加到收藏", Toast.LENGTH_SHORT).show();
                addToFavorites.setText("取消收藏");
            }
        });

        buyNow.setOnClickListener(v -> {
            ArrayList<Product> orderItems = new ArrayList<>();
            orderItems.add(product);

            OrderDetail orderDetail = new OrderDetail(userEmail, new Date(), OrderStatus.PENDING_PAYMENT, orderItems);

            Intent orderDetailIntent = new Intent(ProductDetailActivity.this, OrderDetailsActivity.class);
            orderDetailIntent.putExtra("orderDetail", orderDetail);
            startActivity(orderDetailIntent);
        });


        selectStyleButton.setOnClickListener(v -> showSelectStyleDialog());
    }

    private void loadProductDetails(int productId, String userEmail) {
        ProductDao productDao = new ProductDao(this);
        product = productDao.getProductById(productId, userEmail);
        if (product != null) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format("￥%.2f", product.getPrice()));
            Glide.with(this).load(product.getImageUrl()).into(productImage);
        } else {
            Log.d(TAG, "Failed to load product details");
            Toast.makeText(this, "商品详情加载失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showSelectStyleDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_style);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        ImageView styleImage = dialog.findViewById(R.id.style_image);
        TextView styleName = dialog.findViewById(R.id.style_name);
        TextView stylePrice = dialog.findViewById(R.id.style_price);
        TextView quantityText = dialog.findViewById(R.id.quantity_text);
        ImageButton buttonDecrease = dialog.findViewById(R.id.button_decrease);
        ImageButton buttonIncrease = dialog.findViewById(R.id.button_increase);
        MaterialButton buttonConfirm = dialog.findViewById(R.id.button_confirm);

        styleName.setText(product.getName());
        stylePrice.setText(String.format("￥%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(styleImage);

        buttonDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        buttonIncrease.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        buttonConfirm.setOnClickListener(v -> {
            Product selectedProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), quantity, product.getCategory(), product.getDescription(), product.isRecommended());
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(this);
            cartDbHelper.addProductToCart(selectedProduct);
            dialog.dismiss();
            Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
