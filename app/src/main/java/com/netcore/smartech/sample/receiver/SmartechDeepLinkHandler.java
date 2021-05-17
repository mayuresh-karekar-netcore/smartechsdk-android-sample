package com.netcore.smartech.sample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.netcore.smartech.sample.utils.SmartechHelper;

public class SmartechDeepLinkHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // NOTE: If PN is clicked in FOREGROUND mode the call for deeplink and custom payload will come here.
        SmartechHelper.handleDeepLinkAndCustomPayload(context, intent);
    }
}