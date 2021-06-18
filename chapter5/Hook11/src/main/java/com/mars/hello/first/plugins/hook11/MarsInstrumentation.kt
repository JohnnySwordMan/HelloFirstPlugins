package com.mars.hello.first.plugins.hook11

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.mars.components.util.reflect.MethodUtils
import com.mars.hello.first.plugins.common.base.Logger

/**
 * Created by geyan on 2021/6/19
 *
 * hook Instrumentation
 */
class MarsInstrumentation(private val originInstrumentation: Instrumentation?) : Instrumentation() {

    companion object {
        const val TAG = "MarsInstrumentation"
    }

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?,
        intent: Intent?, requestCode: Int, options: Bundle?
    ): ActivityResult? {
        Logger.e(TAG, "invoke execStartActivity")

        // 反射调用原始的execStartActivity
        val paramTypes = arrayOf(
            Context::class.java, IBinder::class.java,
            IBinder::class.java, Activity::class.java,
            Intent::class.java, Int::class.javaPrimitiveType, Bundle::class.java
        )

        val paramValues = arrayOf(
            who, contextThread, token, target,
            intent, requestCode, options
        )
        return MethodUtils.invokeMethod(
            originInstrumentation,
            "execStartActivity",
            paramTypes,
            paramValues
        ) as ActivityResult?
    }
}