package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> cartProducts;
    private OnProductSelectedListener onProductSelectedListener;
    private boolean isEditing = false;

    public interface OnProductSelectedListener {
        void onProductSelected();
    }

    public CartAdapter(Context context, List<Product> cartProducts, OnProductSelectedListener onProductSelectedListener) {
        this.context = context;
        this.cartProducts = cartProducts;
        this.onProductSelectedListener = onProductSelectedListener;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProducts.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("¥%.2f", product.getPrice()));
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        holder.checkboxSelect.setChecked(product.isSelected());
        holder.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked);
            onProductSelectedListener.onProductSelected();
        });

        holder.buttonIncrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            product.setQuantity(++quantity);
            holder.quantityText.setText(String.valueOf(quantity));
            onProductSelectedListener.onProductSelected();
            CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);
            dbHelper.updateProductQuantity(product.getId(), quantity);
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 1) {
                product.setQuantity(--quantity);
                holder.quantityText.setText(String.valueOf(quantity));
                onProductSelectedListener.onProductSelected();
                CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);
                dbHelper.updateProductQuantity(product.getId(), quantity);
            } else {
                // 数量减为0时移除商品项
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    removeItem(pos);
                    onProductSelectedListener.onProductSelected();
                    CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);
                    dbHelper.deleteCartProduct(product.getId());
                }
            }
        });

        holder.buttonRemove.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        holder.buttonRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                removeItem(pos);
                onProductSelectedListener.onProductSelected();
                CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);
                dbHelper.deleteCartProduct(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartProducts.size()) {
            cartProducts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkboxSelect;
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        Button buttonIncrease;
        Button buttonDecrease;
        Button buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxSelect = itemView.findViewById(R.id.checkbox_select);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantityText = itemView.findViewById(R.id.quantity_text);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
            buttonRemove = itemView.findViewById(R.id.button_remove);
        }
    }
}
