package com.mars.activityhook2

import android.os.Handler
import com.mars.activityhook2.util.RefInvoke
import java.lang.reflect.Proxy

/**
 * Created by geyan on 2020/10/31
 */
object AMSHookHelper {

    const val EXTRA_TARGET_INTENT = "extra_target_intent"

    /**
     * hook ActivityManagerNative中的startActivity方法
     */
    fun hookAMN() {
        val gDefault =
            RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault")
        val mInstance = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance")
        val class2Interface = Class.forName("android.app.IActivityManager")
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(class2Interface),
            MockClass1(mInstance)
        )
        RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy)
    }

    /**
     * hook ActivityThread
     */
    fun hookActivityThread() {

        val currentActivityThread =
            RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread")
        val mH = RefInvoke.getFieldObject(currentActivityThread, "mH") as Handler
        RefInvoke.setFieldObject(Handler::class.java, mH, "mCallback", MockClass2(mH))
    }
}