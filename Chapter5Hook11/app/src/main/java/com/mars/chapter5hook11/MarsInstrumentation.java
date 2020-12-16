package com.mars.chapter5hook11;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.mars.chapter5hook11.util.MethodUtils;

/**
 * Created by geyan on 2020/12/17
 */
public class MarsInstrumentation extends Instrumentation {


    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;

    public MarsInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Log.e("gy", "MarsInstrumentation---execStartActivity");
        // 反射调用原始的execStartActivity
        Class[] p1 = {Context.class, IBinder.class,
                IBinder.class, Activity.class,
                Intent.class, int.class, Bundle.class};
        Object[] v1 = {who, contextThread, token, target,
                intent, requestCode, options};
        return (ActivityResult) MethodUtils.invokeMethod(
                mBase, "execStartActivity", p1, v1);
    }
}

