package com.netcore.smartech.sample.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Set;

public class FirebaseAnalyticsHelper {
    private static final String TAG = "FirebaseAnalyticsHelper";
    private static FirebaseAnalytics mFirebaseAnalytics;

    private static synchronized FirebaseAnalytics getFirebaseAnalytics(Context context) {
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());

        return mFirebaseAnalytics;
    }

    public static void logEvent(Context context, String eventName, HashMap<String, Object> properties) {
        if (!TextUtils.isEmpty(eventName)) {
            HashMap<String, Object> hanselData = HanselHelper.logEvent(eventName, properties, HanselHelper.ANALYTICS_VENDOR_NAME_FIREBASE);
            if (hanselData != null) {
                properties.putAll(hanselData);
            }

            Bundle propertiesBundle = new Bundle();
            Set<String> keys = properties.keySet();
            for (String key : keys) {
                propertiesBundle.putString(key, String.valueOf(properties.get(key)));
            }

            getFirebaseAnalytics(context).logEvent(eventName, propertiesBundle);
        } else {
            Log.e(TAG, "Event name is not valid.");
        }
    }
}