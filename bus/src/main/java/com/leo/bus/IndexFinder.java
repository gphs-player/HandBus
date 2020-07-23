package com.leo.bus;

import java.util.List;

/**
 * <p>Date:2020/7/23.2:30 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public interface IndexFinder {

    List<EventMethodInfo> find(Class<?> event);
}
