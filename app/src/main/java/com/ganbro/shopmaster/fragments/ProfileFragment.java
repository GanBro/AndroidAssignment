package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.AddressListActivity;
import com.ganbro.shopmaster.activities.ContactUsActivity;
import com.ganbro.shopmaster.activities.FavoritesActivity;
import com.ganbro.shopmaster.activities.LoginActivity;
import com.ganbro.shopmaster.activities.OrderStatusActivity;
import com.ganbro.shopmaster.activities.AboutActivity;
import com.ganbro.shopmaster.activities.PrivacyPolicyActivity;
import com.ganbro.shopmaster.database.OrderDatabaseHelper;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private TextView loginRegisterButton;
    private View myFavoritesButton;
    private View logoutButton;
    private View addressButton;
    private View completedLayout;
    private View aboutButton;
    private View manageProductsButton;
    private View deleteProductButton;
    private View customerServiceButton;
    private View privacyPolicyButton;  // New button for Privacy Policy
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
        completedLayout = view.findViewById(R.id.completed_layout);
        aboutButton = view.findViewById(R.id.about_button);
        manageProductsButton = view.findViewById(R.id.manage_products_button);
        deleteProductButton = view.findViewById(R.id.delete_product_button);
        customerServiceButton = view.findViewById(R.id.customer_service_button);
        privacyPolicyButton = view.findViewById(R.id.privacy_policy_button);  // Initialize the new button
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

        completedLayout.setOnClickListener(v -> {
            Log.d(TAG, "已完成点击事件触发");
            // 跳转到 OrderStatusActivity，显示已完成订单
            Intent intent = new Intent(getActivity(), OrderStatusActivity.class);
            intent.putExtra("order_status", "COMPLETED");
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        aboutButton.setOnClickListener(v -> {
            // 跳转到 AboutActivity
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        });

        manageProductsButton.setOnClickListener(v -> {
            // 跳转到 AddProductFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddProductFragment())
                    .addToBackStack(null)
                    .commit();
        });

        deleteProductButton.setOnClickListener(v -> {
            // 跳转到 DeleteProductFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DeleteProductFragment())
                    .addToBackStack(null)
                    .commit();
        });

        customerServiceButton.setOnClickListener(v -> {
            // 跳转到 ContactUsActivity
            Intent intent = new Intent(getActivity(), ContactUsActivity.class);
            startActivity(intent);
        });

        privacyPolicyButton.setOnClickListener(v -> {
            // 跳转到 PrivacyPolicyActivity
            Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
            startActivity(intent);
        });
    }
}
