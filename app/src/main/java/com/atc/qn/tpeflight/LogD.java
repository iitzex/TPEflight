package com.atc.qn.tpeflight;

import android.util.Log;

public class LogD {
    static boolean DEBUG = true;
    static String TAG = "TPEflight";

    static public void out(String msg) {
        if(DEBUG)
            Log.d(TAG, msg);
    }
}
