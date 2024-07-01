package com.ganbro.shopmaster.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;

public class StoreOwnerUpdatesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_owner_updates, container, false);

        WebView webView = view.findViewById(R.id.webview_qq_space);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new WebViewClient());

        // 获取保存的Cookie
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String cookies = sharedPreferences.getString("cookies", "");

        // 设置Cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (cookies != null && !cookies.isEmpty()) {
            String[] cookieArray = cookies.split(";");
            for (String cookie : cookieArray) {
                cookieManager.setCookie("https://user.qzone.qq.com", cookie);
            }
        }

        // 处理明文HTTP流量问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        // 加载QQ空间的URL
        webView.loadUrl("https://user.qzone.qq.com/2551921037");

        return view;
    }
}