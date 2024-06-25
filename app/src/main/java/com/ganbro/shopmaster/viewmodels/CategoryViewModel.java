package com.ganbro.shopmaster.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> categoryList;
    private MutableLiveData<List<Product>> productList;
    private MutableLiveData<List<Product>> recommendedProductList;
    private ProductDao productDao;
    private int userId;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        productDao = new ProductDao(application);

        // 获取 SharedPreferences 中保存的用户ID
        SharedPreferences sharedPreferences = application.getSharedPreferences("UserPrefs", Application.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);
    }

    public LiveData<List<String>> getCategoryList() {
        if (categoryList == null) {
            categoryList = new MutableLiveData<>();
            loadCategories();
        }
        return categoryList;
    }

    public LiveData<List<Product>> getProductList() {
        if (productList == null) {
            productList = new MutableLiveData<>();
        }
        return productList;
    }

    public LiveData<List<Product>> getRecommendedProductList() {
        if (recommendedProductList == null) {
            recommendedProductList = new MutableLiveData<>();
        }
        return recommendedProductList;
    }

    private void loadCategories() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> categories = productDao.getAllCategories(userId);
                if (categories != null && !categories.isEmpty()) {
                    categoryList.postValue(categories);
                } else {
                    Log.d("CategoryViewModel", "No categories found in database.");
                }
            }
        }).start();
    }

    public void loadProductsByCategory(final String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Product> products = productDao.getProductsByCategory(category, userId);
                if (products != null && !products.isEmpty()) {
                    productList.postValue(products);
                } else {
                    Log.d("CategoryViewModel", "No products found in category: " + category);
                }
            }
        }).start();
    }

    public void loadRecommendedProductsByCategory(final String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Product> recommendedProducts = productDao.getRecommendedProductsByCategory(category, userId);
                if (recommendedProducts != null && !recommendedProducts.isEmpty()) {
                    recommendedProductList.postValue(recommendedProducts);
                } else {
                    Log.d("CategoryViewModel", "No recommended products found in category: " + category);
                }
            }
        }).start();
    }
}
