package com.ganbro.shopmaster.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.ProductDao;
import com.ganbro.shopmaster.models.Product;

public class AddProductFragment extends Fragment {

    private ProductDao productDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productDao = new ProductDao(getContext());

        EditText editTextProductName = view.findViewById(R.id.edit_text_product_name);
        EditText editTextProductPrice = view.findViewById(R.id.edit_text_product_price);
        EditText editTextProductImageUrl = view.findViewById(R.id.edit_text_product_image_url);
        EditText editTextProductCategory = view.findViewById(R.id.edit_text_product_category);
        EditText editTextProductDescription = view.findViewById(R.id.edit_text_product_description);
        CheckBox checkBoxProductRecommended = view.findViewById(R.id.check_box_product_recommended);
        Button buttonAddProduct = view.findViewById(R.id.button_add_product);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        buttonAddProduct.setOnClickListener(v -> {
            String name = editTextProductName.getText().toString();
            String priceText = editTextProductPrice.getText().toString();
            String imageUrl = editTextProductImageUrl.getText().toString();
            String category = editTextProductCategory.getText().toString();
            String description = editTextProductDescription.getText().toString();
            boolean isRecommended = checkBoxProductRecommended.isChecked();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceText) || TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)) {
                Toast.makeText(getContext(), "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "价格格式错误", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(0, name, price, imageUrl, 0, category, description, isRecommended, false);
            productDao.addProduct(product, userEmail);

            editTextProductName.setText("");
            editTextProductPrice.setText("");
            editTextProductImageUrl.setText("");
            editTextProductCategory.setText("");
            editTextProductDescription.setText("");
            checkBoxProductRecommended.setChecked(false);

            Toast.makeText(getContext(), "商品已添加", Toast.LENGTH_SHORT).show();

            // 添加商品后返回ProfileFragment
            getParentFragmentManager().popBackStack();
        });
    }
}
