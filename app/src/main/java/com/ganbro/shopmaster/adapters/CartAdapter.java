package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.CartActivity;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> cartProductList;

    public CartAdapter(Context context, List<Product> cartProductList) {
        this.context = context;
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProductList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("￥%.2f", product.getPrice()));
        // Load product image using a library like Picasso or Glide
        // Picasso.get().load(product.getImageUrl()).into(holder.productImage);

        holder.buttonDecrease.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(product.getQuantity()));
                updateTotalPrice();
            }
        });

        holder.buttonIncrease.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(product.getQuantity()));
            updateTotalPrice();
        });

        holder.buttonRemove.setOnClickListener(v -> {
            cartProductList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartProductList.size());
            updateTotalPrice();
        });

        holder.quantityText.setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        Button buttonDecrease;
        Button buttonIncrease;
        Button buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantityText = itemView.findViewById(R.id.quantity_text);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonRemove = itemView.findViewById(R.id.button_remove);
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : cartProductList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        // Assuming you have a method in CartActivity to update the total price TextView
        if (context instanceof CartActivity) {
            ((CartActivity) context).updateTotalPrice(totalPrice);
        }
    }
}
