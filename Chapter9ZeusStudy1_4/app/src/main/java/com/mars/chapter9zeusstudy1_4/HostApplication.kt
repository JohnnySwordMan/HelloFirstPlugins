package com.mars.chapter9zeusstudy1_4

import android.app.Application
import android.content.Context
import com.mars.chapter9zeusstudy1_4.hook.AMSHookHelper
import com.mars.pluginapi.PluginManager

/**
 * Created by geyan on 2021/1/17
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        PluginManager.init(this)

        AMSHookHelper.hookActivityManagerNative()
        AMSHookHelper.hookActivityThread()
    }
}