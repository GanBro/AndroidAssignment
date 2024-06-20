package com.ganbro.shopmaster;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;

public class AddProductActivity extends AppCompatActivity {
    private EditText productNameInput;
    private EditText productPriceInput;
    private EditText productImageUrlInput;
    private Button addProductButton;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameInput = findViewById(R.id.product_name_input);
        productPriceInput = findViewById(R.id.product_price_input);
        productImageUrlInput = findViewById(R.id.product_image_url_input);
        addProductButton = findViewById(R.id.add_product_button);

        db = AppDatabase.getInstance(this);

        addProductButton.setOnClickListener(view -> {
            String name = productNameInput.getText().toString().trim();
            String priceText = productPriceInput.getText().toString().trim();
            String imageUrl = productImageUrlInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceText) || TextUtils.isEmpty(imageUrl)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImageUrl(imageUrl);

            db.productDao().insert(product);

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
