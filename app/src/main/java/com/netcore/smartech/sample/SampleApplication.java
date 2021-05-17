package com.netcore.smartech.sample;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.netcore.smartech.sample.activity.ProfileActivity;
import com.netcore.smartech.sample.activity.RegisterActivity;
import com.netcore.smartech.sample.utils.FirebaseAnalyticsHelper;
import com.netcore.smartech.sample.utils.HanselHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;

import java.util.HashMap;

import io.hansel.hanselsdk.HanselActionListener;
import io.hansel.ujmtracker.HanselInternalEventsListener;

public class SampleApplication extends MultiDexApplication implements HanselInternalEventsListener, HanselActionListener {
    private static final String TAG = "SampleApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        SmartechHelper.initialize(this);
        SmartechHelper.registerSmartechDeepLinkHandler(this);
        SmartechHelper.setPushNotificationIcon(this, R.drawable.ic_notification);

        HanselHelper.enableDebugLogs();
        HanselHelper.registerHanselInternalEventsListener(this);
        HanselHelper.registerHanselActionListener(HanselHelper.ACTION_OPEN_PROFILE_ACTIVITY, this);
        HanselHelper.registerHanselActionListener(HanselHelper.ACTION_OPEN_REGISTER_ACTIVITY, this);
    }

    /**
     * This method receives the callbacks for Hansel internal events like "hansel_nudge_event". Using this callback
     * method you can capture the response of nudge events and pass them to your analytics.
     * NOTE: To receive the callback of the nudge event "Send the event to Analytics partner" checkbox must be
     * checked while creating the nudge on the dashboard.
     *
     * @param eventName      - Name of the Hansel event.
     * @param dataFromHansel - Data from Hansel. Eg. nudge_name, nudge_type.
     */
    @Override
    public void onEvent(String eventName, HashMap dataFromHansel) {
        if ("hansel_nudge_event".equals(eventName)) {
            FirebaseAnalyticsHelper.logEvent(this, "hansel_nudge_event", dataFromHansel);
        }
    }

    /**
     * This method receives the callback when an custom action is invoked. By validating the action name
     * the custom action can be performed like navigating user to specific activity.
     *
     * @param actionName - Name of the action to trigger.
     */
    @Override
    public void onActionPerformed(String actionName) {
        Log.d(TAG, "Inside onActionPerformed." + actionName);

        switch (actionName) {
            case HanselHelper.ACTION_OPEN_PROFILE_ACTIVITY: {
                SmartechHelper.openDeeplink(this, ProfileActivity.class, null);
            }
            break;

            case HanselHelper.ACTION_OPEN_REGISTER_ACTIVITY: {
                SmartechHelper.openDeeplink(this, RegisterActivity.class, null);
            }
            break;

            default: {
                Log.e(TAG, "Failed to perform the action.");
            }
            break;
        }
    }
}