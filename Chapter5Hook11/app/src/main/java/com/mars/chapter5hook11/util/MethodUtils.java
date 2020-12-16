package com.mars.chapter5hook11.util;

import java.lang.reflect.Method;

/**
 * Created by geyan on 2020/12/17
 */
public class MethodUtils {

    public static Object invokeMethod(Object target, String methodName, Class[] parameterTypes, Object[] args) {
        if (target == null)
            return null;

        try {
            //调用一个private方法
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes); //在指定类中获取指定的方法
            method.setAccessible(true);
            return method.invoke(target, args);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
