package com.mars.dynamic0

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2020/11/8
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        FileUtils.copyFromAssets(base!!, APK_NAME)
    }

    companion object {

        const val APK_NAME = "plugin1.apk"
    }
}