package com.mars.hello.first.plugins.common.base

import android.util.Log

/**
 * Created by geyan on 2021/6/19
 */
object Logger {

    @JvmStatic
    fun e(tag: String? = "gy", msg: Any) {
        Log.e(tag, "$msg")
    }
}