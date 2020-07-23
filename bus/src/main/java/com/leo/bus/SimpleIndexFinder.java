package com.leo.bus;

import java.util.List;

/**
 * <p>Date:2020/7/23.4:00 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class SimpleIndexFinder implements IndexFinder {
    List<EventMethodInfo> methods;

    public SimpleIndexFinder(List<EventMethodInfo> methods) {
        this.methods = methods;
    }

    @Override
    public List<EventMethodInfo> find(Class<?> event) {
        return null;
    }
}
