package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.OrderItemAdapter;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;
    private TextView totalPriceTextView;
    private Button submitOrderButton;
    private List<Product> orderItems;
    private double totalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recyclerViewOrderItems = findViewById(R.id.recycler_view_order_items);
        totalPriceTextView = findViewById(R.id.text_total_price);
        submitOrderButton = findViewById(R.id.submit_order_button);

        // 获取传递的订单商品列表和总价
        orderItems = (List<Product>) getIntent().getSerializableExtra("orderItems");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        // 设置RecyclerView
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);

        // 设置总价
        totalPriceTextView.setText(String.format("¥%.2f", totalPrice));

        // 提交订单按钮点击事件
        submitOrderButton.setOnClickListener(v -> {
            // 处理提交订单逻辑
            // ...
        });
    }
}
