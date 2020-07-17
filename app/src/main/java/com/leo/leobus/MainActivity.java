package com.leo.leobus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leo.bus.HandBus;
import com.leo.bus.HandLogger;
import com.leo.bus.Receive;
import com.leo.leobus.event.IntEvent;
import com.leo.leobus.event.JsonEvent;
import com.leo.leobus.event.OtherEvent;
import com.leo.leobus.event.StringEvent;

public class MainActivity extends AppCompatActivity {

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
//                HandBus.getInstance().post(" msg from bus ");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandBus.getInstance().unregister(this);
    }

    @Receive
    public void showSomething(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Receive
    public void doStringEvent(StringEvent msg) {
        Toast.makeText(this, "StringEvent", Toast.LENGTH_SHORT).show();
    }
    @Receive
    public void doIntEvent(IntEvent msg) {
        Toast.makeText(this, "IntEvent", Toast.LENGTH_SHORT).show();
    }
    @Receive
    public void doJsonEvent(JsonEvent msg) {
        Toast.makeText(this, "JsonEvent", Toast.LENGTH_SHORT).show();
    }
    @Receive
    public void doOtherEvent(OtherEvent msg) {
        Toast.makeText(this, "OtherEvent", Toast.LENGTH_SHORT).show();
    }
}