package com.mars.chapter5hook13;

import android.os.Handler;

import com.mars.chapter5hook13.util.FieldUtils;

/**
 * Created by geyan on 2020/12/19
 */
public class HookHelper {

    public static void hook() {
        Object currentActivityThread = FieldUtils.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        Handler mH = (Handler) FieldUtils.getFieldObject("android.app.ActivityThread", currentActivityThread, "mH");
        FieldUtils.setFieldObject(Handler.class, mH, "mCallback", new MockClass2(mH));
    }

}
