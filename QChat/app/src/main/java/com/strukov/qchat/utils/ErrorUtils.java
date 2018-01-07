package com.strukov.qchat.utils;

import android.util.Log;

import com.google.android.gms.common.api.ApiException;

/**
 * Created by Matthew on 15.12.2017.
 */

public class ErrorUtils {
    private static String TAG_IN = "SIGN-IN";
    private static String TAG_OUT = "SIGN-OUT";
    public static void signInError(Throwable t) {
        Log.w(TAG_IN, "signInResult:failed code=" + ((ApiException)t).getStatusCode());
    }

    public static void signOutError(Throwable t) {
        Log.w(TAG_OUT, "signOutResult:failed code=" + ((ApiException)t).getStatusCode());
    }
}
