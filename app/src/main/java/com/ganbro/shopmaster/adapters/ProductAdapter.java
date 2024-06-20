package com.ganbro.shopmaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ganbro.shopmaster.data.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, parent, false);
        }

        Product product = getItem(position);

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        TextView textViewPrice = convertView.findViewById(R.id.text_view_price);

        textViewName.setText(product.getName());
        textViewPrice.setText(String.valueOf(product.getPrice()));

        return convertView;
    }
}
