package com.leo.bus;

import java.lang.reflect.Method;

/**
 * <p>Date:2020/7/17.6:16 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:事件消费者和Method 一对一关系
 * </p>
 */
class EventHandler {
    Object target;
    Method method;

    public EventHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public String toString() {
        return "EventHandler{" +
                "target=" + target.getClass().getName() +
                ", method=" + method.getName() +
                '}';
    }
}
