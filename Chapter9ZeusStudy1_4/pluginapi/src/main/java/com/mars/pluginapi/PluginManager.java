package com.mars.pluginapi;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.mars.components.util.file.FileCopyUtils;
import com.mars.components.util.reflect.FieldUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import kotlin.jvm.Volatile;

/**
 * Created by geyan on 2021/1/17
 */
public class PluginManager {

    public final static List<PluginItem> plugins = new ArrayList<>();

    @Volatile
    public static ClassLoader mCurClassLoader;
    @Volatile
    public static ClassLoader mBaseClassLoader;

    // ContextImpl中的LoadedApk对象中的mPackageInfo
    private static Object mPackageInfo;

    // 原始的application中的BaseContext，不能是其他的，否则会内存泄漏
    @Volatile
    private static Context mBaseContext;

    @Volatile
    public static Resources mCurResources;


    public static void init(Application application) {
        mBaseContext = application.getBaseContext();
        mPackageInfo = FieldUtils.readField(mBaseContext, "mPackageInfo");

        mCurResources = mBaseContext.getResources();
        mBaseClassLoader = mBaseContext.getClassLoader();
        mCurClassLoader = mBaseContext.getClassLoader();

        try {
            AssetManager assetManager = application.getAssets();
            String[] assetsList = assetManager.list("");
            // 遍历宿主的assets文件件，拷贝插件apk
            for (String path : assetsList) {
                if (path.endsWith(".apk")) {
                    String apkName = path;
                    FileCopyUtils.extractAssets(mBaseContext, apkName);
                    PluginItem item = generatePluginItem(apkName);
                    plugins.add(item);
                }
            }
            reloadInstallPluginResources(plugins);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MarsClassLoader marsClassLoader = new MarsClassLoader(mBaseContext.getPackageCodePath(), mBaseContext.getClassLoader());
        File dexOutputDir = mBaseContext.getDir("dex", Context.MODE_PRIVATE);
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        for (PluginItem item : plugins) {
            DexClassLoader dexClassLoader = new DexClassLoader(item.pluginPath, dexOutputPath, null, mBaseClassLoader);
            marsClassLoader.addPluginClassLoader(dexClassLoader);
        }
        FieldUtils.writeField(mPackageInfo, "mClassLoader", marsClassLoader);
        // 为啥要设置这个？
        Thread.currentThread().setContextClassLoader(marsClassLoader);
        mCurClassLoader = marsClassLoader;
    }

    private static void reloadInstallPluginResources(List<PluginItem> plugins) {

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);

            addAssetPath.invoke(assetManager, mBaseContext.getPackageResourcePath());

            for (PluginItem item : plugins) {
                addAssetPath.invoke(assetManager, item.pluginPath);
            }

            Resources newResources = new Resources(assetManager,
                    mBaseContext.getResources().getDisplayMetrics(),
                    mBaseContext.getResources().getConfiguration());

            FieldUtils.writeField(mBaseContext, "mResources", newResources);
            //这是最主要的需要替换的，如果不支持插件运行时更新，只留这一个就可以了
            FieldUtils.writeField(mPackageInfo, "mResources", newResources);

            //清除一下之前的resource的数据，释放一些内存
            //因为这个resource有可能还被系统持有着，内存都没被释放
            //clearResoucesDrawableCache(mNowResources);

            mCurResources = newResources;
            //需要清理mtheme对象，否则通过inflate方式加载资源会报错
            //如果是activity动态加载插件，则需要把activity的mTheme对象也设置为null
            FieldUtils.writeField(mBaseContext, "mTheme", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static PluginItem generatePluginItem(String apkName) {
        File file = mBaseContext.getFileStreamPath(apkName);
        PluginItem item = new PluginItem();
        item.pluginPath = file.getAbsolutePath();
        item.packageInfo = DLUtils.getPackageInfo(mBaseContext, item.pluginPath);
        return item;
    }

}
