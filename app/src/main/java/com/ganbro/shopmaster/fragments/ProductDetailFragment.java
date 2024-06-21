package com.ganbro.shopmaster.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

public class ProductDetailFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        productImage = view.findViewById(R.id.product_image);
        productName = view.findViewById(R.id.product_name);
        productDescription = view.findViewById(R.id.product_description);
        productPrice = view.findViewById(R.id.product_price);
        addToCartButton = view.findViewById(R.id.add_to_cart_button);
        contactCustomerService = view.findViewById(R.id.contact_customer_service);
        addToFavorites = view.findViewById(R.id.add_to_favorites);
        buyNow = view.findViewById(R.id.buy_now);
        selectStyleButton = view.findViewById(R.id.select_style_button);

        // Simulate getting product data
        product = new Product(1, "现货【TUMO】雨库洛牌元素 软妹茶安系短袖连衣裙", 179.00, "url_to_image", 1, "上衣"); // 添加类别参数

        // Set product details
        productName.setText(product.getName());
        productDescription.setText("预售截止10月15日，预售期为限量礼品版，包括特别礼盒x1，面巾x1，邮票x1套，书签x1，明信片x1，信封x1，礼盒袋");
        productPrice.setText(String.format("￥%.2f", product.getPrice()));
        // Load product image using a library like Picasso or Glide
        // Picasso.get().load(product.getImageUrl()).into(productImage);

        addToCartButton.setOnClickListener(v -> {
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(getContext());
            cartDbHelper.addProductToCart(product);
            Toast.makeText(getContext(), "商品已添加到购物车", Toast.LENGTH_SHORT).show();
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

        return view;
    }

    private void showSelectStyleDialog() {
        Dialog dialog = new Dialog(getContext());
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
            Product selectedProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), quantity, product.getCategory());
            CartDatabaseHelper cartDbHelper = new CartDatabaseHelper(getContext());
            cartDbHelper.addProductToCart(selectedProduct);
            dialog.dismiss();
            Toast.makeText(getContext(), "商品已添加到购物车", Toast.LENGTH_SHORT).show();
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
