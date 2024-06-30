package com.ganbro.shopmaster.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.ProductDao;

public class DeleteProductFragment extends Fragment {

    private EditText productNameEditText;
    private Button deleteButton;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productNameEditText = view.findViewById(R.id.product_name_edit_text);
        deleteButton = view.findViewById(R.id.delete_button);

        // 获取 SharedPreferences 中保存的邮箱地址
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");

        // 设置键盘操作
        productNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                deleteProduct();
                return true;
            }
            return false;
        });

        deleteButton.setOnClickListener(v -> deleteProduct());
    }

    private void deleteProduct() {
        String productName = productNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(getContext(), "请输入商品名称", Toast.LENGTH_SHORT).show();
        } else {
            ProductDao productDao = new ProductDao(getContext());
            int rowsDeleted = productDao.deleteProductByName(productName, userEmail);
            if (rowsDeleted > 0) {
                Toast.makeText(getContext(), "商品已删除", Toast.LENGTH_SHORT).show();
                // 删除完成后返回ProfileFragment
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "未找到商品", Toast.LENGTH_SHORT).show();
            }
            productNameEditText.setText(""); // 清空输入框
        }
    }
}
