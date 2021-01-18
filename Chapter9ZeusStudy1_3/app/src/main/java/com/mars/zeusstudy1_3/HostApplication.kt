package com.mars.zeusstudy1_3

import android.app.Application
import android.content.Context
import com.mars.pluginapi.PluginManager

/**
 * Created by geyan on 2021/1/19
 */
class HostApplication : Application() {



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        PluginManager.init(this)

        mockData()
        AMSHookHelper.hookActivityManagerNative()
        AMSHookHelper.hookActivityThread()
    }

    private fun mockData() {
        pluginActivityMap["com.mars.plugin.PluginActivity"] =
            "com.mars.zeusstudy1_3.SingleTaskActivity1"
        pluginActivityMap["com.mars.plugin.PluginActivity2"] =
            "com.mars.zeusstudy1_3.SingleTopActivity1"
    }

    companion object {

        val pluginActivityMap = HashMap<String, String>()

        @JvmStatic
        fun getPluginActivityMap(): Map<String, String> {
            return pluginActivityMap
        }
    }
}