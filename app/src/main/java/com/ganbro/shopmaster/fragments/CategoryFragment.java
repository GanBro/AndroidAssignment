package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.HorizontalScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ganbro.shopmaster.R;

public class CategoryFragment extends Fragment {

    private ListView listViewCategories;
    private LinearLayout layoutContent;
    private TextView tvRecommendTitle;
    private HorizontalScrollView scrollViewRecommend;
    private LinearLayout layoutRecommendProducts;
    private TextView tvCommonCategories;
    private GridLayout gridCommonCategories;

    private ArrayAdapter<String> categoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listViewCategories = view.findViewById(R.id.listView_categories);
        layoutContent = view.findViewById(R.id.layout_content);
        tvRecommendTitle = view.findViewById(R.id.tv_recommend_title);
        scrollViewRecommend = view.findViewById(R.id.scrollView_recommend);
        layoutRecommendProducts = view.findViewById(R.id.layout_recommend_products);
        tvCommonCategories = view.findViewById(R.id.tv_common_categories);
        gridCommonCategories = view.findViewById(R.id.grid_common_categories);

        // 示例分类数据
        String[] categories = {"小裙子", "上衣", "下装", "外套", "配件", "包包", "妆扮", "居家好物", "办公文具", "数码周边", "游戏专区"};
        categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(categoriesAdapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categories[position];
                updateContentForCategory(category);
            }
        });

        return view;
    }

    private void updateContentForCategory(String category) {
        // 清空当前内容
        layoutRecommendProducts.removeAllViews();
        gridCommonCategories.removeAllViews();

        // 更新标题
        tvRecommendTitle.setText(category + " 推荐");

        // 根据分类更新推荐商品
        for (int i = 0; i < 3; i++) {  // 示例商品数量
            LinearLayout productLayout = new LinearLayout(getContext());
            productLayout.setOrientation(LinearLayout.VERTICAL);
            productLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            productLayout.setPadding(8, 0, 8, 0);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            imageView.setImageResource(R.drawable.placeholder_category);  // 示例图片
            productLayout.addView(imageView);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText("￥300.00");
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setGravity(Gravity.CENTER);
            productLayout.addView(textView);

            layoutRecommendProducts.addView(productLayout);
        }

        // 根据分类更新常用分类
        for (int i = 0; i < 4; i++) {  // 示例分类数量
            LinearLayout categoryLayout = new LinearLayout(getContext());
            categoryLayout.setOrientation(LinearLayout.VERTICAL);
            categoryLayout.setLayoutParams(new GridLayout.LayoutParams());
            categoryLayout.setGravity(Gravity.CENTER);
            categoryLayout.setPadding(8, 0, 8, 0);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
            imageView.setImageResource(R.drawable.placeholder_category);  // 示例图片
            categoryLayout.addView(imageView);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText("常用分类 " + (i + 1));
            textView.setGravity(Gravity.CENTER);
            categoryLayout.addView(textView);

            gridCommonCategories.addView(categoryLayout);
        }
    }
}
