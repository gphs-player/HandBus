package com.leo.bus;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p>Date:2020/7/17.6:16 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:事件消费者和Method 一对一关系
 * </p>
 */
class EventHandler {
    //消息接收者
    Object target;
    Method method;
    //线程标识
    int threadMode;
    Class<?> eventType;

    public EventHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }


    @Override
    public int hashCode() {
        return method.hashCode();
    }

    private int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "EventHandler{" +
                "target=" + target.getClass().getName() +
                ", method=" + method.getName() +
                '}';
    }
}
