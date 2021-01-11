package com.mars.activityhook1;

import android.content.pm.PackageInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan.marx on 2020/10/24
 */
public class MockClass3 implements InvocationHandler {

    private Object target;

    public MockClass3(Object base) {
        target = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getPackageInfo")) {
            return new PackageInfo();
        }
        return method.invoke(target, args);
    }
}
