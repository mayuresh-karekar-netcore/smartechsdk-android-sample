package com.netcore.smartech.sample.service;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.netcore.smartech.sample.utils.Keys;
import com.netcore.smartech.sample.utils.SharedPreferenceHelper;
import com.netcore.smartech.sample.utils.SmartechHelper;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferenceHelper.putString(this, Keys.FCM_TOKEN, token);
        SmartechHelper.setDevicePushToken(this, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        boolean isFromSmartech = SmartechHelper.handlePushNotification(this, remoteMessage.getData().toString());
        if (!isFromSmartech) {
            // TODO Handle notifications from other sources.
        }
    }
}