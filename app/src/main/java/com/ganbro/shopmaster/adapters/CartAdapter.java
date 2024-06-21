package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnProductSelectedListener onProductSelectedListener;

    public CartAdapter(Context context, List<Product> productList, OnProductSelectedListener onProductSelectedListener) {
        this.context = context;
        this.productList = productList;
        this.onProductSelectedListener = onProductSelectedListener;
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
        holder.quantityTextView.setText(String.valueOf(product.getQuantity()));

        holder.checkBox.setChecked(product.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked);
            if (onProductSelectedListener != null) {
                onProductSelectedListener.onProductSelected();
            }
        });

        holder.increaseButton.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
            updateProductInDatabase(product);
            if (onProductSelectedListener != null) {
                onProductSelectedListener.onProductSelected();
            }
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
                updateProductInDatabase(product);
                if (onProductSelectedListener != null) {
                    onProductSelectedListener.onProductSelected();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void updateProductInDatabase(Product product) {
        CartDatabaseHelper db = new CartDatabaseHelper(context);
        db.updateProductQuantity(product.getId(), product.getQuantity());
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, quantityTextView;
        public ImageView productImage;
        public CheckBox checkBox;
        public ImageButton increaseButton, decreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textView_product_name);
            productPrice = itemView.findViewById(R.id.textView_product_price);
            productImage = itemView.findViewById(R.id.imageView_product);
            checkBox = itemView.findViewById(R.id.checkbox_select);
            quantityTextView = itemView.findViewById(R.id.textView_quantity);
            increaseButton = itemView.findViewById(R.id.button_increase);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
        }
    }

    public interface OnProductSelectedListener {
        void onProductSelected();
    }
}
