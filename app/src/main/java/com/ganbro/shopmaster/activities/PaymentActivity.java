package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private TextView paymentAmount;
    private Button paymentButton;
    private Button cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentAmount = findViewById(R.id.payment_amount);
        paymentButton = findViewById(R.id.payment_button);
        cancelButton = findViewById(R.id.cancel_button);

        // 获取 SharedPreferences 中保存的用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        final int userId = sharedPreferences.getInt("user_id", -1);

        // 获取传递的支付金额和订单商品列表
        final double amount = getIntent().getDoubleExtra("amount", 0.0);
        List<Product> tempOrderItems = (List<Product>) getIntent().getSerializableExtra("orderItems");

        // 如果 orderItems 为 null，则初始化为空列表
        if (tempOrderItems == null) {
            tempOrderItems = new ArrayList<>();
        }
        final List<Product> orderItems = tempOrderItems;

        paymentAmount.setText(String.format("支付金额: ¥%.2f", amount));
        Log.d(TAG, "支付金额: " + amount);

        paymentButton.setOnClickListener(v -> {
            // 模拟支付成功
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            long orderId = orderDatabaseHelper.addOrder("待收货", amount, orderItems, userId);
            Log.d(TAG, "支付成功，订单ID: " + orderId);

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "待收货");
            startActivity(intent);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            // 模拟支付取消
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            long orderId = orderDatabaseHelper.addOrder("待付款", amount, orderItems, userId);
            Log.d(TAG, "支付取消，订单ID: " + orderId);

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "待付款");
            startActivity(intent);
            finish();
        });
    }
}
