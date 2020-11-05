package com.mars.zeusstudy1_1

import android.app.Application
import android.content.Context
import com.mars.pluginapi.PluginManager

/**
 * Created by geyan on 2020/11/6
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        PluginManager.init(this)
    }
}