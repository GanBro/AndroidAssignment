package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;
    private TextView totalPriceTextView;
    private Button submitOrderButton;
    private TextView textOrderNotes;
    private TextView textAddressName, textAddressPhone;
    private Button buttonSelectAddress;
    private List<Product> orderItems;
    private double totalPrice;
    private String orderStatus;
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

        // 获取传递的订单商品列表和总价
        orderItems = (List<Product>) getIntent().getSerializableExtra("orderItems");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        orderStatus = getIntent().getStringExtra("order_status");

        // 设置RecyclerView
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, orderItems);
        recyclerViewOrderItems.setAdapter(orderItemAdapter);

        // 设置总价
        totalPriceTextView.setText(String.format("¥%.2f", totalPrice));

        // 提交订单按钮点击事件
        submitOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
            intent.putExtra("amount", totalPrice);
            startActivity(intent);
            finish();
        });

        // 点击订单备注可以编辑备注
        textOrderNotes.setOnClickListener(v -> showEditOrderNotesDialog());

        // 选择收货人按钮点击事件
        buttonSelectAddress.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailsActivity.this, SelectAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
        });
    }

    private void showEditOrderNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑订单备注");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(textOrderNotes.getText().toString());
        builder.setView(input);

        // Set up the buttons
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
}
