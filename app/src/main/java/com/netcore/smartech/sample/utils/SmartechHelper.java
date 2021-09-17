package com.netcore.smartech.sample.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.netcore.android.SMTBundleKeys;
import com.netcore.android.Smartech;
import com.netcore.android.inapp.InAppCustomHTMLListener;
import com.netcore.android.logger.SMTDebugLevel;
import com.netcore.android.smartechpush.SmartPush;
import com.netcore.android.smartechpush.notification.SMTNotificationOptions;
import com.netcore.android.smartechpush.notification.channel.SMTNotificationChannel;
import com.netcore.smartech.sample.R;
import com.netcore.smartech.sample.activity.LoginActivity;
import com.netcore.smartech.sample.activity.ProfileActivity;
import com.netcore.smartech.sample.activity.RegisterActivity;
import com.netcore.smartech.sample.receiver.SmartechDeepLinkHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SmartechHelper {
    private static final String TAG = "SmartechHelper";

    /**
     * This method will initialize the Smartech SDK and will track app install/update events.
     **/
    public static void initialize(Application application) {
        Smartech smartech = Smartech.getInstance(new WeakReference<>(application.getApplicationContext()));

        smartech.initializeSdk(application); // Initializing the SDK.
        smartech.setDebugLevel(SMTDebugLevel.Level.VERBOSE); // Enabling Smartech logs for testing.
        smartech.trackAppInstallUpdateBySmartech(); // Tracking app install/update event.
        setFCMToken(application.getApplicationContext());
    }

    /**
     * This method will set the push notification icon to Smartech SDK.
     *
     * @param resId - Resource Id of the drawable to be set as notification icon.
     **/
    @SuppressLint("ResourceType")
    public static void setPushNotificationIcon(Context context, @DrawableRes int resId) {
        SMTNotificationOptions options = new SMTNotificationOptions(context);
        options.setSmallIconTransparentId(resId);
        options.setTransparentIconBgColor(context.getString(R.color.colorAccent));
        SmartPush.getInstance(new WeakReference<>(context)).setNotificationOptions(options);
    }

    /**
     * This method will create the notification channel and group.
     *
     * @param channelId   - Id of the channel  to be created.
     * @param channelName - Name of the channel  to be created.
     * @param groupId     - Id of the group in which channel will belong.
     * @param groupName   - Name of the group in which channel will belong.
     **/
    public static void createNotificationChannel(Context context, String channelId, String channelName, String groupId, String groupName) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SmartPush smartPush = SmartPush.getInstance(new WeakReference<>(context));
            smartPush.createNotificationChannelGroup(groupId, groupName);

            SMTNotificationChannel.Builder smtChannelBuilder = new SMTNotificationChannel.Builder(channelId, channelName, NotificationManager.IMPORTANCE_MAX);
            smtChannelBuilder.setChannelGroupId(groupId);
            smtChannelBuilder.setChannelDescription("This is sample notification channel.");
            smtChannelBuilder.setNotificationSound("sample_sound");

            smartPush.createNotificationChannel(smtChannelBuilder.build());
        }
    }

    /**
     * This method will set user identity and login user to Smartech.
     *
     * @param identity - Value for the PRIMARY KEY set on the Smartech panel. This app uses user's email as identity as PRIMARY KEY set on the panel is EMAIL.
     **/
    public static void login(Context context, String identity) {
        if (!TextUtils.isEmpty(identity)) {
            Smartech.getInstance(new WeakReference<>(context)).login(identity); // NOTE: In addition to calling this method after successful login and successful sign-up, call this method when the app finds user is already logged in navigates user to home screen. Check the usage of this method for example.
        } else {
            Log.e(TAG, "User identity is null or empty.");
        }
    }

    /**
     * This method will clear user's identity from Smartech SDK locally and logout user from Smartech.
     *
     * @param clearIdentity - Whether to clear user's identity.
     **/
    public static void logout(Context context, boolean clearIdentity) {
        Smartech.getInstance(new WeakReference<>(context)).logoutAndClearUserIdentity(clearIdentity);
    }

    /**
     * This method will return the user's identity stored locally in the Smartech SDK.
     **/
    public static String getUserIdentity(Context context) {
        return Smartech.getInstance(new WeakReference<>(context)).getUserIdentity();
    }

    /**
     * This method will clear the user's identity stored locally in the Smartech SDK.
     **/
    public static void clearUserIdentity(Context context) {
        Smartech.getInstance(new WeakReference<>(context)).clearUserIdentity();
    }

    /**
     * This method will update the user's profile data.
     *
     * @param profilePayload - Profile data of the user to be updated.
     **/
    public static void updateUserProfile(Context context, HashMap<String, Object> profilePayload) {
        if (profilePayload != null && profilePayload.size() > 0) {
            Smartech.getInstance(new WeakReference<>(context)).updateUserProfile(profilePayload);
        } else {
            Log.e(TAG, "Profile payload is not valid.");
        }
    }

    /**
     * This method will set the FCM token to the Smartech SDK.
     *
     * @param token - Firebase token.
     **/
    public static void setDevicePushToken(Context context, String token) {
        if (!TextUtils.isEmpty(token)) {
            SmartPush.getInstance(new WeakReference<>(context)).setDevicePushToken(token);
        } else {
            Log.e(TAG, "Token is null or empty.");
        }
    }

    /**
     * This method will return the FCM token saved locally in the Smartech SDK.
     **/
    public static String getDevicePushToken(Context context) {
        return SmartPush.getInstance(new WeakReference<>(context)).getDevicePushToken();
    }

    /**
     * This method will render and track the notification received from Smartech. This method will return true if the notification is from Smartech else will return false.
     *
     * @param payload - Payload of the notification.
     **/
    public static boolean handlePushNotification(Context context, String payload) {
        return SmartPush.getInstance(new WeakReference<>(context)).handlePushNotification(payload);
    }

    /**
     * This method will call track event for specified event name.
     * NOTE: You will be using smartech.trackEvent() or HanselTracker.logEvent() method according to your contract with Netcore.
     * smartech.trackEvent()    - Events will be sent to Smartech server for analytics.
     * HanselTracker.logEvent() - Events will be logged locally in the device.
     *
     * @param eventName - Name of the event to be tracked.
     **/
    public static void trackEvent(Context context, String eventName) {
        if (!TextUtils.isEmpty(eventName)) {
            Smartech.getInstance(new WeakReference<>(context)).trackEvent(eventName, new HashMap<>());
        } else {
            Log.e(TAG, "Event name is not valid.");
        }
    }

    /**
     * This method will call track event for specified event name with specified payload.
     * NOTE: You will be using smartech.trackEvent() or HanselTracker.logEvent() method according to your contract with Netcore.
     * smartech.trackEvent()    - Events will be sent to Smartech server for analytics.
     * HanselTracker.logEvent() - Events will be logged locally in the device.
     *
     * @param eventName - Name of the event to be tracked.
     * @param payload   - Payload data for the event.
     **/
    public static void trackEvent(Context context, String eventName, HashMap<String, Object> payload) {
        if (!TextUtils.isEmpty(eventName) && payload != null && payload.size() > 0) {
            Smartech.getInstance(new WeakReference<>(context)).trackEvent(eventName, payload);
        } else {
            Log.e(TAG, "Event name or payload is not valid.");
        }
    }

    /**
     * This method will set InAppHTMLListener for custom HTML in-app callbacks to Smartech SDK.
     *
     * @param listener - Custom HTML in-app callback listener.
     **/
    public static void setInAppCustomHTMLListener(Context context, InAppCustomHTMLListener listener) {
        Smartech.getInstance(new WeakReference<>(context)).setInAppCustomHTMLListener(listener);
    }

    /**
     * This method will return InAppHTMLListener set in Smartech SDK.
     **/
    public static InAppCustomHTMLListener getInAppCustomHTMLListener(Context context) {
        return Smartech.getInstance(new WeakReference<>(context)).getInAppCustomHTMLListener();
    }

    /**
     * This method will return whether user has opted for push notification. Returns true if opted-in.
     **/
    public static boolean hasOptedPushNotification(Context context) {
        return SmartPush.getInstance(new WeakReference<>(context)).hasOptedPushNotification();
    }

    /**
     * This method will opt in/out the push notifications.
     *
     * @param opt - true for opt-in, false for opt-out.
     **/
    public static void optPushNotification(Context context, boolean opt) {
        SmartPush.getInstance(new WeakReference<>(context)).optPushNotification(opt);
    }

    /**
     * This method will return whether user has opted for in-app messages. Returns true if opted-in.
     **/
    public static boolean hasOptedInAppMessage(Context context) {
        return Smartech.getInstance(new WeakReference<>(context)).hasOptedInAppMessage();
    }

    /**
     * This method will opt in/out the in-app messages.
     *
     * @param opt - true for opt-in, false for opt-out.
     **/
    public static void optInAppMessage(Context context, boolean opt) {
        Smartech.getInstance(new WeakReference<>(context)).optInAppMessage(opt);
    }

    /**
     * This method will return whether user has opted for event tracking. Returns true if opted-in.
     **/
    public static boolean hasOptedTracking(Context context) {
        return Smartech.getInstance(new WeakReference<>(context)).hasOptedTracking();
    }

    /**
     * This method will opt in/out the event tracking.
     *
     * @param opt - true for opt-in, false for opt-out.
     **/
    public static void optTracking(Context context, boolean opt) {
        Smartech.getInstance(new WeakReference<>(context)).optTracking(opt);
    }

    /**
     * This method will set user's location to Smartech SDK.
     *
     * @param location - Location object with latitude and longitude of the user's location.
     **/
    public static void setUserLocation(Context context, Location location) {
        Smartech.getInstance(new WeakReference<>(context)).setUserLocation(location);
    }

    /**
     * This method will return the Smartech device unique Id/GUID.
     **/
    public static String getDeviceUniqueId(Context context) {
        return Smartech.getInstance(new WeakReference<>(context)).getDeviceUniqueId();
    }

    /**
     * This method will register the broadcast receiver for foreground push notification and deep link clicks.
     **/
    public static void registerSmartechDeepLinkHandler(Context context) {
        SmartechDeepLinkHandler smtDeepLinkHandler = new SmartechDeepLinkHandler();
        IntentFilter filter = new IntentFilter("com.smartech.EVENT_PN_INBOX_CLICK");
        context.registerReceiver(smtDeepLinkHandler, filter);
    }

    /**
     * This method will handle the deep link and custom payload.
     **/
    public static void handleDeepLinkAndCustomPayload(Context context, Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String customPayload = null;
                if (extras.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD)) {
                    customPayload = extras.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD);
                    Log.i(TAG, "Custom Payload: " + customPayload);
                }

                if (extras.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK)) {
                    String deepLink = extras.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK);
                    Log.i(TAG, "Deeplink: " + deepLink);

                    if (!TextUtils.isEmpty(deepLink)) {
                        if (deepLink.startsWith("sampleapp://profile")) {
                            openDeeplink(context, ProfileActivity.class, customPayload);
                        } else if (deepLink.startsWith("sampleapp://login")) {
                            openDeeplink(context, LoginActivity.class, customPayload);
                        } else if (deepLink.startsWith("sampleapp://register")) {
                            openDeeplink(context, RegisterActivity.class, customPayload);
                        } else if (URLUtil.isValidUrl(deepLink)) {
                            // If any of the deeplinks do not match, redirect the user to the browser if the deeplink is a valid URL.
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
                            browserIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(browserIntent);
                        }
                    }
                }
            }
        }
    }

    public static void openDeeplink(Context context, Class<? extends AppCompatActivity> activityClass, String customPayload) {
        Intent deeplinkIntent = new Intent(context, activityClass);
        deeplinkIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (customPayload != null)
            deeplinkIntent.putExtra(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD, customPayload);
        context.startActivity(deeplinkIntent);
    }

    /**
     * This will fetch the FCM token and set to SDK if token present in SDK is not valid.
     **/
    private static void setFCMToken(Context context) {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !TextUtils.isEmpty(task.getResult())) {
                            String fcmToken = task.getResult();
                            String currentToken = SmartechHelper.getDevicePushToken(context);

                            Log.i("TOKEN", "FCM Instance Id Token: " + fcmToken);
                            Log.i("TOKEN", "Current FCM Token: " + currentToken);

                            if (TextUtils.isEmpty(currentToken)) {
                                SmartechHelper.setDevicePushToken(context, fcmToken);
                                Log.i("TOKEN", "New token set: " + fcmToken);
                            } else if (!currentToken.equals(fcmToken)) {
                                SmartechHelper.setDevicePushToken(context, fcmToken);
                                Log.i("TOKEN", "New token set: " + fcmToken);
                            } else {
                                Log.i("TOKEN", "Both tokens are same.");
                            }
                        } else {
                            Log.e("TOKEN", "Fetch FCM token failed: Task unsuccessful.");
                        }
                    });
        } catch (Exception e) {
            Log.e("TOKEN", "Fetch FCM token failed: " + e.getMessage());
        }
    }
}