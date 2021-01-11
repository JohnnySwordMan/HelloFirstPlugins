package com.mars.activityhook2;

import com.mars.activityhook2.util.RefInvoke;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by geyan on 2020/10/31
 * https://www.androidos.net.cn/android/7.0.0_r31/xref/libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
 * https://www.androidos.net.cn/android/7.0.0_r31/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
 *
 * 创建插件的dexElements，将其添加到宿主的dexElements中
 */
public final class BaseDexClassLoaderHookHelper {

    public static void patchClassLoader(ClassLoader cl, File apkFile, File optDexFile) throws IOException {

        // 获取BaseDexClassLoader中的pathList字段，pathList类型是DexPathList
        Object pathListObj = RefInvoke.getFieldObject(DexClassLoader.class.getSuperclass(), cl, "pathList");
        // 获取DexPathList类中的dexElements字段，dexElements类型是Elements[]
        Object[] dexElements = (Object[]) RefInvoke.getFieldObject(pathListObj, "dexElements");
        // Element类型
        Class<?> elementClass = dexElements.getClass().getComponentType();
        // 创建一个数组，用来替换原始数组
        Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);
        // 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile)构造函数
        Class[] p1 = {File.class, boolean.class, File.class, DexFile.class};
        Object[] v1 = {apkFile, false, apkFile, DexFile.loadDex(apkFile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0)};
        Object o = RefInvoke.createObject(elementClass, p1, v1);
        Object[] toAddElementArray = new Object[]{o};
        // 复制原始的elements
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        // 将新增的elements加入。
        // 将toAddElementArray数组从0开始复制到newElements数组中，toAddElementArray[0]放在newElements[dexElements.length]的位置，只复制toAddElementArray.length个
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);
        RefInvoke.setFieldObject(pathListObj, "dexElements", newElements);
    }
}
