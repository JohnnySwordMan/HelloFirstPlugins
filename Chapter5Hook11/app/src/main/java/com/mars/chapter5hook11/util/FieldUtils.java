package com.mars.chapter5hook11.util;

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

    public static void setFieldObject(Class clazz, Object target, String filedName, Object filedVaule) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(target, filedVaule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
