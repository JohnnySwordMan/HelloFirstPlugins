package com.mars.zeusstudy1_2;

import android.os.Handler;

import com.mars.pluginservice.RefInvoke;

import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2020/11/23
 */
public class AMSHookHelper {

    public final static String EXTRA_TARGET_INTENT = "extra_target_intent";

    /**
     * hook AMS，将要启动的插件中的Activity换成StubActivity
     * 1. 获取AMN的gDefault单例，final Singleton类型的
     */
    public static void hookAMN() throws ClassNotFoundException {
        Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault");
        // 这个其实就是IActivityManager的实现类
        Object mInstance = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance");

        Class<?> aClass = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{aClass}, new MockClass1(mInstance));
        // gDefault的mInstance替换成proxy，因为mInstance返回的就是IActivityManager的实现类
        RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }

    /**
     * hook ActivityThread，将StubActivity还原成真正要启动的插件中的Activity
     *
     * 由于之前我们用StubActivity欺骗了AMS，现在需要换回真正要启动的Activity
     * 到最终要启动的Activity的时候，会交给ActivityThread的内部类H完成，H会完成消息转发，最终调用其callback
     */
    public static void hookActivityThread() {
        // 获取当前的ActivityThread
        Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        Handler mH = (Handler) RefInvoke.getFieldObject(currentActivityThread, "mH");
        RefInvoke.setFieldObject(Handler.class, mH, "mCallback", new MockClass2(mH));
    }

    /**
     * ActivityThread.java
     *
     * private static volatile ActivityThread sCurrentActivityThread;
     * final H mH = new H();
     *
     */

    /**
     * H.java
     * public void dispatchMessage(Message msg) {
     *         if (msg.callback != null) {
     *             handleCallback(msg);
     *         } else {
     *             if (mCallback != null) {
     *                 if (mCallback.handleMessage(msg)) {
     *                     return;
     *                 }
     *             }
     *             handleMessage(msg);
     *         }
     *     }
     */

    /**
     * private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
     *         protected IActivityManager create() {
     *             IBinder b = ServiceManager.getService("activity");
     *             if (false) {
     *                 Log.v("ActivityManager", "default service binder = " + b);
     *             }
     *             IActivityManager am = asInterface(b);
     *             if (false) {
     *                 Log.v("ActivityManager", "default service = " + am);
     *             }
     *             return am;
     *         }
     *     };
     */

    /**
     * public abstract class Singleton<T> {
     *     private T mInstance;
     *
     *     protected abstract T create();
     *
     *     public final T get() {
     *         synchronized (this) {
     *             if (mInstance == null) {
     *                 mInstance = create();
     *             }
     *             return mInstance;
     *         }
     *     }
     * }
     */
}
