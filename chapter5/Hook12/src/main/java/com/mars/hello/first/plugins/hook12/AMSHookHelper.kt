package com.mars.hello.first.plugins.hook12

import android.annotation.SuppressLint
import com.mars.components.util.reflect.FieldUtils
import java.lang.reflect.Proxy

/**
 * Created by geyan on 2021/6/20
 */
object AMSHookHelper {

    /**
     * hook ActivityManager的getDefault方法的返回值
     */
    @SuppressLint("PrivateApi")
    fun hookActivityManagerNative() {

        // 反射，获取AMN的gDefault属性，gDefault是一个单例
        val gDefault =
            FieldUtils.readStaticField("android.app.ActivityManagerNative", "gDefault")

        // 获取gDefault.get()的返回值，即mInstance，也就是IActivityManager的实现类对象
        val mInstance = FieldUtils.readField("android.util.Singleton", gDefault, "mInstance")

        val c1 = arrayOf(Class.forName("android.app.IActivityManager"))

        // 动态代理
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            c1,
            HookIActivityManager(mInstance)
        )

        FieldUtils.writeField("android.util.Singleton", gDefault, "mInstance", proxy)
    }
}