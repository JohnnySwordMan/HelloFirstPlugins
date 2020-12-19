package com.mars.chapter5hook12.hook;

import com.mars.chapter5hook12.util.FieldUtils;

import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2020/12/19
 * Instrumentation.execStartActivity中执行ActivityManagerNative的startActivity方法
 */
public class AMSHookHelper {

    /**
     * hook ActivityManagerNative的gDefault，gDefault:Singleton<IActivityManager>
     */
    public static void hookAMN() throws ClassNotFoundException {

        Object gDefault = FieldUtils.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault");
        // IActivityManager接口实例
        Object mInstance = FieldUtils.getFieldObject("android.util.Singleton", gDefault, "mInstance");

        Class<?> cls = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, new MockClass1(mInstance));
        FieldUtils.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }
}
