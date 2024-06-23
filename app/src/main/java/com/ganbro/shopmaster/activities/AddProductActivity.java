package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextImageUrl;
    private EditText editTextCategory;
    private EditText editTextDescription; // 新增描述输入框
    private Button buttonAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription); // 初始化描述输入框
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                double price = Double.parseDouble(editTextPrice.getText().toString().trim());
                String imageUrl = editTextImageUrl.getText().toString().trim();
                String category = editTextCategory.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim(); // 获取描述

                if (name.isEmpty() || price <= 0 || imageUrl.isEmpty() || category.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Product product = new Product(0, name, price, imageUrl, 1, category, description, false); // 传递描述
                ProductDao productDao = new ProductDao(AddProductActivity.this);
                productDao.addProduct(product);

                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
