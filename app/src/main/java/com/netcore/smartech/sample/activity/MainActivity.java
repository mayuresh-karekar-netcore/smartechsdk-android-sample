package com.netcore.smartech.sample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.netcore.smartech.sample.R;
import com.netcore.smartech.sample.utils.FirebaseAnalyticsHelper;
import com.netcore.smartech.sample.utils.HanselHelper;
import com.netcore.smartech.sample.utils.Keys;
import com.netcore.smartech.sample.utils.SharedPreferenceHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;
import com.netcore.smartech.sample.utils.Utility;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        requestPermissions();
        trackScreenViewed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fcm_token: {
                String fcmToken = SmartechHelper.getDevicePushToken(this);
                if (!TextUtils.isEmpty(fcmToken)) {
                    Utility.copyToClipboard(this, "FCM Token", fcmToken);
                    Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.tv_guid: {
                String guid = SmartechHelper.getDeviceUniqueId(this);
                if (!TextUtils.isEmpty(guid)) {
                    Utility.copyToClipboard(this, "Smartech GUID", guid);
                    Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.tv_add_to_cart: {
                HashMap<String, Object> payload = new HashMap<>();
                payload.put("product_id", 101);
                payload.put("product_name", "Google Pixel 3");
                payload.put("price", 75599.50);
                payload.put("quantity", 2);
                payload.put("is_gift", "false"); // Wrap the boolean values around quotes.

                SmartechHelper.trackEvent(this, "smt_add_to_cart", payload);
                FirebaseAnalyticsHelper.logEvent(this, "smt_add_to_cart", payload);
                Toast.makeText(this, R.string.tracking_add_to_cart, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.tv_checkout: {
                HashMap<String, Object> payload = new HashMap<>();
                payload.put("product_id", 102);
                payload.put("product_name", "One Plus 7");
                payload.put("price", 38000.50);
                payload.put("quantity", 1);
                payload.put("is_gift", "false"); // Wrap the boolean values around quotes.

                SmartechHelper.trackEvent(this, "smt_checkout", payload);
                FirebaseAnalyticsHelper.logEvent(this, "smt_checkout", payload);
                Toast.makeText(this, R.string.tracking_checkout, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.tv_add_to_wish_list: {
                HashMap<String, Object> payload = new HashMap<>();
                payload.put("product_id", 103);
                payload.put("product_name", "Samsung Note 10");
                payload.put("price", 49999);
                payload.put("quantity", 1);
                payload.put("is_gift", "true"); // NOTE: Wrap the boolean values around quotes.

                SmartechHelper.trackEvent(this, "smt_add_to_wish_list", payload);
                FirebaseAnalyticsHelper.logEvent(this, "smt_add_to_wish_list", payload);
                Toast.makeText(this, R.string.tracking_add_to_wish_list, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.tv_update_profile: {
                // NOTE: User profile should be set only when user identity is present in Smartech SDK.
                String identity = SmartechHelper.getUserIdentity(this);
                if (!TextUtils.isEmpty(identity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    Toast.makeText(this, R.string.please_login_first, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.tv_clear_identity: {
                SmartechHelper.clearUserIdentity(this);
                SharedPreferenceHelper.putBoolean(this, Keys.IS_USER_LOGGED_IN, false);
                SharedPreferenceHelper.putString(this, Keys.LOGGED_IN_USER_IDENTITY, null);
                Toast.makeText(this, R.string.user_identity_cleared, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.tv_logout: {
                SmartechHelper.logout(this, true); // NOTE: Boolean parameter denotes whether you want to clear user's identity.
                HanselHelper.clearUserIdAndAttributes();
                SharedPreferenceHelper.putBoolean(this, Keys.IS_USER_LOGGED_IN, false);
                SharedPreferenceHelper.putString(this, Keys.LOGGED_IN_USER_IDENTITY, null);
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, R.string.you_are_logged_out, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;

            case R.id.tv_set_location: {
                // NOTE: If "SMT_IS_AUTO_FETCHED_LOCATION" metadata is set to 1 in AndroidManifest and location permissions is given, No need to set location manually, the SDK will fetch the location automatically.
                Location location = new Location("");
                location.setLatitude(18.9985652);
                location.setLongitude(72.8259925);
                SmartechHelper.setUserLocation(this, location);
                Toast.makeText(this, R.string.default_user_location_set, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.sw_opt_pn: {
                SmartechHelper.optPushNotification(this, isChecked);
            }
            break;

            case R.id.sw_opt_in_app: {
                SmartechHelper.optInAppMessage(this, isChecked);
            }
            break;

            case R.id.sw_opt_tracking: {
                SmartechHelper.optTracking(this, isChecked);
            }
            break;
        }
    }

    private void init() {
        String fcmToken = SmartechHelper.getDevicePushToken(this);
        if (!TextUtils.isEmpty(fcmToken)) {
            TextView tvFcmToken = findViewById(R.id.tv_fcm_token);
            tvFcmToken.setText(fcmToken);
            tvFcmToken.setOnClickListener(this);
        }

        String guid = SmartechHelper.getDeviceUniqueId(this);
        if (!TextUtils.isEmpty(guid)) {
            TextView tvGuid = findViewById(R.id.tv_guid);
            tvGuid.setText(guid);
            tvGuid.setOnClickListener(this);
        }

        findViewById(R.id.tv_add_to_cart).setOnClickListener(this);
        findViewById(R.id.tv_checkout).setOnClickListener(this);
        findViewById(R.id.tv_add_to_wish_list).setOnClickListener(this);

        findViewById(R.id.tv_update_profile).setOnClickListener(this);
        findViewById(R.id.tv_clear_identity).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);

        SwitchCompat swOptPn, swOptInApp, swOptTracking;
        swOptPn = findViewById(R.id.sw_opt_pn);
        swOptPn.setChecked(SmartechHelper.hasOptedPushNotification(this));
        swOptPn.setOnCheckedChangeListener(this);

        swOptInApp = findViewById(R.id.sw_opt_in_app);
        swOptInApp.setChecked(SmartechHelper.hasOptedInAppMessage(this));
        swOptInApp.setOnCheckedChangeListener(this);

        swOptTracking = findViewById(R.id.sw_opt_tracking);
        swOptTracking.setChecked(SmartechHelper.hasOptedTracking(this));
        swOptTracking.setOnCheckedChangeListener(this);
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE
        };

        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    private void trackScreenViewed() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("screen_name", "Home");
            SmartechHelper.trackEvent(this, "screen_viewed", payload);
            FirebaseAnalyticsHelper.logEvent(this, "screen_viewed", payload);
        }, 500);
    }
}