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

        // 获取 SharedPreferences 中保存的用户邮箱
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        final String userEmail = sharedPreferences.getString("email", null);

        // 获取传递的支付金额和订单商品列表
        final double amount = getIntent().getDoubleExtra("amount", 0.0);
        List<Product> orderItems = getSerializableExtraAsProductList(getIntent(), "orderItems");

        Log.d(TAG, "支付金额: " + amount);
        Log.d(TAG, "获取的产品列表: " + orderItems);

        // 如果 orderItems 为 null，则初始化为空列表
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        paymentAmount.setText(String.format("支付金额: ¥%.2f", amount));

        final List<Product> finalOrderItems = orderItems;
        final String finalUserEmail = userEmail;

        paymentButton.setOnClickListener(v -> {
            // 模拟支付成功
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            long orderId = orderDatabaseHelper.addOrder("PENDING_RECEIPT", amount, finalOrderItems, finalUserEmail);
            Log.d(TAG, "支付成功，订单ID: " + orderId);

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "PENDING_RECEIPT");
            startActivity(intent);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            // 模拟支付取消
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            long orderId = orderDatabaseHelper.addOrder("PENDING_PAYMENT", amount, finalOrderItems, finalUserEmail);
            Log.d(TAG, "支付取消，订单ID: " + orderId);

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "PENDING_PAYMENT");
            startActivity(intent);
            finish();
        });
    }

    @SuppressWarnings("unchecked")
    private List<Product> getSerializableExtraAsProductList(Intent intent, String key) {
        try {
            return (List<Product>) intent.getSerializableExtra(key);
        } catch (ClassCastException e) {
            Log.e(TAG, "将 Serializable 转换为 List<Product> 时出错: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
