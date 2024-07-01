package com.ganbro.shopmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.fragments.CartFragment;
import com.ganbro.shopmaster.fragments.CategoryFragment;
import com.ganbro.shopmaster.fragments.DiscoverFragment;
import com.ganbro.shopmaster.fragments.HomeFragment;
import com.ganbro.shopmaster.fragments.ProfileFragment;
import com.ganbro.shopmaster.database.ProductDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查用户是否已登录
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail == null) {
            // 用户未登录，跳转到登录页面
            Log.d(TAG, "用户未登录，跳转到登录页面");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        Log.d(TAG, "用户已登录，继续进入主页面");
        setContentView(R.layout.activity_main);

        // 初始化底部导航视图并设置监听器
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

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void showProfileFragmentWithOrderStatus(String orderStatus) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("order_status", orderStatus);
        profileFragment.setArguments(args);
        transaction.replace(R.id.fragment_container, profileFragment);
        transaction.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void initializeProductData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null); // 获取 userEmail
        if (userEmail != null) {
            ProductDao productDao = new ProductDao(this);
            productDao.initializeProducts(userEmail); // 传递 userEmail
            Log.d(TAG, "产品数据已初始化");
        } else {
            Log.d(TAG, "用户未登录或缺少电子邮件，跳过产品数据初始化");
        }
    }
}
