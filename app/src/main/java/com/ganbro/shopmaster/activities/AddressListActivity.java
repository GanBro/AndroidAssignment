package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
            startActivity(intent);
        });
    }

    private void loadAddresses() {
        // 从数据库加载地址并显示在界面上
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor = db.query(DatabaseManager.TABLE_ADDRESSES, null, null, null, null, null, null);

        // 打印表结构
        Cursor tableCursor = db.rawQuery("PRAGMA table_info(addresses);", null);
        if (tableCursor.moveToFirst()) {
            do {
                String columnName = tableCursor.getString(1);
                Log.d("TableInfo", "列: " + columnName);
            } while (tableCursor.moveToNext());
        }
        tableCursor.close();

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
