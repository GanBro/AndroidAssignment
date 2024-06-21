package com.ganbro.shopmaster.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CartFragment extends Fragment {

    private TextView buttonEdit;
    private Button buttonCollect;
    private Button buttonDelete;
    private CheckBox checkboxSelectAll;
    private View editModeButtons;
    private boolean isEditing = false;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<Product> cartProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        buttonEdit = view.findViewById(R.id.cart_edit);
        buttonCollect = view.findViewById(R.id.button_collect);
        buttonDelete = view.findViewById(R.id.button_delete);
        checkboxSelectAll = view.findViewById(R.id.checkbox_select_all);
        editModeButtons = view.findViewById(R.id.edit_mode_buttons);
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);

        // Set up RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the cart products list from the database
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        cartProducts = cartDatabaseHelper.getAllCartProducts();

        cartAdapter = new CartAdapter(getActivity(), cartProducts);
        recyclerViewCart.setAdapter(cartAdapter);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    editModeButtons.setVisibility(View.GONE);
                    buttonEdit.setText("编辑");
                } else {
                    editModeButtons.setVisibility(View.VISIBLE);
                    buttonEdit.setText("完成");
                }
                isEditing = !isEditing;
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteSelectedItems();
            }
        });

        checkboxSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllItems(checkboxSelectAll.isChecked());
            }
        });

        return view;
    }

// CartFragment.java

    private void confirmDeleteSelectedItems() {
        new AlertDialog.Builder(getActivity())
                .setTitle("确认删除")
                .setMessage("你确定要删除选中的商品吗？")
                .setPositiveButton("确认", (dialog, which) -> deleteSelectedItems())
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteSelectedItems() {
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        for (int i = cartProducts.size() - 1; i >= 0; i--) {
            Product product = cartProducts.get(i);
            if (product.isSelected()) {
                cartAdapter.removeItem(i);
                cartDatabaseHelper.deleteCartProduct(product.getId());
            }
        }
    }


    private void selectAllItems(boolean isChecked) {
        for (Product product : cartProducts) {
            product.setSelected(isChecked);
        }
        cartAdapter.notifyDataSetChanged();
    }
}