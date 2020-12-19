package com.mars.chapter5hook14;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by geyan on 2020/12/19
 */
class MarsInstrumentation extends Instrumentation {

    Instrumentation target;

    MarsInstrumentation(Instrumentation base) {
        target = base;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Log.e("gy", "MarsInstrumentation---newActivity");
        return target.newActivity(cl, className, intent);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        Log.e("gy", "MarsInstrumentation---callActivityOnCreate");
        target.callActivityOnCreate(activity, icicle);
    }
}
