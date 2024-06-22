package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductSelectedListener listener;
    private CartDatabaseHelper db;

    public CartAdapter(Context context, List<Product> productList, OnProductSelectedListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.db = new CartDatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
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

    class CartViewHolder extends RecyclerView.ViewHolder {

        private CheckBox selectCheckBox;
        private ImageView productImageView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView quantityTextView;
        private ImageButton increaseButton;
        private ImageButton decreaseButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            selectCheckBox = itemView.findViewById(R.id.checkbox_select);
            productImageView = itemView.findViewById(R.id.imageView_product);
            nameTextView = itemView.findViewById(R.id.textView_product_name);
            priceTextView = itemView.findViewById(R.id.textView_product_price);
            quantityTextView = itemView.findViewById(R.id.textView_quantity);
            increaseButton = itemView.findViewById(R.id.button_increase);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
        }

        public void bind(Product product) {
            nameTextView.setText(product.getName());
            priceTextView.setText(String.format("¥%.2f", product.getPrice()));
            quantityTextView.setText(String.valueOf(product.getQuantity()));
            selectCheckBox.setChecked(product.isSelected());

            // 在这里加载产品图片（如果需要的话）

            increaseButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Product updatedProduct = productList.get(position);
                updatedProduct.setQuantity(updatedProduct.getQuantity() + 1);
                quantityTextView.setText(String.valueOf(updatedProduct.getQuantity()));
                db.updateProductQuantity(updatedProduct.getId(), updatedProduct.getQuantity());
                listener.onProductSelected();
            });

            decreaseButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Product updatedProduct = productList.get(position);
                if (updatedProduct.getQuantity() > 1) {
                    updatedProduct.setQuantity(updatedProduct.getQuantity() - 1);
                    quantityTextView.setText(String.valueOf(updatedProduct.getQuantity()));
                    db.updateProductQuantity(updatedProduct.getId(), updatedProduct.getQuantity());
                    listener.onProductSelected();
                }
            });

            selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                Product updatedProduct = productList.get(position);
                updatedProduct.setSelected(isChecked);
                listener.onProductSelected();
            });
        }
    }

    public interface OnProductSelectedListener {
        void onProductSelected();
    }
}
