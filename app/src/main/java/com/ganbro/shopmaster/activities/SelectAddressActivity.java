package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.AddressAdapter;
import com.ganbro.shopmaster.models.Address;
import com.ganbro.shopmaster.database.AddressDao;

import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAddresses;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        recyclerViewAddresses = findViewById(R.id.recycler_view_addresses);
        recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(this));

        // 从数据库获取收货人信息
        addressList = getAddressListFromDatabase();

        // 设置适配器
        addressAdapter = new AddressAdapter(this, addressList);
        recyclerViewAddresses.setAdapter(addressAdapter);

        // 设置适配器的点击事件
        addressAdapter.setOnItemClickListener((view, position) -> {
            Address selectedAddress = addressList.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedAddress", selectedAddress);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private List<Address> getAddressListFromDatabase() {
        AddressDao addressDao = new AddressDao(this);
        return addressDao.getAllAddresses();
    }
}
