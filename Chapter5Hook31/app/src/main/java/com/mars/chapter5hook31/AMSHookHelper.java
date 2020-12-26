package com.mars.chapter5hook31;

import android.app.Instrumentation;
import android.os.Handler;

import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2020/12/26
 */
public final class AMSHookHelper {

    public final static String EXTRA_TARGET_INTENT = "extra_target_intent";

    /**
     * Hook上半场
     */
    public static void hookAMN() throws ClassNotFoundException {
        Object gDefault = FieldUtils.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault");
        Object mInstance = FieldUtils.getFieldObject("android.util.Singleton", gDefault, "mInstance");

        Class<?> cls = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, new MockClass1(mInstance));
        FieldUtils.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }

    /**
     * Hook下半场：对H类的mCallback字段进行Hook
     */
    public static void hookCallbackInH() {
        Object sCurrentActivityThread = FieldUtils.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        Handler mH = (Handler) FieldUtils.getFieldObject("android.app.ActivityThread", sCurrentActivityThread, "mH");
        FieldUtils.setFieldObject(Handler.class, mH, "mCallback", new MockClass2(mH));
    }

    /**
     * Hook下半场：对ActivityThread的mInstrumentation字段进行Hook，有两个方法签名可以拦截，一个是newActivity，一个是callActivityOnCreate
     */
    public static void hookActivityThread() {
        Object sCurrentActivityThread = FieldUtils.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        Instrumentation mInstrumentation = (Instrumentation) FieldUtils.getFieldObject(sCurrentActivityThread, "mInstrumentation");
        Instrumentation marsInstrumentation = new MarsInstrumentation(mInstrumentation);
        FieldUtils.setFieldObject(sCurrentActivityThread, "mInstrumentation", marsInstrumentation);
    }

}
