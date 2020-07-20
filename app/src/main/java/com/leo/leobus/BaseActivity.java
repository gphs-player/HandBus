package com.leo.leobus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.leo.bus.HandLogger;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void log(String msg){
        HandLogger.logDebug(msg);
    }
    protected void logError(String msg){
        HandLogger.logError(msg);
    }
}