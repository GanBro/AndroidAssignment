package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.AddressListActivity;
import com.ganbro.shopmaster.activities.FavoritesActivity;
import com.ganbro.shopmaster.activities.LoginActivity;
import com.ganbro.shopmaster.activities.OrderStatusActivity;
import com.ganbro.shopmaster.adapters.OrderItemAdapter;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;
import com.ganbro.shopmaster.models.Product;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private TextView loginRegisterButton;
    private View myFavoritesButton;
    private View logoutButton;
    private View addressButton;
    private ImageView waitingPaymentIcon;
    private ImageView waitingReceiptIcon;
    private RecyclerView recyclerView;
    private OrderDatabaseHelper orderDatabaseHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginRegisterButton = view.findViewById(R.id.login_register_button);
        myFavoritesButton = view.findViewById(R.id.my_favorites_button);
        logoutButton = view.findViewById(R.id.logout_button);
        addressButton = view.findViewById(R.id.address_button);
        waitingPaymentIcon = view.findViewById(R.id.ic_waiting_payment);
        waitingReceiptIcon = view.findViewById(R.id.ic_waiting_receipt);
        recyclerView = view.findViewById(R.id.recycler_view_order_items);
        orderDatabaseHelper = new OrderDatabaseHelper(getContext());

        // 获取 SharedPreferences 中保存的邮箱地址
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "登录/注册");

        // 设置文本为邮箱地址
        loginRegisterButton.setText(userEmail.equals("登录/注册") ? "登录/注册" : userEmail);

        // 设置点击事件
        loginRegisterButton.setOnClickListener(v -> {
            // 跳转到 LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        myFavoritesButton.setOnClickListener(v -> {
            // 跳转到 FavoritesActivity
            Intent intent = new Intent(getActivity(), FavoritesActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // 清除登录信息并跳转到 LoginActivity
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        addressButton.setOnClickListener(v -> {
            // 跳转到 AddressListActivity
            Intent intent = new Intent(getActivity(), AddressListActivity.class);
            startActivity(intent);
        });

        waitingPaymentIcon.setOnClickListener(v -> {
            Log.d(TAG, "待付款点击事件触发");
            // 跳转到 OrderStatusActivity，显示待付款订单
            Intent intent = new Intent(getActivity(), OrderStatusActivity.class);
            intent.putExtra("order_status", "PENDING_PAYMENT");
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        waitingReceiptIcon.setOnClickListener(v -> {
            Log.d(TAG, "待收货点击事件触发");
            // 跳转到 OrderStatusActivity，显示待收货订单
            Intent intent = new Intent(getActivity(), OrderStatusActivity.class);
            intent.putExtra("order_status", "PENDING_RECEIPT");
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // 检查是否有传递的订单状态
        Bundle args = getArguments();
        if (args != null && args.containsKey("order_status")) {
            String orderStatus = args.getString("order_status");
            Log.d(TAG, "传递的订单状态: " + orderStatus);
            loadOrders(orderStatus);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadOrders(String status) {
        List<Product> orders = orderDatabaseHelper.getOrderItemsByStatusAndUser(status, userEmail);
        Log.d(TAG, "加载到的订单项数量: " + orders.size());
        OrderItemAdapter adapter = new OrderItemAdapter(getContext(), orders);
        recyclerView.setAdapter(adapter);
    }
}