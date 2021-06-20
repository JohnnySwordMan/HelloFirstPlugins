package com.mars.hello.first.plugins.hook12

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2021/6/20
 */
class Hook12Application : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        try {
            AMSHookHelper.hookActivityManagerNative()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}