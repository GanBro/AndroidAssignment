package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private CartDatabaseHelper cartDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        cartDatabaseHelper = new CartDatabaseHelper(this);
        loadCartData();
    }

    private void loadCartData() {
        List<Product> cartProductList = cartDatabaseHelper.getAllCartProducts();
        cartAdapter = new CartAdapter(this, cartProductList, new CartAdapter.OnProductSelectedListener() {
            @Override
            public void onProductSelected() {
                updateTotalPrice(cartProductList);
            }
        });
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalPrice(cartProductList);
    }

    private void updateTotalPrice(List<Product> cartProductList) {
        double totalPrice = 0.0;
        for (Product product : cartProductList) {
            if (product.isSelected()) {
                totalPrice += product.getPrice() * product.getQuantity();
            }
        }
        TextView totalPriceTextView = findViewById(R.id.total_price);
        totalPriceTextView.setText(String.format("合计: ¥%.2f", totalPrice));
    }
}
