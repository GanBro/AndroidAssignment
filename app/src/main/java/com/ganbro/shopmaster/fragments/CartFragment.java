package com.ganbro.shopmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        editModeButtons = view.findViewById(R.id.edit_mode_buttons);
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);

        // Set up RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the cart products list from the database
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getActivity());
        cartProducts = cartDatabaseHelper.getAllCartProducts();

        // Pass context and product list to CartAdapter
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

        return view;
    }
}
