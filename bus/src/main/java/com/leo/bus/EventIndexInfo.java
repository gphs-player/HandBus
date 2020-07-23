package com.leo.bus;

import java.lang.reflect.Method;

/**
 * <p>Date:2020/7/23.3:39 PM</p>
 * <p>Author:leo</p>
 * <p>Desc: APT生成的Event类要包含的信息
 * </p>
 */
public class EventIndexInfo {
    final Class<?> eventType;
    final String methodName;
    final int threadMode;

    public EventIndexInfo(Class<?> eventType,  String methodName, int threadMode) {
        this.eventType = eventType;
        this.methodName = methodName;
        this.threadMode = threadMode;
    }

    public EventMethodInfo generateMethodInfo(Class<?> target) {
        try {
            Method method = target.getDeclaredMethod(methodName, eventType);
            int threadMode = this.threadMode;
            Class<?> eventType = this.eventType;
            return new EventMethodInfo(method,threadMode,eventType);
        } catch (NoSuchMethodException e) {
            throw new BusException("createMethodInfo method == null");
        }
    }
}
