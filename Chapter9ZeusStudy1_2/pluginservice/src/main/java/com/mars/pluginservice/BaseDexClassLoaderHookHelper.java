package com.mars.pluginservice;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by geyan on 2020/11/23
 */
public final class BaseDexClassLoaderHookHelper {

    /**
     * 1. 获取BaseDexClassLoader的pathList字段，pathList字段是DexPathList类型的。这里获取的是宿主的pathList
     * 2. 获取DexPathList的dexElements字段，dexElements字段是Element[]类型的
     * 3. 创建新的Element[]
     * 4. 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
     * 5. 复制
     * 6. 替换
     */
    public static void pathClassLoader(ClassLoader classLoader, File apkFile, File optDexFile) throws IOException {
        Object pathListObj = RefInvoke.getFieldObject(DexClassLoader.class.getSuperclass(), classLoader, "pathList");
        Object[] dexElements = (Object[]) RefInvoke.getFieldObject(pathListObj, "dexElements");
        Class<?> elementClass = dexElements.getClass().getComponentType();
        Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);
        Class[] p1 = {File.class, boolean.class, File.class, DexFile.class};
        // getCanonicalPath() 方法返回绝对路径，会把 ..\ 、.\ 这样的符号解析掉
        Object[] v1 = {apkFile, false, apkFile, DexFile.loadDex(apkFile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0)};
        Object o = RefInvoke.createObject(elementClass, p1, v1);
        Object[] toAddElementArray = new Object[]{o};
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);
        RefInvoke.setFieldObject(pathListObj, "dexElements", newElements);
    }
}
