package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.fragments.CartFragment;
import com.ganbro.shopmaster.fragments.CategoryFragment;
import com.ganbro.shopmaster.fragments.DiscoverFragment;
import com.ganbro.shopmaster.fragments.HomeFragment;
import com.ganbro.shopmaster.fragments.ProfileFragment;
import com.ganbro.shopmaster.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 插入示例数据
        insertSampleData();

        // Initialize bottom navigation view and set listener
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_category:
                        selectedFragment = new CategoryFragment();
                        break;
                    case R.id.navigation_discover:
                        selectedFragment = new DiscoverFragment();
                        break;
                    case R.id.navigation_cart:
                        selectedFragment = new CartFragment();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void insertSampleData() {
        ProductDao productDao = new ProductDao(this);
        productDao.addProduct(new Product(0, "上衣1", 100.00, "product_image", 1, "上衣"));
        productDao.addProduct(new Product(0, "上衣2", 120.00, "product_image", 1, "上衣"));
        productDao.addProduct(new Product(0, "上衣3", 130.00, "product_image", 1, "上衣"));
        productDao.addProduct(new Product(0, "上衣4", 140.00, "product_image", 1, "上衣"));
        productDao.addProduct(new Product(0, "下装1", 150.00, "product_image", 1, "下装"));
        productDao.addProduct(new Product(0, "下装2", 160.00, "product_image", 1, "下装"));
        productDao.addProduct(new Product(0, "外套1", 200.00, "product_image", 1, "外套"));
        productDao.addProduct(new Product(0, "外套2", 220.00, "product_image", 1, "外套"));
        productDao.addProduct(new Product(0, "配件1", 50.00, "product_image", 1, "配件"));
        productDao.addProduct(new Product(0, "配件2", 60.00, "product_image", 1, "配件"));
        productDao.addProduct(new Product(0, "包包1", 300.00, "product_image", 1, "包包"));
        productDao.addProduct(new Product(0, "包包2", 320.00, "product_image", 1, "包包"));
        // 你可以继续添加更多类别和商品
    }
}
