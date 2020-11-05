package com.mars.pluginapi

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by geyan on 2020/11/6
 */
open class BaseActivity: AppCompatActivity() {

    override fun getResources(): Resources {
        return PluginManager.getResources()
    }
}