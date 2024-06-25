package com.ganbro.shopmaster.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText codeInput;
    private Button sendCodeButton;
    private Button loginButton;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        codeInput = findViewById(R.id.code_input);
        sendCodeButton = findViewById(R.id.send_code_button);
        loginButton = findViewById(R.id.login_button);

        // 检查是否可以自动登录
        autoLoginIfPossible();

        sendCodeButton.setOnClickListener(v -> sendVerificationCode());
        loginButton.setOnClickListener(v -> verifyCodeAndLogin());
    }

    // 检查是否可以自动登录
    private void autoLoginIfPossible() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long lastLoginTimestamp = sharedPreferences.getLong("login_timestamp", 0);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1 && System.currentTimeMillis() - lastLoginTimestamp <= 3600000) { // 1小时 = 3600000毫秒
            String email = sharedPreferences.getString("email", null);
            if (email != null) {
                // 自动登录用户
                Log.d(TAG, "自动登录成功，跳转到主页面");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("fromLogin", true);
                startActivity(intent);
                finish();
            }
        } else {
            Log.d(TAG, "没有可用的自动登录信息或自动登录已过期");
        }
    }

    private void sendVerificationCode() {
        String email = emailInput.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "请输入您的邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/send-verification-code";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.VISIBLE);
                },
                error -> {
                    String errorMessage = "发送验证码时出错";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    }
                    Log.e(TAG, "Error: " + errorMessage, error);
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        queue.add(stringRequest);
    }

    private void verifyCodeAndLogin() {
        String email = emailInput.getText().toString();
        String code = codeInput.getText().toString();
        if (code.isEmpty()) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/verify-code";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putLong("login_timestamp", System.currentTimeMillis()); // 保存当前时间戳
                    editor.apply();

                    int userId = insertOrGetUser(email);
                    editor.putInt("user_id", userId);
                    editor.apply();

                    Log.d(TAG, "登录成功，用户ID: " + userId + ", 邮箱: " + email);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("fromLogin", true);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    String errorMessage = "验证验证码时出错";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    }
                    Log.e(TAG, "Error: " + errorMessage, error);
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("code", code);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        queue.add(stringRequest);
    }

    private int insertOrGetUser(String email) {
        SQLiteDatabase db = new DatabaseManager(this).getWritableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, "email = ?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return userId;
        } else {
            ContentValues values = new ContentValues();
            values.put("email", email);
            long userId = db.insert("users", null, values);
            cursor.close();
            return (int) userId;
        }
    }
}
