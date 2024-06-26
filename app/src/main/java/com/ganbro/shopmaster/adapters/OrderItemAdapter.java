package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Product;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<Product> orderItems;

    public OrderItemAdapter(Context context, List<Product> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Product product = orderItems.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("¥%.2f", product.getPrice()));
        holder.productQuantity.setText(String.format("x%d", product.getQuantity()));
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }
}
