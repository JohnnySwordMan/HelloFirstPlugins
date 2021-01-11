package com.mars.zeusstudy1_2

import android.app.Application
import android.content.Context
import com.mars.pluginservice.PluginManager

/**
 * Created by geyan on 2020/11/21
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        PluginManager.init(this)

        AMSHookHelper.hookAMN()
        AMSHookHelper.hookActivityThread()
    }
}