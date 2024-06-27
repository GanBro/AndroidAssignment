package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private TextView textTotalPrice;
    private Button buttonCheckout;
    private CartAdapter cartAdapter;
    private CartDatabaseHelper cartDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        textTotalPrice = view.findViewById(R.id.total_price);
        buttonCheckout = view.findViewById(R.id.button_checkout);
        cartDatabaseHelper = new CartDatabaseHelper(getContext());

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCartProducts();

        buttonCheckout.setOnClickListener(v -> {
            if (cartAdapter.getItemCount() == 0) {
                Toast.makeText(getContext(), "购物车为空，无法结算", Toast.LENGTH_SHORT).show();
            } else {
                // 跳转到订单详情页面
                double totalPrice = calculateTotalPrice();
                List<Product> orderItems = cartAdapter.getCartProducts();

                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra("orderItems", new ArrayList<>(orderItems));
                intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);
            }
        });
    }

    private void loadCartProducts() {
        List<Product> cartProducts = cartDatabaseHelper.getAllCartProducts();
        cartAdapter = new CartAdapter(getContext(), cartProducts, this::updateTotalPrice);
        recyclerViewCart.setAdapter(cartAdapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        textTotalPrice.setText(String.format("合计: ¥%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : cartAdapter.getCartProducts()) {
            if (product.isSelected()) {
                totalPrice += product.getPrice() * product.getQuantity();
            }
        }
        return totalPrice;
    }
}
