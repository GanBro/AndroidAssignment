package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.AddressDao;
import com.ganbro.shopmaster.models.Address;

public class AddAddressActivity extends AppCompatActivity {

    private EditText editTextAddress;
    private EditText editTextName;
    private EditText editTextPhone;
    private Button buttonSaveAddress;
    private AddressDao addressDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        editTextAddress = findViewById(R.id.edit_text_address);
        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        buttonSaveAddress = findViewById(R.id.button_save_address);
        addressDao = new AddressDao(this);

        buttonSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void saveAddress() {
        String address = editTextAddress.getText().toString();
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();

        if (address.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Address newAddress = new Address();
        newAddress.setAddress(address);
        newAddress.setName(name);
        newAddress.setPhone(phone);

        long result = addressDao.insertAddress(newAddress);

        if (result != -1) {
            Toast.makeText(this, "地址保存成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // 设置结果
            finish();
        } else {
            Toast.makeText(this, "保存地址失败", Toast.LENGTH_SHORT).show();
        }
    }
}
