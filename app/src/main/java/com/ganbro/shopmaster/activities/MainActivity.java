package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.database.ProductDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddProduct;
    private ListView listViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddProduct = findViewById(R.id.button_add_product);
        listViewProducts = findViewById(R.id.listview_products);
        dbHelper = new ProductDatabaseHelper(this);

        productList = dbHelper.getAllProducts();
        productAdapter = new ProductAdapter(this, productList);
        listViewProducts.setAdapter(productAdapter);

        buttonAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivityForResult(intent, 1);
        });

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", productList.get(position));
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            productList.clear();
            productList.addAll(dbHelper.getAllProducts());
            productAdapter.notifyDataSetChanged();
        }
    }
}
