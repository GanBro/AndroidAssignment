package com.ganbro.shopmaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ganbro.shopmaster.R;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<String> categories;

    public CategoryAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder();
            holder.categoryName = convertView.findViewById(R.id.text_view_category_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String category = categories.get(position);
        holder.categoryName.setText(category);

        return convertView;
    }

    private static class ViewHolder {
        TextView categoryName;
    }
}
