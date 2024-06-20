package com.ganbro.shopmaster.activities;

import android.os.Bundle;
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
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editText_name);
        editTextPrice = findViewById(R.id.editText_price);
        editTextImageUrl = findViewById(R.id.editText_image_url);
        buttonAddProduct = findViewById(R.id.button_add_product);

        dbHelper = new ProductDatabaseHelper(this);

        buttonAddProduct.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String imageUrl = editTextImageUrl.getText().toString();

            if (!name.isEmpty() && !imageUrl.isEmpty()) {
                Product product = new Product(0, name, price, imageUrl);
                dbHelper.addProduct(product);
                Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(AddProductActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
