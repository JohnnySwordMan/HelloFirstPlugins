package com.mars.chapter9activityhook1_v2.hook.model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.mars.chapter9activityhook1_v2.hook.AMSHookHelper;
import com.mars.components.util.reflect.FieldUtils;
import com.mars.components.util.reflect.MethodUtils;

import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2021/1/16
 */
public class ActivityThreadHook implements Handler.Callback {

    private Handler target;

    public ActivityThreadHook(Handler base) {
        target = base;
    }

    /**
     * 详见ActivityThread#handleMessage方法
     */
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
        }
        target.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Object obj = msg.obj;
        Intent intent = (Intent) FieldUtils.readField(obj, "intent");
        Intent targetIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        // 如果Hook操作是在Application中调用的，那么targetIntent需要判空；如果放在MainActivity中执行，则无需判空
        if (targetIntent == null) {
            return;
        }
        intent.setComponent(targetIntent.getComponent());
        // 修改packageName，命中缓存。因为之前是通过欺骗AMS的，因此当前的packageName还是StubActivity对应的packageName，
        // 现在需要改成插件PluginActivity对应的packageName
        ActivityInfo activityInfo = (ActivityInfo) FieldUtils.readField(obj, "activityInfo");
        activityInfo.applicationInfo.packageName = targetIntent.getPackage() == null ? targetIntent.getComponent().getPackageName() : targetIntent.getPackage();

        try {
            hookPackageManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * LoadedApk.initializeJavaContextClassLoader方法内部会检查这个包是否在系统安装，如果没有安装，会抛出异常。
     * Caused by: java.lang.IllegalStateException: Unable to get package info for com.mars.plugin; is package not installed?
     * at android.app.LoadedApk.initializeJavaContextClassLoader(LoadedApk.java:634)
     * at android.app.LoadedApk.makeApplication(LoadedApk.java:792)
     * at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2555) 
     * at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2707) 
     * at android.app.ActivityThread.-wrap12(ActivityThread.java) 
     * at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1460) 
     * at com.mars.chapter9activityhook1_v2.hook.model.ActivityThreadHook.handleMessage(ActivityThreadHook.java:32) 
     * at android.os.Handler.dispatchMessage(Handler.java:98) 
     * at android.os.Looper.loop(Looper.java:154) 
     * at android.app.ActivityThread.main(ActivityThread.java:6077) 
     * at java.lang.reflect.Method.invoke(Native Method) 
     * at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:866) 
     * at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:756) 
     */
    private void hookPackageManager() throws ClassNotFoundException {
        Object sCurrentActivityThread = FieldUtils.readStaticField("android.app.ActivityThread", "sCurrentActivityThread");
        // 获取当前ActivityThread的sPackageManager字段
        Object sPackageManager = FieldUtils.readField(sCurrentActivityThread, "sPackageManager");

        @SuppressLint("PrivateApi")
        Class<?> iPackageManager = Class.forName("android.content.pm.IPackageManager");
        Object proxy = Proxy.newProxyInstance(iPackageManager.getClassLoader(), new Class[]{iPackageManager}, new PackageManagerHook(sPackageManager));
        FieldUtils.writeField(sCurrentActivityThread, "sPackageManager", proxy);
    }
}
