package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;

public class PaymentActivity extends AppCompatActivity {

    private TextView paymentAmount;
    private Button paymentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentAmount = findViewById(R.id.payment_amount);
        paymentButton = findViewById(R.id.payment_button);

        // 获取传递的支付金额
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        paymentAmount.setText(String.format("支付金额: ¥%.2f", amount));

        paymentButton.setOnClickListener(v -> {
            // 模拟支付成功
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            orderDatabaseHelper.addOrder("待收货", amount);

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("order_status", "待收货");
            startActivity(intent);
            finish();
        });
    }
}
