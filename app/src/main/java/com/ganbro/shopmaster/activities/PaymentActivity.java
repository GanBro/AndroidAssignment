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

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        final String userEmail = sharedPreferences.getString("email", null);

        final double amount = getIntent().getDoubleExtra("amount", 0.0);
        List<Product> orderItems = (List<Product>) getIntent().getSerializableExtra("orderItems");

        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        paymentAmount.setText(String.format("支付金额: ¥%.2f", amount));
        Log.d(TAG, "支付金额: " + amount);

        final List<Product> finalOrderItems = orderItems;
        final String finalUserEmail = userEmail;

        paymentButton.setOnClickListener(v -> handlePayment(finalOrderItems, finalUserEmail, amount, "COMPLETED", "支付成功", true));

        cancelButton.setOnClickListener(v -> handlePayment(finalOrderItems, finalUserEmail, amount, "PENDING_PAYMENT", "支付取消", false));
    }

    private void handlePayment(List<Product> orderItems, String userEmail, double amount, String orderStatus, String toastMessage, boolean deleteCartItems) {
        OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
        long orderId = orderDatabaseHelper.addOrder(orderStatus, amount, orderItems, userEmail);
        Log.d(TAG, toastMessage + "，订单ID: " + orderId);

        if (deleteCartItems) {
            // 删除购物车中的已结算商品
            for (Product product : orderItems) {
                cartDatabaseHelper.deleteCartProduct(product.getId());
            }
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.putExtra("order_status", orderStatus);
        if (deleteCartItems) {
            intent.putExtra("from_payment_success", true);
        } else {
            intent.putExtra("from_payment_cancel", true);
        }
        // 清除活动栈中的其他活动以确保返回首页
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
