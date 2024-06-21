package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Product;
import java.util.List;
// CartAdapter.java

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public CartAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("￥%.2f", product.getPrice()));
        // 使用 Glide 或 Picasso 加载图片
        // Picasso.get().load(product.getImageUrl()).into(holder.productImage);

        holder.checkBox.setChecked(product.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> product.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice;
        public ImageView productImage;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textView_product_name);
            productPrice = itemView.findViewById(R.id.textView_product_price);
            productImage = itemView.findViewById(R.id.imageView_product);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }
}

