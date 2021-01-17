package com.mars.chapter9activityhook1_v2.hook;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.mars.chapter9activityhook1_v2.hook.model.ActivityManagerHook;
import com.mars.chapter9activityhook1_v2.hook.model.ActivityThreadHook;
import com.mars.components.util.reflect.FieldUtils;

import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2021/1/16
 */
public class AMSHookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";
    private static final String SINGLETON_NAME = "android.util.Singleton";
    private static final String ACTIVITY_MANAGER_NATIVE_NAME = "android.app.ActivityManagerNative";
    private static final String IACTIVITY_MANAGER_NAME = "android.app.IActivityManager";
    private static final String ACTIVITY_THREAD_NAME = "android.app.ActivityThread";

    /**
     * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/ActivityManagerNative.java
     * hook ActivityManagerNative的gDefault的mInstance字段，使用动态代理的方式，由ActivityManagerHook实例对象替代
     */
    public static void hookActivityManagerNative() throws ClassNotFoundException {

        // 获取AMN的gDefault单例
        Object gDefault = FieldUtils.readStaticField(ACTIVITY_MANAGER_NATIVE_NAME, "gDefault");
        Object instance = FieldUtils.readField(SINGLETON_NAME, gDefault, "mInstance");
        @SuppressLint("PrivateApi")
        Class<?> cls = Class.forName(IACTIVITY_MANAGER_NAME);
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, new ActivityManagerHook(instance));
        FieldUtils.writeField(SINGLETON_NAME, gDefault, "mInstance", proxy);
    }

    /**
     * 由于之前用StubActivity欺骗了AMS，现在需要还原回真正启动的插件Activity，
     * 最终会交给ActivityThread的内部类H来启动Activity，H会完成消息转发，最终调用其callback
     */
    public static void hookActivityThread() {
        Object sCurrentActivityThread = FieldUtils.readStaticField(ACTIVITY_THREAD_NAME, "sCurrentActivityThread");
        // 一个进程只有一个ActivityThread，获取该对象的mH
        Handler mH = (Handler) FieldUtils.readField(sCurrentActivityThread, "mH");
        // 只要接口才能使用动态代理，这里直接构建对象就可以
        FieldUtils.writeField(Handler.class, mH, "mCallback", new ActivityThreadHook(mH));
    }
}
