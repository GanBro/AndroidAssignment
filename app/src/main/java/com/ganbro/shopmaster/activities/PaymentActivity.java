package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private TextView paymentAmount;
    private Button paymentButton;
    private Button cancelButton;
    private CartDatabaseHelper cartDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentAmount = findViewById(R.id.payment_amount);
        paymentButton = findViewById(R.id.payment_button);
        cancelButton = findViewById(R.id.cancel_button);
        cartDatabaseHelper = new CartDatabaseHelper(this);

        // 获取 SharedPreferences 中保存的用户邮箱
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        final String userEmail = sharedPreferences.getString("email", null);

        // 获取传递的支付金额和订单商品列表
        final double amount = getIntent().getDoubleExtra("amount", 0.0);
        List<Product> orderItems = (List<Product>) getIntent().getSerializableExtra("orderItems");

        // 如果 orderItems 为 null，则初始化为空列表
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        paymentAmount.setText(String.format("支付金额: ¥%.2f", amount));
        Log.d(TAG, "支付金额: " + amount);

        final List<Product> finalOrderItems = orderItems;
        final String finalUserEmail = userEmail;

        paymentButton.setOnClickListener(v -> {
            // 模拟支付成功
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            long orderId = orderDatabaseHelper.addOrder("PENDING_RECEIPT", amount, finalOrderItems, finalUserEmail);
            Log.d(TAG, "支付成功，订单ID: " + orderId);

            // 删除购物车中的已结算商品
            for (Product product : finalOrderItems) {
                cartDatabaseHelper.deleteCartProduct(product.getId());
            }

            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "PENDING_RECEIPT");
            intent.putExtra("from_payment_success", true); // 标识是从支付成功返回的
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
}
