package com.netcore.smartech.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.netcore.smartech.sample.R;
import com.netcore.smartech.sample.receiver.SmartechDeepLinkHandler;
import com.netcore.smartech.sample.utils.FirebaseAnalyticsHelper;
import com.netcore.smartech.sample.utils.HanselHelper;
import com.netcore.smartech.sample.utils.Keys;
import com.netcore.smartech.sample.utils.SharedPreferenceHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isUserLoggedIn = SharedPreferenceHelper.getBoolean(this, Keys.IS_USER_LOGGED_IN, false);
        String loggedInUserIdentity = SharedPreferenceHelper.getString(this, Keys.LOGGED_IN_USER_IDENTITY, null);
        if (isUserLoggedIn && !TextUtils.isEmpty(loggedInUserIdentity)) {
            // User is already logged in, it is recommended to login user at this point also, to get the identity of user updating the app from non-Netcore version.
            SmartechHelper.login(this, loggedInUserIdentity);
            HanselHelper.setUserId(loggedInUserIdentity);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        init();

        // NOTE: If PN is clicked in BACKGROUND or TERMINATED mode the call for deeplink and custom payload will come here.
        new SmartechDeepLinkHandler().onReceive(this, getIntent());
        trackScreenViewed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_identity: {
                String identity = edIdentity.getText().toString();
                if (!TextUtils.isEmpty(identity)) {
                    SmartechHelper.login(this, identity); // NOTE: Pass the value for PRIMARY KEY which you have set on panel. We are passing user's email id as we have set EMAIL as PRIMARY KEY on Smartech panel.
                    HanselHelper.setUserId(identity);
                    SharedPreferenceHelper.putBoolean(this, Keys.IS_USER_LOGGED_IN, true);
                    SharedPreferenceHelper.putString(this, Keys.LOGGED_IN_USER_IDENTITY, identity);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, R.string.enter_email_id, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.btn_register: {
                startActivity(new Intent(this, RegisterActivity.class));
            }
            break;

            case R.id.btn_guest_login: {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            break;
        }
    }

    private void init() {
        edIdentity = findViewById(R.id.et_identity);
        findViewById(R.id.btn_update_identity).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_guest_login).setOnClickListener(this);
    }

    private void trackScreenViewed() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("screen_name", "Login");
            SmartechHelper.trackEvent(this, "screen_viewed", payload);
            FirebaseAnalyticsHelper.logEvent(this, "screen_viewed", payload);
        }, 500);
    }
}