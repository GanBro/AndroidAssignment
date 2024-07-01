package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText subjectInput;
    private EditText messageInput;
    private Button sendButton;

    private static final String DEFAULT_EMAIL = "2551921037@qq.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        emailInput = findViewById(R.id.contact_us_email);
        subjectInput = findViewById(R.id.contact_us_subject);
        messageInput = findViewById(R.id.contact_us_message);
        sendButton = findViewById(R.id.contact_us_send_button);

        emailInput.setText(DEFAULT_EMAIL);
        emailInput.setEnabled(false);

        sendButton.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        String email = emailInput.getText().toString();
        String subject = subjectInput.getText().toString();
        String message = messageInput.getText().toString();

        if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://116.205.231.93:3000/send-email";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "邮件已发送", Toast.LENGTH_SHORT).show(),
                error -> {
                    String errorMessage = "发送邮件时出错";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("subject", subject);
                params.put("message", message);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        queue.add(stringRequest);
    }
}
