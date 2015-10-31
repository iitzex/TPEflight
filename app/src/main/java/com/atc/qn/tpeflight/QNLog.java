package com.atc.qn.tpeflight;

import android.util.Log;

public class QNLog{
    static final private boolean DEBUG = true;
//    static boolean DEBUG = false;
    static final private String TAG = "TPEFLIGHT";

    static public void d(String msg) {
        if(DEBUG)
            Log.d(TAG, msg);
    }
    static public void d(int msg) {
        if(DEBUG)
            Log.d(TAG, String.valueOf(msg));
    }
}
