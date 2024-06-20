package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageViewProduct;
    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private CheckBox checkBoxAddToCart;
    private Button buttonAddToCart;
    private Product product;
    private CartDatabaseHelper cartDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageViewProduct = findViewById(R.id.imageView_product);
        textViewProductName = findViewById(R.id.textView_product_name);
        textViewProductPrice = findViewById(R.id.textView_product_price);
        checkBoxAddToCart = findViewById(R.id.checkBox_add_to_cart);
        buttonAddToCart = findViewById(R.id.button_add_to_cart);

        cartDbHelper = new CartDatabaseHelper(this);

        product = (Product) getIntent().getSerializableExtra("product");
        textViewProductName.setText(product.getName());
        textViewProductPrice.setText(String.format("$%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(imageViewProduct);

        buttonAddToCart.setOnClickListener(v -> {
            if (checkBoxAddToCart.isChecked()) {
                cartDbHelper.addProductToCart(product);
                Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please check the checkbox to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
