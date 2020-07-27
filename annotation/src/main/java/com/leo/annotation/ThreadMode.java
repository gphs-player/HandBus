package com.leo.annotation;

/**
 * <p>Date:2020/7/20.1:58 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:消息执行线程
 * </p>
 */
public interface ThreadMode {
    //发消息线程
    int THREAD_DEFAULT = 0;
    //主线程
    int THREAD_MAIN = 1;
    //子线程
    int THREAD_BACKGROUND = 3;
}
