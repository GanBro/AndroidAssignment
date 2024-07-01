package com.ganbro.shopmaster;

import android.app.Application;
import android.content.SharedPreferences;
import com.ganbro.shopmaster.database.DatabaseManager;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.database.VideoDao;

public class ShopMasterApp extends Application {

    private static ShopMasterApp instance;
    private DatabaseManager databaseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        databaseManager = new DatabaseManager(this);
//        initializeProducts();
//        initializeVideos();
    }

    public static ShopMasterApp getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    private void initializeProducts() {
        ProductDao productDao = new ProductDao(this);

        // 从 SharedPreferences 中获取默认用户信息
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "2551921037@qq.com"); // 默认电子邮件

        // 初始化产品数据
        productDao.initializeProducts(userEmail);
    }

    private void initializeVideos() {
        VideoDao videoDao = new VideoDao(this);
        videoDao.initializeVideos();
    }
}
