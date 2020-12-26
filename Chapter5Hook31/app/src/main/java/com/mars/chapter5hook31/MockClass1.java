package com.mars.chapter5hook31;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2020/12/26
 */
class MockClass1 implements InvocationHandler {

    Object target;

    public MockClass1(Object obj) {
        target = obj;
    }

    /**
     * 拦截startActivity方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Intent originIntent;
            int idx = 0;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        idx = i;
                        break;
                    }
                }
            }
            originIntent = (Intent) args[idx];
            Intent newIntent = new Intent();
            // 替身Activity的包名，在这个项目中由于TargetActivity和StubActivity的包名是一样的，因此可以通过这种方式直接获取
//            String stubPackage = originIntent.getComponent().getPackageName();
            String stubPackage = "com.mars.chapter5hook31";
            ComponentName componentName = new ComponentName(stubPackage, StubActivity.class.getName());
            newIntent.setComponent(componentName);
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, originIntent);
            Log.e("gy", "originIntent = " + originIntent);
            args[idx] = newIntent;
        }
        return method.invoke(target, args);
    }
}
