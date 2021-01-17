package com.mars.pluginapi;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by geyan on 2021/1/17
 * <p>
 * ClassLoader容器
 * 方案三：将系统的ClassLoader替换为自己的MarsClassLoader，
 * MarsClassLoader内部维护一个list，保存所有插件的ClassLoader。
 * loadClass方法会先尝试使用宿主的ClassLoader加载类，如果不能加载，就遍历list，直到找到一个能加载类的ClassLoader
 */
class MarsClassLoader extends PathClassLoader {

    private List<DexClassLoader> allDexClassLoaderList;

    public MarsClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
        allDexClassLoaderList = new ArrayList<>();
    }


    protected void addPluginClassLoader(DexClassLoader dexClassLoader) {
        if (dexClassLoader != null) {
            allDexClassLoaderList.add(dexClassLoader);
        }
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> cls = null;
        try {
            cls = getParent().loadClass(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cls != null) {
            return cls;
        }
        if (allDexClassLoaderList != null && allDexClassLoaderList.size() > 0) {
            for (DexClassLoader classLoader : allDexClassLoaderList) {
                if (classLoader == null) {
                    continue;
                }
                try {
                    cls = classLoader.loadClass(className);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cls != null) {
                    return cls;
                }
            }
        }
        throw new ClassNotFoundException(className + " in loader " + this);
    }
}
