package com.mars.chapter5hook12.hook;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2020/12/19
 */
public class MockClass1 implements InvocationHandler {

    Object target;

    public MockClass1(Object base) {
        target = base;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Log.e("gy", "MockClass1---invoke method = " + method.getName());
        }
        return method.invoke(target, args);
    }
}
