package com.mars.pluginapi

import android.content.pm.PackageInfo

/**
 * Created by geyan on 2020/11/6
 */
data class PluginItem(val packageInfo: PackageInfo, val pluginPath: String)