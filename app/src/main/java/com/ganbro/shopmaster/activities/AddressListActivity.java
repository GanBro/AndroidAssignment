package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.AddressDao;
import com.ganbro.shopmaster.models.Address;

import java.util.List;

public class AddressListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ADDRESS = 1;
    private LinearLayout addressListLayout;
    private AddressDao addressDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        addressListLayout = findViewById(R.id.address_list);
        Button addAddressButton = findViewById(R.id.button_add_address);

        addressDao = new AddressDao(this);

        // 加载现有地址
        loadAddresses();

        // 设置添加地址按钮点击事件
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
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
        List<Address> addresses = addressDao.getAllAddresses();
        for (Address address : addresses) {
            addAddressToView(address);
        }
    }

    private void addAddressToView(Address address) {
        View addressItem = LayoutInflater.from(this).inflate(R.layout.item_address, addressListLayout, false);

        TextView addressTextView = addressItem.findViewById(R.id.text_view_address);
        TextView nameTextView = addressItem.findViewById(R.id.text_view_name);
        TextView phoneTextView = addressItem.findViewById(R.id.text_view_phone);
        ImageView deleteButton = addressItem.findViewById(R.id.delete_address_button);

        addressTextView.setText(address.getAddress());
        nameTextView.setText(address.getName());
        phoneTextView.setText(address.getPhone());

        deleteButton.setOnClickListener(v -> {
            addressDao.deleteAddress(address.getId());
            loadAddresses();
        });

        addressListLayout.addView(addressItem);
    }
}
