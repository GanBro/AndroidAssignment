package com.ganbro.shopmaster.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ProductDao productDao;
    private MutableLiveData<List<Product>> products;

    public HomeViewModel(Application application) {
        super(application);
        productDao = new ProductDao(application);
        products = new MutableLiveData<>();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> productList = productDao.getAllProducts();
        products.setValue(productList);
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
