package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CartDatabaseHelper";

    private static final String DATABASE_NAME = "shopmaster.db";
    private static final int DATABASE_VERSION = 2; // 更新为最新版本

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_QUANTITY = "quantity"; // 新增的列

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER);";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 当数据库版本升级时调用
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 处理数据库降级
        Log.w(TAG, "Downgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addProductToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, product.getName());
            values.put(COLUMN_PRICE, product.getPrice());
            values.put(COLUMN_IMAGE_URL, product.getImageUrl());
            values.put(COLUMN_QUANTITY, product.getQuantity());

            Log.d(TAG, "Inserting product into cart: " + product.getName());

            db.insert(TABLE_CART, null, values);
            Log.d(TAG, "Product inserted into cart successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error inserting product into cart", e);
        } finally {
            db.close();
        }
    }

    public List<Product> getAllCartProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CART, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                    int imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);
                    int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);

                    if (idIndex == -1 || nameIndex == -1 || priceIndex == -1 || imageUrlIndex == -1 || quantityIndex == -1) {
                        throw new IllegalArgumentException("Column not found in cursor");
                    }

                    Product product = new Product(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getDouble(priceIndex),
                            cursor.getString(imageUrlIndex));
                    product.setQuantity(cursor.getInt(quantityIndex));
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching products from cart", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return productList;
    }
}
