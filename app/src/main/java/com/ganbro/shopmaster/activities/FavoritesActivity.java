package com.ganbro.shopmaster.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.adapters.VideoAdapter;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.database.VideoDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import com.ganbro.shopmaster.models.Video;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductFavorites;
    private RecyclerView recyclerViewVideoFavorites;
    private ProductAdapter productAdapter;
    private VideoAdapter videoAdapter;
    private ProductDao productDao;
    private VideoDatabaseHelper videoDatabaseHelper;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerViewProductFavorites = findViewById(R.id.recycler_view_product_favorites);
        recyclerViewVideoFavorites = findViewById(R.id.recycler_view_video_favorites);

        recyclerViewProductFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVideoFavorites.setLayoutManager(new LinearLayoutManager(this));

        productDao = new ProductDao(this);
        videoDatabaseHelper = new VideoDatabaseHelper(this);

        // 获取 SharedPreferences 中保存的用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        List<Product> favoriteProducts = productDao.getAllFavorites(userId);
        List<Video> favoriteVideos = videoDatabaseHelper.getAllFavoriteVideos();

        productAdapter = new ProductAdapter(this, favoriteProducts, false);
        videoAdapter = new VideoAdapter(this, favoriteVideos);

        recyclerViewProductFavorites.setAdapter(productAdapter);
        recyclerViewVideoFavorites.setAdapter(videoAdapter);
    }
}
