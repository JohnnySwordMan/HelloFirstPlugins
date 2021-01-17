package com.mars.chapter9activityhook1_v2.hook;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;

import com.mars.chapter9activityhook1_v2.CustomClassLoader;
import com.mars.chapter9activityhook1_v2.HostApplication;
import com.mars.components.util.file.FileCopyUtils;
import com.mars.components.util.reflect.FieldUtils;
import com.mars.components.util.reflect.MethodUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by geyan on 2021/1/17
 * <p>
 * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/ActivityManagerNative.java
 * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/ActivityThread.java
 * https://www.androidos.net.cn/android/9.0.0_r8/xref/frameworks/base/core/java/android/content/pm/PackageParser.java
 */
public class LoadedApkClassLoaderHookHelper {

    private static final String ACTIVITY_THREAD_NAME = "android.app.ActivityThread";

    private static Map<String, Object> loadedApkMap = new HashMap<>();

    public static void hookLoadedApkInActivityThread(File apkFile) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Object sCurrentActivityThread = FieldUtils.readStaticField(ACTIVITY_THREAD_NAME, "sCurrentActivityThread");
        // 获取当前ActivityThread对象的mPackages字段
        Map mPackages = (Map) FieldUtils.readField(sCurrentActivityThread, "mPackages");

        Object defaultCompatibilityInfo = FieldUtils.readStaticField("android.content.res.CompatibilityInfo", "DEFAULT_COMPATIBILITY_INFO");
        ApplicationInfo applicationInfo = generateApplicationInfo(apkFile);

        @SuppressLint("PrivateApi")
        Class<?> compatibilityInfoCls = Class.forName("android.content.res.CompatibilityInfo");
        // 调用当前ActivityThread的getPackageInfoNoCheck方法获取LoadedApk对象
        Object loadedApk = MethodUtils.invokeMethod(sCurrentActivityThread, "getPackageInfoNoCheck",
                new Class[]{ApplicationInfo.class, compatibilityInfoCls},
                new Object[]{applicationInfo, defaultCompatibilityInfo});

        String optDexPath = FileCopyUtils.getPluginOptDexDir(HostApplication.getContext(), applicationInfo.packageName).getPath();
        String libPath = FileCopyUtils.getPluginLibDir(HostApplication.getContext(), applicationInfo.packageName).getPath();

        // 为插件创建一个ClassLoader
        ClassLoader pluginClassLoader = new CustomClassLoader(apkFile.getPath(), optDexPath, libPath, ClassLoader.getSystemClassLoader());
        FieldUtils.writeField(loadedApk, "mClassLoader", pluginClassLoader);

        // 将插件LoadedApk对象放入缓存中
        WeakReference<Object> weakReference = new WeakReference<>(loadedApk);
        mPackages.put(applicationInfo.packageName, weakReference);

        // 由于是弱引用，因此必须在静态方法中保存，不然容易回收
        loadedApkMap.put(applicationInfo.packageName, weakReference);
    }

    /**
     * 从apk中取得ApplicationInfo信息
     * 反射PackageParser的generateApplicationInfo方法，构建出ApplicationInfo对象，
     * 其他的操作都是围绕着反射调用generateApplicationInfo所需要的入参做准备的。
     * generateApplicationInfo方法在每个Android版本都有改动，因此需要根据不同的版本做适配。
     */
    private static ApplicationInfo generateApplicationInfo(File apkFile) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        @SuppressLint("PrivateApi")
        Class<?> packageParserCls = Class.forName("android.content.pm.PackageParser");
        @SuppressLint("PrivateApi")
        Class<?> packageParser$PackageCls = Class.forName("android.content.pm.PackageParser$Package");
        @SuppressLint("PrivateApi")
        Class<?> packageUserStateCls = Class.forName("android.content.pm.PackageUserState");

        /**
         * --->:依赖于
         * 创建Package对象 ---> PackageParser.parsePackage ---> 创建PackageParse对象
         */
        Object packageParser = packageParserCls.newInstance();
        Object packageObj = MethodUtils.invokeMethod(packageParser, "parsePackage", new Class[]{File.class, int.class}, new Object[]{apkFile, 0});
        Object defaultPackageUserState = packageUserStateCls.newInstance();

        ApplicationInfo applicationInfo = (ApplicationInfo) MethodUtils.invokeMethod(packageParser, "generateApplicationInfo",
                new Class[]{packageParser$PackageCls, int.class, packageUserStateCls},
                new Object[]{packageObj, 0, defaultPackageUserState});

        applicationInfo.sourceDir = apkFile.getPath();
        applicationInfo.publicSourceDir = apkFile.getPath();
        return applicationInfo;
    }
}
