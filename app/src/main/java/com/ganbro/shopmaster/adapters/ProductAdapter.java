package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private boolean isRecommend;

    public ProductAdapter(Context context, List<Product> productList, boolean isRecommend) {
        this.context = context;
        this.productList = productList;
        this.isRecommend = isRecommend;
    }

    @Override
    public int getItemViewType(int position) {
        return isRecommend ? R.layout.item_recommend_product : R.layout.item_common_product;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return isRecommend ? new RecommendProductViewHolder(view) : new CommonProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (isRecommend) {
            RecommendProductViewHolder recommendHolder = (RecommendProductViewHolder) holder;
            recommendHolder.productPrice.setText(String.format("￥%.2f", product.getPrice()));
            recommendHolder.productImage.setImageResource(R.drawable.product_image); // 统一使用 product_image.png
        } else {
            CommonProductViewHolder commonHolder = (CommonProductViewHolder) holder;
            commonHolder.productName.setText(product.getName());
            commonHolder.productImage.setImageResource(R.drawable.product_image); // 统一使用 product_image.png
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class RecommendProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;

        public RecommendProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView_product);
            productPrice = itemView.findViewById(R.id.textView_product_price);
        }
    }

    public static class CommonProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;

        public CommonProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView_product);
            productName = itemView.findViewById(R.id.textView_product_name);
        }
    }
}
