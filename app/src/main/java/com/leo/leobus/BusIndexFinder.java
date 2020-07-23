package com.leo.leobus;

import com.leo.bus.EventIndexInfoWrap;
import com.leo.bus.EventMethodInfo;
import com.leo.bus.IndexFinder;
import com.leo.bus.EventIndexInfo;
import com.leo.bus.ThreadMode;
import com.leo.leobus.event.StringEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Date:2020/7/23.2:22 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:
 * 应该由APT自动生成
 * </p>
 */
public class BusIndexFinder implements IndexFinder {

    private static Map<Class<?>, EventIndexInfoWrap> mIndexMap = new HashMap<>();

    static {
        putIndex(new EventIndexInfoWrap(MainActivity.class, new EventIndexInfo[]{
                new EventIndexInfo(StringEvent.class, "doStringEvent", ThreadMode.THREAD_BACKGROUND)
        }));
    }

    private static void putIndex(EventIndexInfoWrap infoWrap) {
        mIndexMap.put(infoWrap.getTarget(), infoWrap);
    }

    @Override
    public List<EventMethodInfo> find(Class<?> receiver) {
        EventIndexInfoWrap methodInfoWrap = mIndexMap.get(receiver);
        if (methodInfoWrap == null) return null;
        return methodInfoWrap.getMethodList();
    }

}
