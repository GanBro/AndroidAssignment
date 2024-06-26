package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ganbro.shopmaster.models.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressDao {
    private SQLiteDatabase db;

    public AddressDao(Context context) {
        db = DatabaseManager.getInstance(context).getWritableDatabase();
    }

    public long insertAddress(Address address) {
        ContentValues values = new ContentValues();
        values.put("name", address.getName());
        values.put("phone", address.getPhone());
        values.put("address", address.getAddress());
        Log.d("DatabaseInsert", "插入地址：地址=" + address.getAddress() + ", 名字=" + address.getName() + ", 电话=" + address.getPhone());
        return db.insert("addresses", null, values);
    }

    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        Cursor cursor = db.query("addresses", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Address address = new Address();
                address.setId(cursor.getInt(cursor.getColumnIndex("id")));
                address.setName(cursor.getString(cursor.getColumnIndex("name")));
                address.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                address.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                addresses.add(address);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return addresses;
    }
}
