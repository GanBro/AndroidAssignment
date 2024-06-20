package com.ganbro.shopmaster;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;
import com.ganbro.shopmaster.data.ProductDao;
import com.squareup.picasso.Picasso;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageViewProduct;
    private TextView textViewName, textViewPrice;
    private CheckBox checkBoxCart;
    private Button buttonAddToCart;
    private ProductDao productDao;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageViewProduct = findViewById(R.id.image_view_product);
        textViewName = findViewById(R.id.text_view_name);
        textViewPrice = findViewById(R.id.text_view_price);
        checkBoxCart = findViewById(R.id.check_box_cart);
        buttonAddToCart = findViewById(R.id.button_add_to_cart);

        productDao = AppDatabase.getInstance(this).productDao();

        int productId = getIntent().getIntExtra("productId", -1);

        Executors.newSingleThreadExecutor().execute(() -> {
            product = productDao.findById(productId);
            runOnUiThread(() -> {
                if (product != null) {
                    Picasso.get().load(product.getImageUrl()).into(imageViewProduct);
                    textViewName.setText(product.getName());
                    textViewPrice.setText(String.valueOf(product.getPrice()));
                }
            });
        });

        buttonAddToCart.setOnClickListener(v -> {
            // 添加到购物车的逻辑
        });
    }
}
