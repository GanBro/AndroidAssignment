package com.ganbro.shopmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;
import com.ganbro.shopmaster.data.ProductDao;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private Button buttonAddProduct;
    private ProductDao productDao;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewProducts = findViewById(R.id.list_view_products);
        buttonAddProduct = findViewById(R.id.button_add_product);

        productDao = AppDatabase.getInstance(this).productDao();

        loadProducts();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductDetailActivity.class));
            }
        });

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product product = (Product) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            startActivity(intent);
        });
    }

    private void loadProducts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> products = productDao.getAll();
            runOnUiThread(() -> {
                productAdapter = new ProductAdapter(MainActivity.this, products);
                listViewProducts.setAdapter(productAdapter);
            });
        });
    }
}
