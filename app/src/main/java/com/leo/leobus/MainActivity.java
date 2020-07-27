package com.leo.leobus;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.leo.annotation.Receive;
import com.leo.annotation.ThreadMode;
import com.leo.bus.AutoEventBusIndex;
import com.leo.bus.HandBus;
import com.leo.bus.HandLogger;
import com.leo.leobus.event.IntEvent;
import com.leo.leobus.event.JsonEvent;
import com.leo.leobus.event.OtherEvent;
import com.leo.leobus.event.StringEvent;


public class MainActivity extends BaseActivity {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandLogger.logDebug(HandBus.class.toString());
        HandBus.getInstance().installIndex(new AutoEventBusIndex());
        HandBus.getInstance().register(this);


        findViewById(R.id.send).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,EventActivity1.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandBus.getInstance().unregister(this);
    }

    @Receive(threadMode = ThreadMode.THREAD_BACKGROUND)
    public void doStringEvent(StringEvent msg) {
        log(this.toString() +"--" + msg +"\n休眠2秒 in " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log(this.toString() +"--" + msg +"\n休眠结束");

    }
    @Receive(threadMode = ThreadMode.THREAD_MAIN)
    public  void doIntTwoArgEvent(IntEvent msg) {
        log(this.toString() +"--" + msg);
    }

    @Receive
    public void doJsonEvent(JsonEvent msg) {
        log(this.toString() +"--" + msg);
    }
    @Receive(threadMode = ThreadMode.THREAD_BACKGROUND)
    public void doOtherEvent(OtherEvent msg) {
        log(this.toString() +"--" + msg);
    }
}