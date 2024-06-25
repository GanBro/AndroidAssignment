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
import com.ganbro.shopmaster.fragments.CartFragment;
import com.ganbro.shopmaster.fragments.CategoryFragment;
import com.ganbro.shopmaster.fragments.DiscoverFragment;
import com.ganbro.shopmaster.fragments.HomeFragment;
import com.ganbro.shopmaster.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final long ONE_HOUR_MILLIS = 3600000; // 1小时的毫秒数
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查登录时间戳
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long lastLoginTimestamp = sharedPreferences.getLong("login_timestamp", 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastLoginTimestamp > ONE_HOUR_MILLIS) {
            // 超过1小时，跳转到登录页面
            Log.d(TAG, "自动登录失效，跳转到登录页面");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        Log.d(TAG, "自动登录有效，继续进入主页面");
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
}
