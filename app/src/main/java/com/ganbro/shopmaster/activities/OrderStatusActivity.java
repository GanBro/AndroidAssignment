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
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;
    private TextView statusTitle;
    private OrderDatabaseHelper orderDatabaseHelper;
    private String orderStatus;
    private String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerViewOrderItems = findViewById(R.id.recycler_view_order_items);
        statusTitle = findViewById(R.id.status_title);
        orderDatabaseHelper = new OrderDatabaseHelper(this);

        // 手动插入测试订单数据
        orderDatabaseHelper.addOrder("PENDING_RECEIPT", 100.0, getTestProducts(), "2551921037@qq.com");

        // 获取传递的订单状态和用户邮箱
        orderStatus = getIntent().getStringExtra("order_status");
        userEmail = getIntent().getStringExtra("user_email");

        Log.d("OrderStatusActivity", "订单状态: " + orderStatus + ", 用户邮箱: " + userEmail);

        if (orderStatus != null && userEmail != null) {
            statusTitle.setText(orderStatus);
            loadOrderItems(orderStatus);
        } else {
            statusTitle.setText("订单状态");
            Log.e("OrderStatusActivity", "订单状态或用户邮箱为空");
        }

        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Product> getTestProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(0, "Test Product 1", 10.0, "android.resource://com.ganbro.shopmaster/drawable/product_image", 1, "Category1", "Description1", true, false));
        products.add(new Product(0, "Test Product 2", 20.0, "android.resource://com.ganbro.shopmaster/drawable/product_image", 2, "Category2", "Description2", true, false));
        return products;
    }

    private void loadOrderItems(String status) {
        Log.d("OrderStatusActivity", "加载订单项目，状态: " + status);
        List<Product> orderItems = orderDatabaseHelper.getOrderItemsByStatusAndUser(status, userEmail);
        Log.d("OrderStatusActivity", "找到的订单项目数量: " + orderItems.size());
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }
}
