package com.leo.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Date:2020/7/17.3:15 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class HandBus {

    static {
        HandLogger.logWarn("init HandBus");
    }


    //外部类加载的时候不会加载内部类
    private static class Holder {
        static {
            HandLogger.logWarn("init HandBus.Holder");
        }

        static HandBus INSTANCE = new HandBus();
    }

    private HandBus() {
    }

    public static HandBus getInstance() {
        HandLogger.logDebug("getInstance : " + Holder.INSTANCE.toString());
        return Holder.INSTANCE;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 容器
    ///////////////////////////////////////////////////////////////////////////

    //所有接受者
    /**
     * KEY- class的全路径名称的hashCode
     */
    private Map<Integer, Object> mReceivers = new HashMap<>();

    /**
     *
     *  事件-接受者  映射关系
     *  根据事件类型去找接受者，不必遍历
     */
    //TODO 区分
    private Map<String, Map<Integer, Object>> mEventMappings = new HashMap<>();


    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param receiver 添加事件接收者
     */
    public void register(Object receiver) {
        if (receiver == null) return;
        log("register receiver" + receiver.getClass().getCanonicalName());
        int key = receiver.getClass().getName().hashCode();
        if (!mReceivers.containsKey(key)) {
            mReceivers.put(key, receiver);
        }
        log(String.valueOf(mReceivers.size()));
    }

    /**
     * @param receiver 移除事件接受者
     */
    public void unregister(Object receiver) {
        if (receiver == null) return;
        log("unregister receiver" + receiver.getClass().getCanonicalName());
        int key = receiver.getClass().getName().hashCode();
        mReceivers.remove(key);
        log(String.valueOf(mReceivers.size()));
    }


    public void post(Object event) {
        Collection<Object> receivers = mReceivers.values();
        for (Object receiver : receivers) {
            Method[] methods = receiver.getClass().getDeclaredMethods();
            //遍历接受者的所有方法
            for (Method method : methods) {
                //方法确定是public的
                if (method.getAnnotation(Receive.class) != null) {
                    log("find method " + method.getName()
                            + " for receiver " + receiver.getClass().getCanonicalName());
                    Receive annotation = method.getAnnotation(Receive.class);
                    //方法必须public修饰
                    if ((method.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC) {
                        throw new IllegalStateException("Receive修饰的方法必须为public");
                    }
                    //方法参数只能是1个
                    if (method.getParameterTypes().length == 1) {
                        Class<?> parameterType = method.getParameterTypes()[0];
                        if (parameterType == event.getClass()) {
                            try {
                                method.invoke(receiver, event);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } else {
                            log(method.getName() + " \t-Parameter Type didn't matches ,ignored");
                        }
                    } else {
                        throw new IllegalStateException("Receive修饰的方法参数必须为1");
                    }
                } else {
                    log("method  " + method.getName() + " in " + receiver.getClass().getCanonicalName() + " ignored");
                }
            }
        }
    }

    private void log(String msg) {
        HandLogger.logDebug(msg);
    }

    private void logE(String msg) {
        HandLogger.logError(msg);
    }

    private void logW(String msg) {
        HandLogger.logWarn(msg);
    }
}
