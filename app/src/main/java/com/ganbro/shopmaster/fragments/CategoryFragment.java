package com.ganbro.shopmaster.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CartAdapter;
import com.ganbro.shopmaster.database.CartDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class CategoryFragment extends Fragment {

    private TextView buttonEdit;
    private Button buttonCollect;
    private Button buttonDelete;
    private CheckBox checkboxSelectAll;
    private View editModeButtons;
    private boolean isEditing = false;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<Product> cartProducts;
    private Button buttonTags;
    private ListView listViewCategories;
    private ArrayAdapter<String> categoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        buttonTags = view.findViewById(R.id.button_tags);
        buttonTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tagsFragment = new TagsFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, tagsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonEdit = view.findViewById(R.id.cart_edit);
        buttonCollect = view.findViewById(R.id.button_collect);
        buttonDelete = view.findViewById(R.id.button_delete);
        checkboxSelectAll = view.findViewById(R.id.checkbox_select_all);
        editModeButtons = view.findViewById(R.id.edit_mode_buttons);
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        listViewCategories = view.findViewById(R.id.listView_categories);

        // Set up RecyclerView
        if (recyclerViewCart != null) {
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        // Initialize the cart products list from the database
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        cartProducts = cartDatabaseHelper.getAllCartProducts();

        cartAdapter = new CartAdapter(getActivity(), cartProducts);
        if (recyclerViewCart != null) {
            recyclerViewCart.setAdapter(cartAdapter);
        }

        // Initialize the categories list
        String[] categories = {"上衣", "下装", "外套", "配件", "包包", "装扮", "居家宅品", "办公文具", "数码周边", "游戏专区"};
        categoriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(categoriesAdapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据点击的分类加载相应的内容
                loadCategoryContent(position);
            }
        });

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

    private void loadCategoryContent(int position) {
        // 根据分类位置加载不同的内容
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new UpperClothesFragment();
                break;
            case 1:
                fragment = new LowerClothesFragment();
                break;
            case 2:
                fragment = new OuterwearFragment();
                break;
            // 添加更多分类片段
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.layout_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
    }

    private void selectAllItems(boolean isChecked) {
        for (Product product : cartProducts) {
            product.setSelected(isChecked);
        }
        cartAdapter.notifyDataSetChanged();
    }
}
