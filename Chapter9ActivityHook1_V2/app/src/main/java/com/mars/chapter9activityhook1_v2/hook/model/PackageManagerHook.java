package com.mars.chapter9activityhook1_v2.hook.model;

import android.content.pm.PackageInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2021/1/17
 */
public class PackageManagerHook implements InvocationHandler {

    private Object target;

    public PackageManagerHook(Object obj) {
        target = obj;
    }

    /**
     * LoadedApk#initializeJavaContextClassLoader方法内部，IPackageManager实例调用getPackageInfo方法，如果返回接口为null，则会报
     * "Unable to get package info for " + mPackageName + "; is package not installed?"
     *
     * 详见LoadedApk#initializeJavaContextClassLoader方法
     * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/LoadedApk.java
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            return new PackageInfo();
        }
        return method.invoke(target, args);
    }
}
