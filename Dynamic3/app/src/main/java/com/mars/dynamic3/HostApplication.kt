package com.mars.dynamic3

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2020/11/18
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        FileUtils.copyFromAssets(base!!, PLUGIN1_APK_NAME)
        FileUtils.copyFromAssets(base, PLUGIN2_APK_NAME)
    }

    companion object {

        const val PLUGIN1_APK_NAME = "plugin1.apk"
        const val PLUGIN2_APK_NAME = "plugin2.apk"
    }
}