package com.ganbro.shopmaster.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        contactCustomerService = findViewById(R.id.contact_customer_service);
        addToFavorites = findViewById(R.id.add_to_favorites);
        buyNow = findViewById(R.id.buy_now);
        selectStyleButton = findViewById(R.id.select_style_button);

        // Simulate getting product data
        product = new Product(1, "现货【TUMO】雨库洛牌元素 软妹茶安系短袖连衣裙", 179.00, "url_to_image", 1);

        // Set product details
        productName.setText(product.getName());
        productDescription.setText("预售截止10月15日，预售期为限量礼品版，包括特别礼盒x1，面巾x1，邮票x1套，书签x1，明信片x1，信封x1，礼盒袋");
        productPrice.setText(String.format("￥%.2f", product.getPrice()));
        // Load product image using a library like Picasso or Glide
        // Picasso.get().load(product.getImageUrl()).into(productImage);

        addToCartButton.setOnClickListener(v -> {
            // Add to cart logic
        });

        contactCustomerService.setOnClickListener(v -> {
            // Contact customer service logic
        });

        addToFavorites.setOnClickListener(v -> {
            // Add to favorites logic
        });

        buyNow.setOnClickListener(v -> {
            // Buy now logic
        });

        selectStyleButton.setOnClickListener(v -> showSelectStyleDialog());
    }

    private void showSelectStyleDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_style);

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

        styleName.setText(productName.getText());
        stylePrice.setText(productPrice.getText());
        // Load style image using a library like Picasso or Glide
        // Picasso.get().load(product.getImageUrl()).into(styleImage);

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
            // Add the selected product to the cart
            Product selectedProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), quantity);

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
}
