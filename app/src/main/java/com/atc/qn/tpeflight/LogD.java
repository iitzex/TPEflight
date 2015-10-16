package com.atc.qn.tpeflight;

import android.util.Log;

public class LogD {
//    static boolean DEBUG = true;
    static boolean DEBUG = false;
    static String TAG = "TPEflight";

    static public void out(String msg) {
        if(DEBUG)
            Log.d(TAG, msg);
    }
    static public void out(int msg) {
        if(DEBUG)
            Log.d(TAG, String.valueOf(msg));
    }
}
