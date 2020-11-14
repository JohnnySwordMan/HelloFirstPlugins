package com.mars.plugin

import android.content.Context
import android.util.Log
import com.mars.pluginapi.IDynamic

/**
 * Created by geyan on 2020/11/14
 */
class Dynamic : IDynamic {

    override fun getStringResId(context: Context): String {
        // 插件---getStringResId---resource = android.content.res.Resources@85a1894
        Log.e("gy", "插件---getStringResId---resource = ${context.resources}")
        return context.resources.getString(R.string.plugin_hello)
    }

}