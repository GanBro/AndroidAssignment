package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.DatabaseManager;

public class AddressListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ADDRESS = 1; // 新增
    private LinearLayout addressListLayout;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        addressListLayout = findViewById(R.id.address_list);
        Button addAddressButton = findViewById(R.id.button_add_address);

        dbManager = DatabaseManager.getInstance(this);

        // 加载现有地址
        loadAddresses();

        // 设置添加地址按钮点击事件
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS); // 修改此处
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK) {
            // 重新加载地址
            loadAddresses();
        }
    }

    private void loadAddresses() {
        addressListLayout.removeAllViews(); // 确保重新加载时清空旧的视图
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor = db.query(DatabaseManager.TABLE_ADDRESSES, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_ADDRESS));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_PHONE));

            addAddressToView(address, name, phone);
        }

        cursor.close();
    }

    private void addAddressToView(String address, String name, String phone) {
        View addressItem = LayoutInflater.from(this).inflate(R.layout.item_address, addressListLayout, false);

        TextView addressTextView = addressItem.findViewById(R.id.text_view_address);
        TextView nameTextView = addressItem.findViewById(R.id.text_view_name);
        TextView phoneTextView = addressItem.findViewById(R.id.text_view_phone);

        addressTextView.setText(address);
        nameTextView.setText(name);
        phoneTextView.setText(phone);

        addressListLayout.addView(addressItem);
    }
}
