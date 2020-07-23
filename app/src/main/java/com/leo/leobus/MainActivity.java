package com.leo.leobus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Printer;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.leo.bus.HandBus;
import com.leo.bus.HandLogger;
import com.leo.bus.Receive;
import com.leo.bus.ThreadMode;
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
        HandBus.getInstance().installIndex(new BusIndexFinder());
        HandBus.getInstance().register(this);
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EventActivity1.class));
            }
        });
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
    @Receive
    public void doIntEvent(IntEvent msg) {
        log(this.toString() +"--" + msg);
    }

    @Receive
    public void doJsonEvent(JsonEvent msg) {
        log(this.toString() +"--" + msg);
    }
    @Receive
    public void doOtherEvent(OtherEvent msg) {
        log(this.toString() +"--" + msg);
    }
}