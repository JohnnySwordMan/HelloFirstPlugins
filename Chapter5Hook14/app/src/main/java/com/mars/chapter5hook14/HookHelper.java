package com.mars.chapter5hook14;

import android.app.Instrumentation;

/**
 * Created by geyan on 2020/12/19
 */
public class HookHelper {

    public static void hook() {
        Object currentActivityThread = FieldUtils.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        Instrumentation mInstrumentation = (Instrumentation) FieldUtils.getFieldObject(currentActivityThread.getClass(), currentActivityThread, "mInstrumentation");
        MarsInstrumentation marsInstrumentation = new MarsInstrumentation(mInstrumentation);
        FieldUtils.setFieldObject(currentActivityThread.getClass(), currentActivityThread, "mInstrumentation", marsInstrumentation);
    }
}
