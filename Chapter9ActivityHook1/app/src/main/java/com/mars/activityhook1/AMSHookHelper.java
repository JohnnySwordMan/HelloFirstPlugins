package com.mars.activityhook1;

import android.os.Handler;

import com.mars.activityhook1.util.RefInvoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * Created by geyan.marx on 2020/10/24
 */
public class AMSHookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    /**
     * Hook AMS
     * 主要完成的操作：把真正要启动的Activity临时替换为在AndroidManifest.xml中声明的替身Activity，进而骗过AMS
     * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/ActivityThread.java
     */
    public static void hookAMN() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {

        // 获取AMN的gDefault单例，final静态的
        Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault");
        // gDefault是一个Singleton对象，取出这个单例里面的mInstance字段，这个字段其实是IActivityManagerNative的实现类
        Object mInstance = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance");
        // 创建一个这个对象的代理对象MockClass1，然后替换这个字段，让我们的代理对象帮忙干活
        Class<?> class2Interface = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{class2Interface}, new MockClass1(mInstance));
        // 把gDefault的mInstance字段，修改为proxy
        RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }

    /**
     * 由于之前我们用替身欺骗了AMS，现在我们要换回真正需要启动的Activity,
     * 不然真的启动替身了,
     * 到最终要启动Activity的时候，会交给ActivityThread的一个内部类叫做H来完成，
     * H会完成这个消息转发，最终调用它的callback
     */
    public static void hookActivityThread() throws Exception {
        // 先获取到当前ActivityThread对象
        Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        // 由于ActivityThread一个进程只有一个，我获取这个对象的mH
        Handler mH = (Handler) RefInvoke.getFieldObject(currentActivityThread, "mH");
        // 把Handler的mCallback字段，替换为new MockClass2(mH)
        RefInvoke.setFieldObject(Handler.class, mH, "mCallback", new MockClass2(mH));
    }
}
