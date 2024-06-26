package com.ganbro.shopmaster.fragments;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.ganbro.shopmaster.activities.OrderDetailsActivity;
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnProductSelectedListener {

    private TextView buttonEdit;
    private Button buttonCollect;
    private Button buttonDelete;
    private CheckBox checkboxSelectAll;
    private TextView totalPriceTextView;
    private View editModeButtons;
    private Button buttonCheckout;
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
        totalPriceTextView = view.findViewById(R.id.total_price);
        editModeButtons = view.findViewById(R.id.edit_mode_buttons);
        buttonCheckout = view.findViewById(R.id.button_checkout);
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        cartProducts = cartDatabaseHelper.getAllCartProducts();

        cartAdapter = new CartAdapter(getActivity(), cartProducts, this);
        recyclerViewCart.setAdapter(cartAdapter);

        buttonEdit.setOnClickListener(v -> {
            if (isEditing) {
                editModeButtons.setVisibility(View.GONE);
                buttonEdit.setText("编辑");
            } else {
                editModeButtons.setVisibility(View.VISIBLE);
                buttonEdit.setText("完成");
            }
            isEditing = !isEditing;
            cartAdapter.setEditing(isEditing);
            cartAdapter.notifyDataSetChanged();
        });

        buttonDelete.setOnClickListener(v -> confirmDeleteSelectedItems());

        buttonCollect.setOnClickListener(v -> collectSelectedItems());

        checkboxSelectAll.setOnClickListener(v -> selectAllItems(checkboxSelectAll.isChecked()));

        buttonCheckout.setOnClickListener(v -> checkout());

        updateTotalPrice();

        return view;
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Product product : cartProducts) {
            if (product.isSelected()) {
                totalPrice += product.getPrice() * product.getQuantity();
            }
        }
        totalPriceTextView.setText(String.format("合计: ¥%.2f", totalPrice));
    }

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
        updateTotalPrice();
    }

    private void collectSelectedItems() {
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        for (Product product : cartProducts) {
            if (product.isSelected()) {
                cartDatabaseHelper.addProductToFavorites(product);
            }
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("收藏成功")
                .setMessage("选中的商品已被添加到我的收藏")
                .setPositiveButton("确定", null)
                .show();
    }

    private void selectAllItems(boolean isChecked) {
        for (Product product : cartProducts) {
            product.setSelected(isChecked);
        }
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void checkout() {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : cartProducts) {
            if (product.isSelected()) {
                selectedProducts.add(product);
            }
        }
        double totalPrice = 0;
        for (Product product : selectedProducts) {
            totalPrice += product.getPrice() * product.getQuantity();
        }

        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra("orderItems", (ArrayList<Product>) selectedProducts);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    @Override
    public void onProductSelected() {
        updateTotalPrice();
    }
}
