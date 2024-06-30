package com.ganbro.shopmaster.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.adapters.VideoAdapter;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.database.VideoDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import com.ganbro.shopmaster.models.Video;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private String type;
    private RecyclerView recyclerView;
    private TextView title;
    private ProductDao productDao;
    private VideoDatabaseHelper videoDatabaseHelper;

    public static FavoritesFragment newInstance(String type) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        title = view.findViewById(R.id.title);
        recyclerView = view.findViewById(R.id.recycler_view);

        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }

        productDao = new ProductDao(getContext());
        videoDatabaseHelper = new VideoDatabaseHelper(getContext());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail == null) {
            // 用户未登录，显示相应信息或跳转到登录页面
            title.setText("请先登录");
            return view;
        }

        if ("product".equals(type)) {
            title.setText("商品收藏");
            List<Product> favoriteProducts = productDao.getAllFavorites(userEmail);
            ProductAdapter productAdapter = new ProductAdapter(getContext(), favoriteProducts, false);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 设置为两列布局
            recyclerView.setAdapter(productAdapter);
        } else if ("video".equals(type)) {
            title.setText("视频收藏");
            List<Video> favoriteVideos = videoDatabaseHelper.getAllFavoriteVideos();
            VideoAdapter videoAdapter = new VideoAdapter(getContext(), favoriteVideos);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // 视频还是单列布局
            recyclerView.setAdapter(videoAdapter);
        }

        return view;
    }
}
