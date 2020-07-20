package com.leo.bus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventHandler that = (EventHandler) o;
        return equalsObj(target, that.target) &&
                equalsObj(method, that.method);
    }

    private boolean equalsObj(Object a, Object b) {
        return (a == b) || (a != null && a.getClass().toString().equals(b.getClass().toString()));
    }

    @Override
    public int hashCode() {
        return hash(target, method);
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
