package com.mars.activityhook1.classloader_hook;

import android.util.Log;

import dalvik.system.DexClassLoader;

/**
 * Created by geyan.marx on 2020/10/24
 * 自定义的ClassLoader, 用于加载"插件"的资源和代码
 */
public class CustomClassLoader extends DexClassLoader {

    public CustomClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
        Log.e("gy", "CustomClassLoader---dexPath = " + dexPath +
                " , optimizedDirectory = " + optimizedDirectory +
                " , librarySearchPath = " + librarySearchPath);
    }
}
