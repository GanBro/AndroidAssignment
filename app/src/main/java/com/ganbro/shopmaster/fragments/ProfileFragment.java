package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.FavoritesActivity;
import com.ganbro.shopmaster.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    private TextView loginRegisterButton;
    private View myFavoritesButton;

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

        // 获取 SharedPreferences 中保存的邮箱地址和用户ID
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "登录/注册");
        int userId = sharedPreferences.getInt("user_id", -1);

        // 设置文本为邮箱地址
        if (userId == -1) {
            loginRegisterButton.setText("登录/注册");
        } else {
            loginRegisterButton.setText(email);
        }

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
    }
}
