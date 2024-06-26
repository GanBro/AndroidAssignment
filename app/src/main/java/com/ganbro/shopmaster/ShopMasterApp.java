package com.ganbro.shopmaster;

import android.app.Application;
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
        initializeProducts();
        initializeVideos();
    }

    public static ShopMasterApp getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    private void initializeProducts() {
        ProductDao productDao = new ProductDao(this);
        // Assuming user ID is 1 for initialization purposes
        productDao.initializeProducts(1);
    }

    private void initializeVideos() {
        VideoDao videoDao = new VideoDao(this);
        videoDao.initializeVideos();
    }
}
