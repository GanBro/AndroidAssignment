package com.ganbro.shopmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.ganbro.shopmaster.models.OrderDetail;
import com.ganbro.shopmaster.models.OrderStatus;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDao {

    private SQLiteDatabase db;

    public ProductDao(Context context) {
        DatabaseManager dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addProduct(Product product, String userEmail) {
        if (product == null || TextUtils.isEmpty(userEmail)) {
            Log.e("ProductDao", "Product or userEmail is null/empty");
            return;
        }

        if (TextUtils.isEmpty(product.getName()) || TextUtils.isEmpty(product.getImageUrl()) ||
                TextUtils.isEmpty(product.getCategory()) || TextUtils.isEmpty(product.getDescription()) ||
                product.getPrice() <= 0) {
            Log.e("ProductDao", "Invalid product data: " + product);
            return;
        }

        // Check if a product with the same ID already exists
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_PRODUCT + " WHERE " + DatabaseManager.COLUMN_ID + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(product.getId()), userEmail});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        if (exists) {
            Log.e("ProductDao", "Product with ID " + product.getId() + " already exists for user " + userEmail);
            return;
        }

        // Add prefix to the simplified image URL
        String fullImageUrl = addPrefixToImageUrl(product.getImageUrl());

        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_NAME, product.getName());
        values.put(DatabaseManager.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseManager.COLUMN_IMAGE_URL, fullImageUrl);
        values.put(DatabaseManager.COLUMN_QUANTITY, product.getQuantity());
        values.put(DatabaseManager.COLUMN_CATEGORY, product.getCategory());
        values.put(DatabaseManager.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseManager.COLUMN_IS_RECOMMENDED, product.isRecommended() ? 1 : 0);
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 0);
        values.put(DatabaseManager.COLUMN_IS_IN_CART, 0);
        values.put(DatabaseManager.COLUMN_USER_EMAIL, userEmail);
        values.put(DatabaseManager.COLUMN_ORDER_STATUS, ""); // 默认无订单状态

        try {
            long id = db.insertOrThrow(DatabaseManager.TABLE_PRODUCT, null, values);
            product.setId((int) id); // 设置自动生成的 ID
            Log.d("ProductDao", "添加产品成功，ID: " + id);
        } catch (Exception e) {
            Log.e("ProductDao", "添加产品失败: " + e.getMessage());
        }
    }

    private String addPrefixToImageUrl(String imageUrl) {
        // Example prefix: "android.resource://com.ganbro.shopmaster/drawable/"
        String prefix = "android.resource://com.ganbro.shopmaster/drawable/";
        return prefix + imageUrl;
    }



    public void addProductToFavorites(int productId, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 1);
        int rowsUpdated = db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?", new String[]{String.valueOf(productId), userEmail});
        Log.d("ProductDao", "更新收藏状态，影响的行数: " + rowsUpdated);
    }

    public boolean isProductInFavorites(int productId, String userEmail) {
        String query = "SELECT 1 FROM " + DatabaseManager.TABLE_PRODUCT + " WHERE " + DatabaseManager.COLUMN_ID + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ? AND " + DatabaseManager.COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), userEmail});
        boolean exists = (cursor.getCount() > 0);
        Log.d("ProductDao", "产品是否在收藏中: " + exists);
        cursor.close();
        return exists;
    }

    public List<Product> getAllFavorites(String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_IS_FAVORITE + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {"1", userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询收藏产品，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        true
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getProductsByCategory(String category, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {category, userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询分类产品，分类: " + category + "，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getRecommendedProductsByCategory(String category, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String selection = DatabaseManager.COLUMN_CATEGORY + " = ? AND " + DatabaseManager.COLUMN_IS_RECOMMENDED + " = 1 AND " + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {category, userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询推荐产品，分类: " + category + "，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<String> getAllCategories(String userEmail) {
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, new String[]{DatabaseManager.COLUMN_CATEGORY}, DatabaseManager.COLUMN_USER_EMAIL + " = ?", new String[]{userEmail}, DatabaseManager.COLUMN_CATEGORY, null, null);
        Log.d("ProductDao", "查询所有分类，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public List<Product> getAllProducts(String userEmail) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, DatabaseManager.COLUMN_USER_EMAIL + " = ?", new String[]{userEmail}, null, null, null);
        Log.d("ProductDao", "查询所有产品，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void initializeProducts(String userEmail) {
        Log.d("ProductDao", "初始化产品数据");
        addProduct(new Product(1, "雅鹿牛仔短裤", 26.9, "img_2", 1, "下装", "天丝面料，透气舒适", false), userEmail);
        addProduct(new Product(2, "雅鹿夏季薄款背心", 19.9, "img_3", 1, "上衣", "适合运动和休闲", true), userEmail);
        addProduct(new Product(3, "冰洁新款短袖", 69.0, "img_4", 1, "上衣", "舒适的纯棉材质", true), userEmail);
        addProduct(new Product(4, "雪中飞男女同款纯棉短袖", 69.0, "img_5", 1, "上衣", "简约设计，百搭款式", true), userEmail);
        addProduct(new Product(5, "雪中飞女士简约羽绒马甲", 79.0, "img_6", 1, "外套", "轻盈保暖，冬季必备", true), userEmail);
        addProduct(new Product(6, "唐狮集团冰丝短袖T恤", 29.9, "img_7", 1, "上衣", "冰丝面料，清凉舒适", true), userEmail);
        addProduct(new Product(7, "三福短袖t恤女", 33.1, "img_8", 1, "上衣", "圆领纯棉，情侣款", true), userEmail);
        addProduct(new Product(8, "雅鹿韩版修身短袖t恤", 19.9, "img_9", 1, "上衣", "修身设计，时尚百搭", true), userEmail);
        addProduct(new Product(9, "森马连衣裙合集", 39.0, "img_10", 1, "连衣裙", "清仓特价，多款可选", true), userEmail);
        addProduct(new Product(10, "雅鹿修身防晒衣", 29.9, "img_11", 1, "外套", "防晒防紫外线", true), userEmail);
        addProduct(new Product(11, "阿里自营V领短袖打底衫", 19.9, "img_12", 1, "上衣", "两件装，超值选择", true), userEmail);
        addProduct(new Product(12, "雅鹿牛仔短裤", 26.9, "img_13", 1, "下装", "天丝面料，舒适透气", true), userEmail);
        addProduct(new Product(13, "森马连衣裙女任选", 39.0, "img_14", 1, "连衣裙", "特价清仓，多款可选", true), userEmail);
        addProduct(new Product(14, "森马休闲裤合集", 50.0, "img_15", 1, "下装", "拍两件50元，多款任选", true), userEmail);
        addProduct(new Product(15, "三福芭蕾织带德训鞋", 75.22, "img_16", 1, "鞋子", "到手价59.2，舒适百搭", true), userEmail);
        addProduct(new Product(16, "西装风衣外套", 49.9, "img_17", 1, "外套", "百搭秋冬款，显瘦设计", true), userEmail);
        addProduct(new Product(17, "雪中飞情侣款纯棉T恤", 69.0, "img_18", 1, "上衣", "拍三件69元，情侣款", true), userEmail);
        addProduct(new Product(18, "森马复古法式甜美连衣裙", 39.0, "img_19", 1, "连衣裙", "2024新款，复古甜美", true), userEmail);
        addProduct(new Product(19, "雅鹿牛仔短裤", 26.9, "img_20", 1, "下装", "天丝面料，透气舒适", true), userEmail);
        addProduct(new Product(20, "UPF50+夏季薄款防晒衣", 32.9, "img_21", 1, "外套", "UPF50+防晒，夏季必备", true), userEmail);
        addProduct(new Product(21, "唐狮纯棉短袖女t恤", 29.0, "img_22", 1, "上衣", "多款可选，舒适透气", true), userEmail);
    }

    public Product getProductById(int id, String userEmail) {
        Product product = null;
        String selection = DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {String.valueOf(id), userEmail};
        Cursor cursor = db.query(DatabaseManager.TABLE_PRODUCT, null, selection, selectionArgs, null, null, null);
        Log.d("ProductDao", "查询产品，ID: " + id + "，用户Email: " + userEmail + "，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
            );
        }
        cursor.close();
        return product;
    }

    public List<Product> getOrderItemsByStatusAndUser(String status, String userEmail) {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT oi.* FROM " + DatabaseManager.TABLE_ORDER_ITEMS + " oi " +
                "JOIN " + DatabaseManager.TABLE_ORDER + " o ON oi." + DatabaseManager.COLUMN_ORDER_ID + " = o." + DatabaseManager.COLUMN_ID + " " +
                "WHERE o." + DatabaseManager.COLUMN_ORDER_STATUS + " = ? AND o." + DatabaseManager.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { status, userEmail };
        Cursor cursor = db.rawQuery(query, selectionArgs);
        Log.d("ProductDao", "查询订单项，状态: " + status + "，用户Email: " + userEmail + "，行数: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    private List<Product> getOrderItemsByOrderId(int orderId) {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseManager.TABLE_ORDER_ITEMS + " WHERE " + DatabaseManager.COLUMN_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_RECOMMENDED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_IS_FAVORITE)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // 根据商品名称删除商品
    public int deleteProductByName(String productName, String userEmail) {
        String selection = DatabaseManager.COLUMN_NAME + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {productName, userEmail};
        int rowsDeleted = db.delete(DatabaseManager.TABLE_PRODUCT, selection, selectionArgs);
        Log.d("ProductDao", "删除商品结果，影响的行数: " + rowsDeleted);
        return rowsDeleted;
    }

    public void removeProductFromFavorites(int productId, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_IS_FAVORITE, 0);
        int rowsUpdated = db.update(DatabaseManager.TABLE_PRODUCT, values, DatabaseManager.COLUMN_ID + "=? AND " + DatabaseManager.COLUMN_USER_EMAIL + "=?", new String[]{String.valueOf(productId), userEmail});
        Log.d("ProductDao", "取消收藏状态，影响的行数: " + rowsUpdated);
    }

}
