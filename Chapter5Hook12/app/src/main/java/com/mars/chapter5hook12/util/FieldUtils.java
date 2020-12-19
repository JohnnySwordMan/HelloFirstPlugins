package com.mars.chapter5hook12.util;

import java.lang.reflect.Field;

/**
 * Created by geyan on 2020/12/17
 */
public class FieldUtils {

    public static Object getFieldObject(Class clazz, Object target, String filedName) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getFieldObject(String className, Object obj, String filedName) {
        try {
            Class cls = Class.forName(className);
            return getFieldObject(cls, obj, filedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldObject(Class clazz, Object target, String filedName, Object filedVaule) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(target, filedVaule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(String className, Object obj, String filedName, Object filedVaule) {
        try {
            Class cls = Class.forName(className);
            setFieldObject(cls, obj, filedName, filedVaule);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object getStaticFieldObject(String className, String filedName) {
        return getFieldObject(className, null, filedName);
    }

    public static Object getStaticFieldObject(Class clazz, String filedName) {
        return getFieldObject(clazz, null, filedName);
    }
}
