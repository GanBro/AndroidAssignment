package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView textViewTotalPrice;
    private Button buttonCheckout;
    private CartAdapter cartAdapter;
    private List<Product> cartProductList = new ArrayList<>();
    private CartDatabaseHelper cartDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerView_cart);
        textViewTotalPrice = findViewById(R.id.textView_total_price);
        buttonCheckout = findViewById(R.id.button_checkout);
        cartDbHelper = new CartDatabaseHelper(this);

        cartProductList = cartDbHelper.getAllCartProducts();
        cartAdapter = new CartAdapter(this, cartProductList);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalPrice(calculateTotalPrice());

        buttonCheckout.setOnClickListener(v -> {
            // Handle checkout logic
            Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        });
    }

    public void updateTotalPrice(double totalPrice) {
        textViewTotalPrice.setText(String.format("合计：￥%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : cartProductList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }
}
