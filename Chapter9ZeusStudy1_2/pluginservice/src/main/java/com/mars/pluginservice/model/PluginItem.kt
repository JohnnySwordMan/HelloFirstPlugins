package com.mars.pluginservice.model

import android.content.pm.PackageInfo

/**
 * Created by geyan on 2020/11/23
 */
data class PluginItem(
    val packageInfo: PackageInfo,
    val pluginPath: String
)