package com.leo.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Date:2020/7/23.4:11 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:
 * 为了让  APT 自动生成的类简易而进行包装
 * </p>
 */
public class EventIndexInfoWrap {
    final Class<?> target;
    final EventIndexInfo[] events;

    public EventIndexInfoWrap(Class<?> target, EventIndexInfo[] events) {
        this.target = target;
        this.events = events;
    }

    public Class<?> getTarget() {
        return target;
    }

    public List<EventMethodInfo> getMethodList(){

        List<EventMethodInfo> result = new ArrayList<>(events.length);
        for (EventIndexInfo eventMethodInfo : events) {
            EventMethodInfo handler = eventMethodInfo.generateMethodInfo(target);
            result.add(handler);
        }
        return result;
    }

}
