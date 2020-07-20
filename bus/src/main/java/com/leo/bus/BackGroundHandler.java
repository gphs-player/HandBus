package com.leo.bus;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Date:2020/7/20.4:46 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:子线程处理事件
 * </p>
 */
public class BackGroundHandler implements Runnable,Process{

    private final HandBus mBus;

    public BackGroundHandler(HandBus bus) {
        this.mBus = bus;
    }

    private  ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private LinkedList<PendingEvent> mQueue = new LinkedList<>();

    @Override
    public void process(EventHandler eventHandler, Object eventEntity) {
        PendingEvent pendingEvent = new PendingEvent();
        pendingEvent.event = eventEntity;
        pendingEvent.handler = eventHandler;
        mQueue.offer(pendingEvent);
        DEFAULT_EXECUTOR_SERVICE.submit(this);

    }

    @Override
    public void run() {
        PendingEvent poll = mQueue.poll();
        while (poll != null) {
            mBus.runMethodDirect(poll.handler, poll.event);
            poll = mQueue.poll();
        }
    }
}
