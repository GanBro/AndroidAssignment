package com.ganbro.shopmaster;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.data.Product;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView textViewTotalPrice;
    private Button buttonCheckout;
    private com.ganbro.shopmaster.CartAdapter cartAdapter;
    private List<Product> cartProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        textViewTotalPrice = findViewById(R.id.text_view_total_price);
        buttonCheckout = findViewById(R.id.button_checkout);

        cartProducts = new ArrayList<>(); // 这里应该从数据库或缓存中获取购物车商品
        cartAdapter = new com.ganbro.shopmaster.CartAdapter(cartProducts);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        buttonCheckout.setOnClickListener(v -> {
            // 结算逻辑
        });

        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : cartProducts) {
            totalPrice += product.getPrice();
        }
        textViewTotalPrice.setText("总价: " + totalPrice);
    }
}
