package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private OrderDatabaseHelper orderDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        orderDatabaseHelper = new OrderDatabaseHelper(this);

        findViewById(R.id.clear_cache_button).setOnClickListener(v -> {
            clearOrderDetails();
            clearAppCache();
            Toast.makeText(this, "缓存和订单详情已清除", Toast.LENGTH_SHORT).show();
        });
    }

    private void clearOrderDetails() {
        // 清除订单详情数据库内容
        orderDatabaseHelper.clearOrderDetails();
    }

    private void clearAppCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
