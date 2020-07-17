package com.leo.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Date:2020/7/17.3:49 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Receive {
}
