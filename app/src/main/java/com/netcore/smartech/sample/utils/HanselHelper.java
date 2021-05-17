package com.netcore.smartech.sample.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import io.hansel.core.logger.HSLLogLevel;
import io.hansel.hanselsdk.Hansel;
import io.hansel.hanselsdk.HanselActionListener;
import io.hansel.ujmtracker.HanselInternalEventsListener;
import io.hansel.ujmtracker.HanselTracker;

public class HanselHelper {
    private static final String TAG = "HanselHelper";

    public static final String ANALYTICS_VENDOR_NAME_CLEVERTAP = "ctp";
    public static final String ANALYTICS_VENDOR_NAME_MIXPANEL = "mxp";
    public static final String ANALYTICS_VENDOR_NAME_AMPLITUDE = "amp";
    public static final String ANALYTICS_VENDOR_NAME_SEGMENT_IO = "smt";
    public static final String ANALYTICS_VENDOR_NAME_FIREBASE = "fbs";
    public static final String ANALYTICS_VENDOR_NAME_OMNITURE = "omni";
    public static final String ANALYTICS_VENDOR_NAME_GOOGLE = "ga";
    public static final String ANALYTICS_VENDOR_NAME_LOCALYTICS = "loca";
    public static final String ANALYTICS_VENDOR_NAME_WEBENGAGE = "wbe";
    public static final String ANALYTICS_VENDOR_NAME_HANSEL = "hsl";

    public static final String ACTION_OPEN_PROFILE_ACTIVITY = "action_open_profile_activity";
    public static final String ACTION_OPEN_REGISTER_ACTIVITY = "action_open_register_activity";

    /**
     * This method will enable the Hansel/PX debug logs which can be used for debugging.
     */
    public static void enableDebugLogs() {
        HSLLogLevel.all.setEnabled(true);
        HSLLogLevel.mid.setEnabled(true);
        HSLLogLevel.debug.setEnabled(true);
    }

    /**
     * This method will set the user ID locally in the device, used to uniquely identify the user.
     *
     * @param userId - Unique user ID of the user. Eg. Email, Mobile, Customer ID, etc.
     */
    public static void setUserId(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            Hansel.getUser().setUserId(userId);
        } else {
            Log.e(TAG, "User ID is null or empty.");
        }
    }

    /**
     * This method will clear the user ID and attributes set locally in the device.
     */
    public static void clearUserIdAndAttributes() {
        Hansel.getUser().clear();
    }

    /**
     * This method will set the specified attribute of the user locally. eg. Age = 25.
     *
     * @param userAttribute - Name of the attribute to set.
     * @param value         - Value for the attribute.
     */
    public static void putUserAttribute(String userAttribute, String value) {
        Hansel.getUser().putAttribute(userAttribute, value);
    }

    /**
     * This method will set the specified attribute of the user locally. eg. Age = 25.
     *
     * @param userAttribute - Name of the attribute to set.
     * @param value         - Value for the attribute.
     */
    public static void putUserAttribute(String userAttribute, double value) {
        Hansel.getUser().putAttribute(userAttribute, value);
    }

    /**
     * This method will set the specified attribute of the user locally. eg. Age = 25.
     *
     * @param userAttribute - Name of the attribute to set.
     * @param value         - Value for the attribute.
     */
    public static void putUserAttribute(String userAttribute, boolean value) {
        Hansel.getUser().putAttribute(userAttribute, value);
    }

    /**
     * This method will clear the value for the specified attribute of the user set locally. eg. Age = 25.
     *
     * @param userAttribute - Name of the attribute to clear.
     */
    public static void clearUserAttribute(String userAttribute) {
        Hansel.getUser().clearAttribute(userAttribute);
    }

    /**
     * This method will log the event locally. Such events can be used to trigger a nudge event.
     * NOTE: You will be using smartech.trackEvent() or HanselTracker.logEvent() method according to your contract with Netcore.
     * smartech.trackEvent()    - Events will be sent to Smartech server for analytics.
     * HanselTracker.logEvent() - Events will be logged locally in the device.
     *
     * @param eventName           -  Name of the event.
     * @param properties          - Properties/Attributes of the event.
     * @param analyticsVendorName - Name of the analytics vendor name. eg. ctp for Clevertap.
     */
    public static HashMap<String, Object> logEvent(String eventName, HashMap<String, Object> properties, String analyticsVendorName) {
        if (!TextUtils.isEmpty(eventName) && !TextUtils.isEmpty(analyticsVendorName)) {
            return HanselTracker.logEvent(eventName, analyticsVendorName, properties);
        } else {
            Log.e(TAG, "Event name or analytics vendor is not valid.");
        }

        return null;
    }

    /**
     * This method registers the Hansel internal events listener. Using which you can catch Hansel's events like
     * hansel_nudge_event and log them to your analytics.
     *
     * @param listener - Lister to the event.
     */
    public static void registerHanselInternalEventsListener(HanselInternalEventsListener listener) {
        HanselTracker.registerListener(listener);
    }

    /**
     * This method registers the custom action that can be triggered as a response for a nudge event. The registered action will be
     * available on Hansel dashboard and can be set as a response to nudge event. Example one can crate an action which navigates
     * the user to specific activity in the app.
     *
     * @param actionName - Name of the action.
     * @param listener   - Action listener.
     */
    public static void registerHanselActionListener(String actionName, HanselActionListener listener) {
        if (!TextUtils.isEmpty(actionName)) {
            Hansel.registerHanselActionListener(actionName, listener);
        } else {
            Log.e(TAG, "Action name is not valid.");
        }
    }
}