package com.mars.zeusstudy1;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by mars on 2020/11/4
 * <p>
 * https://www.androidos.net.cn/android/7.0.0_r31/xref/libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
 */
public final class BaseDexClassLoaderHookHelper {

    /**
     * 插件dex合并到宿主
     */
    public static void patchClassLoader(ClassLoader classLoader, File apkFile, File optDexFile) throws IOException {

        // 1. 获取BaseDexClassLoader的pathList字段，pathList字段是DexPathList类型的
        Object pathListObj = RefInvoke.getFieldObject(DexClassLoader.class.getSuperclass(), classLoader, "pathList");

        // 2. 获取DexPathList的dexElements字段，dexElements字段是Element[]类型的
        Object[] dexElements = (Object[]) RefInvoke.getFieldObject(pathListObj, "dexElements");

        // Element类型
        Class<?> elementClass = dexElements.getClass().getComponentType();

        // 3. 创建新的Element[]
        Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);

        // 4. 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
        Class[] p1 = {File.class, boolean.class, File.class, DexFile.class};
        Object[] v1 = {apkFile, false, apkFile, DexFile.loadDex(apkFile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0)};
        Object o = RefInvoke.createObject(elementClass, p1, v1);
        Object[] toAddElementArray = new Object[]{o};

        // 5. 复制
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);

        // 6. 替换
        RefInvoke.setFieldObject(pathListObj, "dexElements", newElements);
    }
}
