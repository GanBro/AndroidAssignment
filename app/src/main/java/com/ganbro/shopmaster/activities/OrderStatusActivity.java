package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.OrderItemAdapter;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;
    private TextView statusTitle;
    private OrderDatabaseHelper orderDatabaseHelper;
    private String orderStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerViewOrderItems = findViewById(R.id.recycler_view_order_items);
        statusTitle = findViewById(R.id.status_title);
        orderDatabaseHelper = new OrderDatabaseHelper(this);

        // 获取传递的订单状态
        orderStatus = getIntent().getStringExtra("order_status");
        if (orderStatus != null) {
            statusTitle.setText(orderStatus);
            loadOrderItems(orderStatus);
        } else {
            statusTitle.setText("订单状态");
        }

        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderItems(String status) {
        List<Product> orderItems = orderDatabaseHelper.getOrderItemsByStatus(status);
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }
}
