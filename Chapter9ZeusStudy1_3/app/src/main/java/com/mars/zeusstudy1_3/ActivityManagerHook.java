package com.mars.zeusstudy1_3;

import android.content.ComponentName;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2021/1/19
 */
public class ActivityManagerHook implements InvocationHandler {
    // StubActivity PackageName
    private static final String PACKAGE_NAME_STUBACTIVITY = "com.mars.zeusstudy1_3";

    private final Object target;

    public ActivityManagerHook(Object obj) {
        target = obj;
    }

    /**
     * 拦截startActivity方法，将要启动的插件Activity替换成占位Activity
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            int idx = checkIntent(args);
            Intent originIntent = (Intent) args[idx];
            Intent newIntent = new Intent();


            ComponentName componentName;

            String rawClass = originIntent.getComponent().getClassName();
            if (HostApplication.getPluginActivityMap().containsKey(rawClass)) {
                String activity = HostApplication.getPluginActivityMap().get(rawClass);
                int pos = activity.lastIndexOf(".");
                String pluginPackage = activity.substring(0, pos);
                componentName = new ComponentName(pluginPackage, activity);
            } else {
                componentName = new ComponentName(PACKAGE_NAME_STUBACTIVITY, StubActivity.class.getName());
            }

            newIntent.setComponent(componentName);
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, originIntent);
            args[idx] = newIntent;
        }
        return method.invoke(target, args);
    }

    private int checkIntent(Object[] args) {
        int idx = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                idx = i;
                break;
            }
        }
        return idx;
    }
}
