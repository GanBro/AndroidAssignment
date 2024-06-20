package com.ganbro.shopmaster;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ganbro.shopmaster.data.AppDatabase;
import com.ganbro.shopmaster.data.Product;
import com.ganbro.shopmaster.data.ProductDao;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProductDao productDao = AppDatabase.getInstance(this).productDao();

        // 使用 ExecutorService 在后台线程中执行数据库操作
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Product product = new Product();
                product.setName("Example Product");
                product.setPrice(99.99);
                product.setImageUrl("https://example.com/product.png");
                productDao.insert(product);
            }
        });
    }
}
