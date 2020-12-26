package com.mars.chapter5hook31;

import java.lang.reflect.Field;

/**
 * Created by geyan on 2020/12/17
 */
public class FieldUtils {

    public static Object getFieldObject(Object obj, String filedName) {
        return getFieldObject(obj.getClass(), obj, filedName);
    }

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

    public static Object getFieldObject(String className, Object target, String filedName) {
        try {
            Class cls = Class.forName(className);
            return getFieldObject(cls, target, filedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldObject(Object obj, String fieldName, Object fieldValue) {
        setFieldObject(obj.getClass(), obj, fieldName, fieldValue);
    }


    public static void setFieldObject(Class clazz, Object target, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(String className, Object obj, String filedName, Object filedValue) {
        try {
            Class cls = Class.forName(className);
            setFieldObject(cls, obj, filedName, filedValue);
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
