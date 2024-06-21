package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.ProductAdapter;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CommonCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name";

    public static CommonCategoryFragment newInstance(String categoryName) {
        CommonCategoryFragment fragment = new CommonCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_category, container, false);
        TextView textViewCategory = view.findViewById(R.id.text_view_category);
        RecyclerView recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        RecyclerView recyclerViewCommon = view.findViewById(R.id.recycler_view_common);

        if (getArguments() != null) {
            String categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            textViewCategory.setText(categoryName);
            setCategoryContent(categoryName, recyclerViewRecommend, recyclerViewCommon);
        }

        return view;
    }

    private void setCategoryContent(String categoryName, RecyclerView recyclerViewRecommend, RecyclerView recyclerViewCommon) {
        // 示例商品数据
        List<Product> recommendProducts = new ArrayList<>();
        List<Product> commonProducts = new ArrayList<>();

        switch (categoryName) {
            case "上衣":
                recommendProducts.add(new Product(1, "上衣1", 100.00, "product_image", 1));
                recommendProducts.add(new Product(2, "上衣2", 120.00, "product_image", 1));
                recommendProducts.add(new Product(3, "上衣3", 120.00, "product_image", 1));
                recommendProducts.add(new Product(4, "上衣4", 120.00, "product_image", 1));
                commonProducts.add(new Product(3, "上衣3", 130.00, "product_image", 1));
                commonProducts.add(new Product(4, "上衣4", 140.00, "product_image", 1));
                break;
            case "下装":
                recommendProducts.add(new Product(1, "下装1", 150.00, "product_image", 1));
                recommendProducts.add(new Product(2, "下装2", 160.00, "product_image", 1));
                recommendProducts.add(new Product(3, "下装3", 160.00, "product_image", 1));
                recommendProducts.add(new Product(4, "下装4", 160.00, "product_image", 1));
                commonProducts.add(new Product(3, "下装3", 170.00, "product_image", 1));
                commonProducts.add(new Product(4, "下装4", 180.00, "product_image", 1));
                break;
            case "外套":
                recommendProducts.add(new Product(1, "外套1", 200.00, "product_image", 1));
                recommendProducts.add(new Product(2, "外套2", 220.00, "product_image", 1));
                recommendProducts.add(new Product(3, "外套3", 220.00, "product_image", 1));
                recommendProducts.add(new Product(4, "外套4", 220.00, "product_image", 1));
                commonProducts.add(new Product(3, "外套3", 230.00, "product_image", 1));
                commonProducts.add(new Product(4, "外套4", 240.00, "product_image", 1));
                break;
            case "配件":
                recommendProducts.add(new Product(1, "配件1", 50.00, "product_image", 1));
                recommendProducts.add(new Product(2, "配件2", 60.00, "product_image", 1));
                recommendProducts.add(new Product(3, "配件3", 60.00, "product_image", 1));
                recommendProducts.add(new Product(4, "配件4", 60.00, "product_image", 1));
                commonProducts.add(new Product(3, "配件3", 70.00, "product_image", 1));
                commonProducts.add(new Product(4, "配件4", 80.00, "product_image", 1));
                break;
            case "包包":
                recommendProducts.add(new Product(1, "包包1", 300.00, "product_image", 1));
                recommendProducts.add(new Product(2, "包包2", 320.00, "product_image", 1));
                recommendProducts.add(new Product(3, "包包3", 320.00, "product_image", 1));
                recommendProducts.add(new Product(4, "包包4", 320.00, "product_image", 1));
                commonProducts.add(new Product(3, "包包3", 330.00, "product_image", 1));
                commonProducts.add(new Product(4, "包包4", 340.00, "product_image", 1));
                break;
            // 保留其他类别...
            case "鞋子":
                recommendProducts.add(new Product(1, "鞋子1", 300.00, "product_image", 1));
                recommendProducts.add(new Product(2, "鞋子2", 320.00, "product_image", 1));
                commonProducts.add(new Product(3, "鞋子3", 330.00, "product_image", 1));
                commonProducts.add(new Product(4, "鞋子4", 340.00, "product_image", 1));
                break;
            case "饰品":
                recommendProducts.add(new Product(1, "饰品1", 300.00, "product_image", 1));
                recommendProducts.add(new Product(2, "饰品2", 320.00, "product_image", 1));
                commonProducts.add(new Product(3, "饰品3", 330.00, "product_image", 1));
                commonProducts.add(new Product(4, "饰品4", 340.00, "product_image", 1));
                break;
            case "内衣":
                recommendProducts.add(new Product(1, "内衣1", 300.00, "product_image", 1));
                recommendProducts.add(new Product(2, "内衣2", 320.00, "product_image", 1));
                commonProducts.add(new Product(3, "内衣3", 330.00, "product_image", 1));
                commonProducts.add(new Product(4, "内衣4", 340.00, "product_image", 1));
                break;
            case "运动服":
                recommendProducts.add(new Product(1, "运动服1", 300.00, "product_image", 1));
                recommendProducts.add(new Product(2, "运动服2", 320.00, "product_image", 1));
                commonProducts.add(new Product(3, "运动服3", 330.00, "product_image", 1));
                commonProducts.add(new Product(4, "运动服4", 340.00, "product_image", 1));
                break;
            default:
                recommendProducts.add(new Product(1, "默认商品1", 100.00, "product_image", 1));
                recommendProducts.add(new Product(2, "默认商品2", 200.00, "product_image", 1));
                recommendProducts.add(new Product(3, "默认商品3", 200.00, "product_image", 1));
                recommendProducts.add(new Product(4, "默认商品4", 200.00, "product_image", 1));
                commonProducts.add(new Product(3, "默认商品3", 300.00, "product_image", 1));
                commonProducts.add(new Product(4, "默认商品4", 400.00, "product_image", 1));
                break;
        }

        // 设置热门推荐 RecyclerView
        ProductAdapter recommendAdapter = new ProductAdapter(getContext(), recommendProducts, true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecommend.setLayoutManager(horizontalLayoutManager);
        recyclerViewRecommend.setAdapter(recommendAdapter);

        // 设置常用分类 RecyclerView
        ProductAdapter commonAdapter = new ProductAdapter(getContext(), commonProducts, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewCommon.setLayoutManager(gridLayoutManager);
        recyclerViewCommon.setAdapter(commonAdapter);
    }
}
