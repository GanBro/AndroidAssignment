package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.OrderItemAdapter;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;
    private TextView statusTitle;
    private ProductDao productDao;
    private String orderStatus;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerViewOrderItems = findViewById(R.id.recycler_view_order_items);
        statusTitle = findViewById(R.id.status_title);
        productDao = new ProductDao(this);

        // 获取传递的订单状态和用户ID
        orderStatus = getIntent().getStringExtra("order_status");
        userId = getIntent().getIntExtra("user_id", -1);

        Log.d("OrderStatusActivity", "订单状态: " + orderStatus + ", 用户ID: " + userId);

        if (orderStatus != null) {
            statusTitle.setText(orderStatus);
            loadOrderItems(orderStatus);
        } else {
            statusTitle.setText("订单状态");
        }

        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderItems(String status) {
        Log.d("OrderStatusActivity", "加载订单项目，状态: " + status);
        List<Product> orderItems = productDao.getOrderItemsByStatusAndUser(status, userId);
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }
}
