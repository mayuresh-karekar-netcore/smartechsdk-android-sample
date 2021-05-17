package com.netcore.smartech.sample.activity;

import android.content.Intent;
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
import com.netcore.smartech.sample.utils.Keys;
import com.netcore.smartech.sample.utils.SharedPreferenceHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;
import com.netcore.smartech.sample.utils.Utility;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etIdentity, etFirstName, etLastName, etPhone, etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        trackScreenViewed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear: {
                EditText[] inputs = {etIdentity, etFirstName, etLastName, etPhone, etCity};
                Utility.clearInputs(inputs);
            }
            break;

            case R.id.btn_register: {
                EditText[] inputs = {etIdentity, etFirstName, etLastName, etPhone, etCity};
                if (!Utility.areInputsEmpty(inputs)) {
                    // NOTE: Do not pass primary key in the profile payload. As email is the primary key set on the panel for this project, email is not passed in the payload.
                    HashMap<String, Object> profilePayload = new HashMap<>();
                    profilePayload.put("FIRST_NAME", etFirstName.getText().toString());
                    profilePayload.put("LAST_NAME", etLastName.getText().toString());
                    profilePayload.put("MOBILE", etPhone.getText().toString());
                    profilePayload.put("CITY", etCity.getText().toString());

                    SharedPreferenceHelper.putBoolean(this, Keys.IS_USER_LOGGED_IN, true);
                    SharedPreferenceHelper.putString(this, Keys.LOGGED_IN_USER_IDENTITY, etIdentity.getText().toString());

                    SmartechHelper.login(this, etIdentity.getText().toString());
                    SmartechHelper.updateUserProfile(this, profilePayload);

                    HanselHelper.setUserId(etIdentity.getText().toString());
                    HanselHelper.putUserAttribute("CITY", etCity.getText().toString());

                    Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, R.string.all_fields_are_required, Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void init() {
        etIdentity = findViewById(R.id.et_identity);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etPhone = findViewById(R.id.et_phone);
        etCity = findViewById(R.id.et_city);

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private void trackScreenViewed() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("screen_name", "Register");
            SmartechHelper.trackEvent(this, "screen_viewed", payload);
            FirebaseAnalyticsHelper.logEvent(this, "screen_viewed", payload);
        }, 500);
    }
}