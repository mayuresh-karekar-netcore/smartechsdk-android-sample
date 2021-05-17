package com.netcore.smartech.sample.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

public class Utility {
    public static void clearInputs(EditText[] inputs) {
        for (EditText input : inputs) {
            input.setText("");
        }
    }

    public static boolean areInputsEmpty(EditText[] inputs) {
        for (EditText input : inputs) {
            if (TextUtils.isEmpty(input.getText().toString()))
                return true;
        }
        return false;
    }

    public static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}