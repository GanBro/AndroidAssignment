package com.ganbro.shopmaster.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private Button addToCartButton;
    private Button contactCustomerService;
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
        contactCustomerService = findViewById(R.id.contact_customer_service);
        addToFavorites = findViewById(R.id.add_to_favorites);
        buyNow = findViewById(R.id.buy_now);
        selectStyleButton = findViewById(R.id.select_style_button);

        int productId = getIntent().getIntExtra("product_id", -1);
        Log.d(TAG, "Received Product ID: " + productId);
        if (productId != -1) {
            loadProductDetails(productId);
        } else {
            Toast.makeText(this, "无法加载商品详情", Toast.LENGTH_SHORT).show();
            finish();
        }

        addToCartButton.setOnClickListener(v -> {
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(this);
            cartDbHelper.addProductToCart(product);
            Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        });

        contactCustomerService.setOnClickListener(v -> {
            // Contact customer service logic
        });

        addToFavorites.setOnClickListener(v -> {
            ProductDao productDao = new ProductDao(this);
            if (!productDao.isProductInFavorites(product.getId())) {
                productDao.addProductToFavorites(product);
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

    private void loadProductDetails(int productId) {
        ProductDao productDao = new ProductDao(this);
        product = productDao.getProductById(productId);
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
        Button styleM = dialog.findViewById(R.id.style_m);
        Button styleL = dialog.findViewById(R.id.style_l);
        Button styleXL = dialog.findViewById(R.id.style_xl);
        TextView quantityText = dialog.findViewById(R.id.quantity_text);
        Button buttonDecrease = dialog.findViewById(R.id.button_decrease);
        Button buttonIncrease = dialog.findViewById(R.id.button_increase);
        Button buttonConfirm = dialog.findViewById(R.id.button_confirm);

        styleName.setText(product.getName());
        stylePrice.setText(String.format("￥%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(styleImage);

        styleM.setOnClickListener(v -> selectStyle(styleM, styleL, styleXL, "M"));
        styleL.setOnClickListener(v -> selectStyle(styleM, styleL, styleXL, "L"));
        styleXL.setOnClickListener(v -> selectStyle(styleM, styleL, styleXL, "XL"));

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
