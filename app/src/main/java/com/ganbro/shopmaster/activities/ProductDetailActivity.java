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
import com.ganbro.shopmaster.models.Product;
import com.google.android.material.button.MaterialButton;

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
    private String selectedStyle = "M";
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

        // 获取 SharedPreferences 中保存的用户邮箱
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail == null) {
            // 用户未登录，跳转到登录页面
            Log.d(TAG, "用户未登录，跳转到登录页面");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        int productId = getIntent().getIntExtra("product_id", -1);
        Log.d(TAG, "Received Product ID: " + productId);
        if (productId != -1) {
            loadProductDetails(productId, userEmail);
        } else {
            Toast.makeText(this, "无法加载商品详情", Toast.LENGTH_SHORT).show();
            finish();
        }

        addToCartButton.setOnClickListener(v -> {
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(this);
            cartDbHelper.addProductToCart(product);
            Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        });

        addToFavorites.setOnClickListener(v -> {
            ProductDao productDao = new ProductDao(this);
            if (!productDao.isProductInFavorites(product.getId(), userEmail)) {
                productDao.addProductToFavorites(product.getId(), userEmail);
                Toast.makeText(this, "商品已添加到收藏", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "商品已经在收藏列表中", Toast.LENGTH_SHORT).show();
            }
        });

        buyNow.setOnClickListener(v -> {
            // Buy now logic
        });

        selectStyleButton.setOnClickListener(v -> showSelectStyleDialog());
    }

    private void loadProductDetails(int productId, String userEmail) {
        ProductDao productDao = new ProductDao(this);
        product = productDao.getProductById(productId, userEmail);
        if (product != null) {
            Log.d(TAG, "Loaded Product: " + product.getName());
            productName.setText(product.getName());
            productDescription.setText(product.getDescription()); // 从数据库加载描述
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
        MaterialButton styleM = dialog.findViewById(R.id.style_m);
        MaterialButton styleL = dialog.findViewById(R.id.style_l);
        MaterialButton styleXL = dialog.findViewById(R.id.style_xl);
        TextView quantityText = dialog.findViewById(R.id.quantity_text);
        ImageButton buttonDecrease = dialog.findViewById(R.id.button_decrease);
        ImageButton buttonIncrease = dialog.findViewById(R.id.button_increase);
        MaterialButton buttonConfirm = dialog.findViewById(R.id.button_confirm);

        styleName.setText(product.getName());
        stylePrice.setText(String.format("￥%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(styleImage);

        View.OnClickListener styleClickListener = v -> {
            // Reset selection state
            styleM.setSelected(false);
            styleL.setSelected(false);
            styleXL.setSelected(false);

            // Highlight the selected button
            v.setSelected(true);

            // Update selected style
            switch (v.getId()) {
                case R.id.style_m:
                    selectedStyle = "M";
                    break;
                case R.id.style_l:
                    selectedStyle = "L";
                    break;
                case R.id.style_xl:
                    selectedStyle = "XL";
                    break;
            }
        };

        styleM.setOnClickListener(styleClickListener);
        styleL.setOnClickListener(styleClickListener);
        styleXL.setOnClickListener(styleClickListener);

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




    private void selectStyle(Button styleM, Button styleL, Button styleXL, String style) {
        selectedStyle = style;
        styleM.setBackgroundResource(style.equals("M") ? R.drawable.selected_button_background : R.drawable.unselected_button_background);
        styleL.setBackgroundResource(style.equals("L") ? R.drawable.selected_button_background : R.drawable.unselected_button_background);
        styleXL.setBackgroundResource(style.equals("XL") ? R.drawable.selected_button_background : R.drawable.unselected_button_background);
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
