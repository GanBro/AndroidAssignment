package com.ganbro.shopmaster;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    private AppDatabase db;
    private List<Product> cartProductList;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        db = AppDatabase.getInstance(this);

        cartProductList = db.productDao().getAll(); // 获取购物车商品列表
        cartAdapter = new CartAdapter(this, cartProductList);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);

        double totalPrice = calculateTotalPrice(cartProductList);
        tvTotalPrice.setText("总价: " + totalPrice);

        btnCheckout.setOnClickListener(v -> {
            // 处理结算逻辑
        });
    }

    private double calculateTotalPrice(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}
