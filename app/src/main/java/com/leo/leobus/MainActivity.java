package com.leo.leobus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leo.bus.HandBus;
import com.leo.bus.HandLogger;
import com.leo.bus.Receive;
import com.leo.bus.ThreadMode;
import com.leo.leobus.event.IntEvent;
import com.leo.leobus.event.JsonEvent;
import com.leo.leobus.event.OtherEvent;
import com.leo.leobus.event.StringEvent;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandLogger.logDebug(HandBus.class.toString());
        HandBus.getInstance().register(this);
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