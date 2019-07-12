package com.qyt.bm.utils;

import android.util.Log;

import com.qyt.bm.BuildConfig;

public class LogInfo {

    private static boolean loggable = BuildConfig.DEBUG;
    private final static String tag = BuildConfig.APPLICATION_ID;

    public static void error(Object msg) {
        if (loggable) {
            if (msg != null) {
                if (msg instanceof Exception) {
                    Log.e(tag, ((Exception) msg).getMessage());
                } else {
                    Log.e(tag, String.valueOf(msg));
                }
            } else {
                error("object null");
            }
        }
    }
}
