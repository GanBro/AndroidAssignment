package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.OrderItemAdapter;
import com.ganbro.shopmaster.models.Address;
import com.ganbro.shopmaster.models.OrderDetail;
import com.ganbro.shopmaster.models.Product;
import java.io.Serializable;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailsActivity";
    private RecyclerView recyclerViewOrderItems;
    private TextView totalPriceTextView;
    private Button submitOrderButton;
    private TextView textOrderNotes;
    private TextView textAddressName, textAddressPhone;
    private Button buttonSelectAddress;
    private List<Product> orderItems;
    private double totalPrice;
    private static final int REQUEST_CODE_SELECT_ADDRESS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recyclerViewOrderItems = findViewById(R.id.recycler_view_order_items);
        totalPriceTextView = findViewById(R.id.text_total_price);
        submitOrderButton = findViewById(R.id.submit_order_button);
        textOrderNotes = findViewById(R.id.text_order_notes);
        textAddressName = findViewById(R.id.text_address_name);
        textAddressPhone = findViewById(R.id.text_address_phone);
        buttonSelectAddress = findViewById(R.id.button_select_address);

        Intent intent = getIntent();
        if (intent != null) {
            Log.d(TAG, "接收到的 Intent: " + intent.toString());
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    Log.d(TAG, "Intent extra [" + key + "]: " + extras.get(key));
                }
            }

            OrderDetail orderDetail = (OrderDetail) intent.getSerializableExtra("orderDetail");
            if (orderDetail != null) {
                orderItems = orderDetail.getProducts();
                totalPrice = calculateTotalPrice(orderItems);

                Log.d(TAG, "收到的订单详情: " + orderDetail.toString());
                Log.d(TAG, "订单商品数量: " + orderItems.size());
                Log.d(TAG, "总价: " + totalPrice);

                recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
                OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
                recyclerViewOrderItems.setAdapter(orderItemAdapter);

                totalPriceTextView.setText(String.format("¥%.2f", totalPrice));
            } else {
                Log.d(TAG, "订单详情为空");
            }
        } else {
            Log.d(TAG, "没有接收到 Intent");
        }

        submitOrderButton.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
            paymentIntent.putExtra("amount", totalPrice);
            paymentIntent.putExtra("orderItems", (Serializable) orderItems);
            Log.d(TAG, "启动 PaymentActivity，金额: " + totalPrice + "，订单商品数量: " + orderItems.size());
            startActivity(paymentIntent);
            finish();
        });

        textOrderNotes.setOnClickListener(v -> showEditOrderNotesDialog());
        buttonSelectAddress.setOnClickListener(v -> {
            Intent selectAddressIntent = new Intent(OrderDetailsActivity.this, SelectAddressActivity.class);
            startActivityForResult(selectAddressIntent, REQUEST_CODE_SELECT_ADDRESS);
        });
    }

    private void showEditOrderNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑订单备注");

        // 设置输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(textOrderNotes.getText().toString());
        builder.setView(input);

        // 设置按钮
        builder.setPositiveButton("确定", (dialog, which) -> textOrderNotes.setText(input.getText().toString()));
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            Address selectedAddress = (Address) data.getSerializableExtra("selectedAddress");
            if (selectedAddress != null) {
                textAddressName.setText(selectedAddress.getAddress());
                textAddressPhone.setText(selectedAddress.getName() + " " + selectedAddress.getPhone());
            }
        }
    }

    private double calculateTotalPrice(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}
