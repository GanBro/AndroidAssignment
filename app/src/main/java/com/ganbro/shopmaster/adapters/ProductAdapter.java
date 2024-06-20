package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }
        TextView productName = convertView.findViewById(R.id.textView_product_name);
        TextView productPrice = convertView.findViewById(R.id.textView_product_price);
        productName.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        return convertView;
    }
}
