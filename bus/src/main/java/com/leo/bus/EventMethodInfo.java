package com.leo.bus;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p>Date:2020/7/17.6:16 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:事件消费者和Method 一对一关系
 * </p>
 */
public class EventMethodInfo {
    //消息接收者
    final Method method;
    //线程标识
    final int threadMode;
    final Class<?> eventType;

    public EventMethodInfo(Method method, int threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }



    @Override
    public int hashCode() {
        return method.hashCode();
    }


    @Override
    public String toString() {
        return "EventMethodInfo{" +
                "method=" + method.getName() +
                ", threadMode=" + threadMode +
                ", eventType=" + eventType +
                '}';
    }
}
