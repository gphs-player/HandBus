package com.leo.bus;

/**
 * <p>Date:2020/7/20.4:14 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class BusException extends RuntimeException {

    public BusException() {
    }

    public BusException(String message) {
        super(message);
    }
}
