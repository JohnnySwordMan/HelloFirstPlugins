package com.mars.pluginservice

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * Created by geyan on 2020/11/23
 */
object AppUtils {

    fun getPackageInfo(
        context: Context,
        apkFilepath: String?
    ): PackageInfo? {
        val pm = context.packageManager
        var pkgInfo: PackageInfo? = null
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath!!, PackageManager.GET_ACTIVITIES)
        } catch (e: java.lang.Exception) {
            // should be something wrong with parse
            e.printStackTrace()
        }
        return pkgInfo
    }
}