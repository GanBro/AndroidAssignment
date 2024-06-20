package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.ProductDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextImageUrl;
    private Button buttonAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                double price = Double.parseDouble(editTextPrice.getText().toString().trim());
                String imageUrl = editTextImageUrl.getText().toString().trim();

                if (name.isEmpty() || price <= 0 || imageUrl.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 创建 Product 对象时传递 quantity 参数
                Product product = new Product(0, name, price, imageUrl, 1);

                ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(AddProductActivity.this);
                dbHelper.addProduct(product);

                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
