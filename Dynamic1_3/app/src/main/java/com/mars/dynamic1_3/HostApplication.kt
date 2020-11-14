package com.mars.dynamic1_3

import android.app.Application
import android.content.Context
import com.mars.dynamic1_3.utils.FileUtils

/**
 * Created by geyan on 2020/11/14
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