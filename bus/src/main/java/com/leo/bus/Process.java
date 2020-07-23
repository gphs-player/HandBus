package com.leo.bus;

/**
 * <p>Date:2020/7/20.4:09 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public interface Process {
    void process(EventHandler eventHandler, Object eventEntity);
}
