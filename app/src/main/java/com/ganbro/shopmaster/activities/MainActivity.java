package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.DatabaseManager;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.fragments.CartFragment;
import com.ganbro.shopmaster.fragments.CategoryFragment;
import com.ganbro.shopmaster.fragments.DiscoverFragment;
import com.ganbro.shopmaster.fragments.HomeFragment;
import com.ganbro.shopmaster.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate: started");

        // 获取 SharedPreferences 中保存的用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        // 删除并重新创建数据库
        resetDatabase();

        // 插入示例数据
        initializeProducts(userId);

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

        // Check if the activity was started from LoginActivity and select the home fragment
        Intent intent = getIntent();
        boolean fromLogin = intent.getBooleanExtra("fromLogin", false);
        Log.d("MainActivity", "fromLogin: " + fromLogin); // 添加调试信息
        if (fromLogin) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void resetDatabase() {
        Log.d("MainActivity", "resetDatabase: started");
        this.deleteDatabase("shopmaster.db");
        // 重新创建数据库
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.getWritableDatabase();
        Log.d("MainActivity", "resetDatabase: completed");
    }

    private void initializeProducts(int userId) {
        Log.d("MainActivity", "initializeProducts: started");
        ProductDao productDao = new ProductDao(this);
        productDao.initializeProducts(userId);
        Log.d("MainActivity", "initializeProducts: completed");
    }

}
