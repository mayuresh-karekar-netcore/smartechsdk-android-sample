package com.netcore.smartech.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.netcore.smartech.sample.R;
import com.netcore.smartech.sample.utils.FirebaseAnalyticsHelper;
import com.netcore.smartech.sample.utils.HanselHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;
import com.netcore.smartech.sample.utils.Utility;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etFirstName, etLastName, etPhone, etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        trackScreenViewed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear: {
                EditText[] inputs = {etFirstName, etLastName, etPhone, etCity};
                Utility.clearInputs(inputs);
            }
            break;

            case R.id.btn_update_profile: {
                EditText[] inputs = {etFirstName, etLastName, etPhone, etCity};
                if (!Utility.areInputsEmpty(inputs)) {
                    HashMap<String, Object> profilePayload = new HashMap<>();
                    profilePayload.put("FIRST_NAME", etFirstName.getText().toString());
                    profilePayload.put("LAST_NAME", etLastName.getText().toString());
                    profilePayload.put("MOBILE", etPhone.getText().toString());
                    profilePayload.put("CITY", etCity.getText().toString());

                    SmartechHelper.updateUserProfile(this, profilePayload);
                    HanselHelper.putUserAttribute("CITY", etCity.getText().toString());
                    Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, R.string.all_fields_are_required, Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void init() {
        EditText etIdentity = findViewById(R.id.et_identity);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etPhone = findViewById(R.id.et_phone);
        etCity = findViewById(R.id.et_city);

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_update_profile).setOnClickListener(this);

        etIdentity.setText(SmartechHelper.getUserIdentity(this));
    }

    private void trackScreenViewed() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("screen_name", "Profile");
            SmartechHelper.trackEvent(this, "screen_viewed", payload);
            FirebaseAnalyticsHelper.logEvent(this, "screen_viewed", payload);
        }, 500);
    }
}