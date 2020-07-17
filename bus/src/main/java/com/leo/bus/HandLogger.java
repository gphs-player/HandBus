package com.leo.bus;

import android.util.Log;

/**
 * <p>Date:2020/7/17.3:20 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class HandLogger {
    private static final String TAG = "HandBus";
    private static final int LEVEL_DEBUG = 1;
    private static final int LEVEL_ERROR = 2;
    private static final int LEVEL_WARN = 3;

    public static void logDebug(String msg){
        logMsg(LEVEL_DEBUG,msg);
    }
    public static void logError(String msg){
        logMsg(LEVEL_ERROR,msg);
    }
    public static void logWarn(String msg){
        logMsg(LEVEL_WARN,msg);
    }
    private static void logMsg(int level,String msg){
        switch (level){
            case LEVEL_DEBUG:
                Log.d(TAG,msg);
                break;
            case LEVEL_WARN:
                Log.w(TAG,msg);
                break;
            case LEVEL_ERROR:
                Log.e(TAG,msg);
                break;
            default:
                Log.wtf(TAG,msg);
                break;
        }
    }
}
