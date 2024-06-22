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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private boolean isHorizontalScroll;

    public ProductAdapter(Context context, List<Product> productList, boolean isHorizontalScroll) {
        this.context = context;
        this.productList = productList;
        this.isHorizontalScroll = isHorizontalScroll;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isHorizontalScroll ? R.layout.item_recommend_product : R.layout.item_common_product;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("¥%.2f", product.getPrice()));
        // 加载图片和其他必要的数据设置...
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textView_product_name);
            productPrice = itemView.findViewById(R.id.textView_product_price);
            productImage = itemView.findViewById(R.id.imageView_product);
        }
    }
}
