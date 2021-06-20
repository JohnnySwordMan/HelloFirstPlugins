package com.mars.hello.first.plugins.hook13

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2021/6/20
 */
class Hook13Application : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        try {
            HookHelper.hookCallbackOfHandler()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}