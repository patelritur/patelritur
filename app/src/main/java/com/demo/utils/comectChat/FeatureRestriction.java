package com.demo.utils.comectChat;

import com.google.android.gms.tasks.OnSuccessListener;

public class FeatureRestriction {
    public static void isCallsSoundEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(true);
    }

    public interface OnSuccessListener {
        void onSuccess(Boolean booleanVal);
    }
}