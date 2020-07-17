package com.leo.leobus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.leo.bus.HandBus;
import com.leo.leobus.event.IntEvent;
import com.leo.leobus.event.JsonEvent;
import com.leo.leobus.event.OtherEvent;
import com.leo.leobus.event.StringEvent;

public class EventActivity1 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event1);
        findViewById(R.id.sendString).setOnClickListener(this);
        findViewById(R.id.sendInt).setOnClickListener(this);
        findViewById(R.id.sendJson).setOnClickListener(this);
        findViewById(R.id.sendOther).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendString:
                HandBus.getInstance().post(new StringEvent());
                break;
            case R.id.sendInt:
                HandBus.getInstance().post(new IntEvent());
                break;
            case R.id.sendJson:
                HandBus.getInstance().post(new JsonEvent());
                break;
            case R.id.sendOther:
                HandBus.getInstance().post(new OtherEvent());
                break;
            default:
                break;
        }
    }
}