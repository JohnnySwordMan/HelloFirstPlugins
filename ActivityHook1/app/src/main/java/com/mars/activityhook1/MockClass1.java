package com.mars.activityhook1;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan.marx on 2020/10/24
 */
public class MockClass1 implements InvocationHandler {

    Object target;

    public MockClass1(Object base) {
        target = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("gy", "MockClass1 method name = " + method.getName());
        if ("startActivity".equals(method.getName())) {
            // 只拦截这个方法
            Intent raw;
            int index  = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) args[index];
            Intent newIntent = new Intent();
            // 替身Activity的包名
            String stubPackage = "com.mars.activityhook1";
            // 将要启动的Activity替换成StubActivity
            ComponentName componentName = new ComponentName(stubPackage, StubActivity.class.getName());
            newIntent.setComponent(componentName);
            // 把原始要启动的TargetActivity保存起来
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, raw);
            args[index] = newIntent;
            Log.e("gy", "MockClass1 hook success");
            return method.invoke(target, args);
        }
        return method.invoke(target, args);
    }
}
