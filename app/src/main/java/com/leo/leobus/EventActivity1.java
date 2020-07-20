package com.leo.leobus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leo.bus.HandBus;
import com.leo.bus.Receive;
import com.leo.leobus.event.IntEvent;
import com.leo.leobus.event.JsonEvent;
import com.leo.leobus.event.OtherEvent;
import com.leo.leobus.event.StringEvent;

public class EventActivity1 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event1);
        HandBus.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandBus.getInstance().unregister(this);
    }

    public void jumpEvent(View view) {
        startActivity(new Intent(EventActivity1.this,EventActivity2.class));
    }
    public void jumpEventFinish(View view) {
        startActivity(new Intent(EventActivity1.this,EventActivity2.class));
        finish();
    }

    @Receive
    public void doEventString(StringEvent msg) {
        log(this.toString() +"--" + msg +"\n休眠5秒");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log(this.toString() +"--" + msg +"\n休眠结束");

    }
    @Receive
    public void doEventInt(IntEvent msg) {
        log(this.toString() +"--" + msg);
    }

    @Receive
    public void doEventJson(JsonEvent msg) {
        log(this.toString() +"--" + msg +"\n休眠3秒");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Receive
    public void doEventOther(OtherEvent msg) {
        log(this.toString() +"--" + msg);
    }


}