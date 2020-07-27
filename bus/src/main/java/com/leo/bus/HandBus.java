package com.leo.bus;

import android.os.Looper;

import com.leo.annotation.Receive;
import com.leo.annotation.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Date:2020/7/17.3:15 PM</p>
 * <p>Author:leo</p>
 * <p>Desc:</p>
 */
public class HandBus {

    private static HandBus mInstance;

    private static class Holder {
        static HandBus INSTANCE = new HandBus();
    }

    private HandBus() {
        mMainThreadHandler = new MainThreadHandler(this);
        mBackgroundHandler = new BackGroundHandler(this);
    }

    public static HandBus getInstance() {
        if (mInstance == null) {
            mInstance = Holder.INSTANCE;
        }
        return mInstance;
    }

    public void installIndex(IndexFinder index) {
        this.mIndexContainer = index;
        mHasEventIndex = mIndexContainer != null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // var
    ///////////////////////////////////////////////////////////////////////////

    private IndexFinder mIndexContainer;
    //编译期已经注册了Event映射Map，不需要再次反射匹配
    private boolean mHasEventIndex;
    /**
     * 根据事件类型去找接收者，不必遍历
     * Event - <Activity,Method>
     */
    private Map<String, List<EventHandler>> mEventMappings = new HashMap<>();

    private Process mMainThreadHandler;
    private Process mBackgroundHandler;

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param receiver 添加事件接收者
     */
    public void register(Object receiver) {
        if (receiver == null) return;
        List<EventMethodInfo> eventMethods = findEventMethod(receiver);
        addReceiver(receiver, eventMethods);
    }

    private void addReceiver(Object receiver, Collection<EventMethodInfo> eventMethods) {
        for (EventMethodInfo eventMethod : eventMethods) {
            //以事件类型维护映射关系
            Class<?> eventType = eventMethod.eventType;
            //已经有接受者，则继续添加新的接收者
            EventHandler eventHandler = new EventHandler(receiver, eventMethod);
            if (mEventMappings.containsKey(eventType.toString())) {
                List<EventHandler> receivers = mEventMappings.get(eventType.toString());
                // 可能尚未注册过 或者已经清空
                if (receivers == null || receivers.size() == 0) {
                    receivers = new ArrayList<>();
                    mEventMappings.put(eventType.toString(), receivers);
                    receivers.add(eventHandler);
                } else {
                    //已经注册，忘记反注册, 报错提醒
                    if (receivers.contains(eventMethod)) {
                        throw new IllegalStateException(receiver.getClass().getName() + " already  register in HandBus");
                    } else {
                        receivers.add(eventHandler);
                    }
                }
            } else {
                //没有接收者,新添加
                List<EventHandler> receivers = new ArrayList<>();
                receivers.add(eventHandler);
                mEventMappings.put(eventType.toString(), receivers);
            }
        }
        log("Register Events:\n" + mEventMappings.toString());

    }

    private List<EventMethodInfo> findEventMethod(Object target) {
        List<EventMethodInfo> result;
        Class<?> receiver = target.getClass();
        if (mHasEventIndex) {
            result = mIndexContainer.find(receiver);
        } else {
            result = usingReflectFind(receiver);
        }
        result = result == null ? new ArrayList<>() : result;
        return result;
    }

    private List<EventMethodInfo> usingReflectFind(Class<?> receiver) {
        List<EventMethodInfo> result = new ArrayList<>();
        Method[] methods = receiver.getDeclaredMethods();
        //遍历接受者的所有方法
        for (Method method : methods) {
            //方法确定是public的
            if (method.getAnnotation(Receive.class) != null) {
                log("find method " + method.getName()
                        + " for receiver " + receiver.getCanonicalName());
                //方法必须public修饰
                if ((method.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC) {
                    throw new BusException("the @Receive method [" + method.getName() + "] must be public, non-static, and non-abstract");
                }
                //方法参数只能是1个
                if (method.getParameterTypes().length == 1) {
                    Receive annotation = method.getAnnotation(Receive.class);
                    Method methodRegister = method;
                    int threadMode = Objects.requireNonNull(annotation).threadMode();
                    Class<?> eventType = method.getParameterTypes()[0];
                    EventMethodInfo handler = new EventMethodInfo(methodRegister, threadMode, eventType);
                    result.add(handler);
                } else {
                    throw new BusException("the @Receive method [" + method.getName() + "] must have exactly 1 parameter ");
                }
            } else {
                log("method  " + method.getName() + " in " + receiver.getCanonicalName() + " ignored");
            }
        }
        return result;
    }

    /**
     * @param receiver 移除事件接受者
     */
    public void unregister(Object receiver) {
        if (receiver == null) return;

        for (String eventKey : mEventMappings.keySet()) {
            List<EventHandler> eventHandlers = mEventMappings.get(eventKey);
            if (eventHandlers == null || eventHandlers.size() == 0) continue;
            int receiverSize = eventHandlers.size();
            for (int i = 0; i < receiverSize; i++) {
                EventHandler eventHandler = eventHandlers.get(i);
                if (eventHandler.receiver.getClass().toString().equals(receiver.getClass().toString())) {
                    eventHandlers.remove(eventHandler);
                    i--;
                    receiverSize--;
                }
            }
        }
    }


    public void post(final Object event) {
        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
        String eventKey = event.getClass().toString();
        List<EventHandler> receiverMaps = mEventMappings.get(eventKey);
        if (receiverMaps == null || receiverMaps.size() == 0) return;
        //所有可以处理该事件的接收者
        for (final EventHandler receiver : receiverMaps) {
            switch (receiver.methodInfo.threadMode) {
                default:
                case ThreadMode.THREAD_DEFAULT:
                    runMethodDirect(receiver, event);
                    break;
                case ThreadMode.THREAD_MAIN:
                    if (isMainThread) {
                        runMethodDirect(receiver, event);
                    } else {
                        log("切换到主线程");
                        mMainThreadHandler.process(receiver, event);
                    }
                    break;
                case ThreadMode.THREAD_BACKGROUND:
                    if (isMainThread) {
                        log("切换到子线程");
                        mBackgroundHandler.process(receiver, event);
                    } else {
                        runMethodDirect(receiver, event);
                    }
                    break;
            }
        }
    }

    void runMethodDirect(EventHandler target, Object event) {
        try {
            target.methodInfo.method.invoke(target.receiver, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        HandLogger.logDebug(msg);
    }
}
