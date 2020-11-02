package com.mars.activityhook2;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2020/10/31
 */
class MockClass1 implements InvocationHandler {

    Object target;

    public MockClass1(Object base) {
        target = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Intent raw;
            int idx = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    idx = i;
                    break;
                }
            }
            raw = (Intent) args[idx];
            Intent newIntent = new Intent();
            String stubPackage = "com.mars.activityhook2";
            ComponentName componentName = new ComponentName(stubPackage, StubActivity.class.getName());
            newIntent.setComponent(componentName);
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, raw);
            args[idx] = newIntent;
            Log.e("gy", "hook startActivity success");
        }
        return method.invoke(target, args);
    }
}
