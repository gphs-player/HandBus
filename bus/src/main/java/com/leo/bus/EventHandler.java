package com.leo.bus;

/**
 * <p>Date:2020/7/23.3:07 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class EventHandler {
    Object receiver;
    EventMethodInfo methodInfo;

    public EventHandler(Object receiver, EventMethodInfo methodInfo) {
        this.receiver = receiver;
        this.methodInfo = methodInfo;
    }

    @Override
    public int hashCode() {
        return methodInfo.hashCode();
    }


    @Override
    public String toString() {
        return "EventHandler{" +
                "target=" + receiver.getClass().getName() +
                ", methodInfo=" + methodInfo.toString() +
                '}';
    }
}
