package com.leo.bus;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.LinkedList;

/**
 * <p>Date:2020/7/20.4:08 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class MainThreadHandler extends Handler implements Process{

    private final HandBus mBus;
    private LinkedList<PendingEvent> mQueue = new LinkedList<>();
    public MainThreadHandler(HandBus bus) {
        super(Looper.getMainLooper());
        this.mBus = bus;
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        PendingEvent poll = mQueue.poll();
        while (poll != null) {
            mBus.runMethodDirect(poll.handler, poll.event);
            poll = mQueue.poll();
        }
    }

    @Override
    public void process(EventHandler eventHandler, Object eventEntity) {
        //发消息回调到主线程
        PendingEvent pendingEvent = new PendingEvent();
        pendingEvent.event = eventEntity;
        pendingEvent.handler = eventHandler;
        mQueue.offer(pendingEvent);
        if (!sendMessage(obtainMessage())){
            throw new BusException("主线程处理消息失败");
        }
    }
}
