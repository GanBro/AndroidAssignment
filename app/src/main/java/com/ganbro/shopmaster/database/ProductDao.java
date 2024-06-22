package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private SQLiteDatabase db;

    public ProductDao(Context context) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductDatabaseHelper.COLUMN_NAME, product.getName());
        values.put(ProductDatabaseHelper.COLUMN_PRICE, product.getPrice());
        values.put(ProductDatabaseHelper.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(ProductDatabaseHelper.COLUMN_QUANTITY, product.getQuantity());
        values.put(ProductDatabaseHelper.COLUMN_CATEGORY, product.getCategory());
        db.insert(ProductDatabaseHelper.TABLE_PRODUCT, null, values);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCT, null, ProductDatabaseHelper.COLUMN_CATEGORY + "=?", new String[]{category}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_CATEGORY))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }
}
