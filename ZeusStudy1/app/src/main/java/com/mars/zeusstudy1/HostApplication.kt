package com.mars.zeusstudy1

import android.app.Application
import android.content.Context

/**
 * Created by mars on 2020/11/3
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        Utils.extractAssets(base!!, APK_NAME)
        val dexFile = getFileStreamPath(APK_NAME)
        val optDexFile = getFileStreamPath(DEX_NAME)

        BaseDexClassLoaderHookHelper.patchClassLoader(classLoader, dexFile, optDexFile)
    }

    companion object {

        const val APK_NAME = "plugin1.apk"
        private const val DEX_NAME = "plugin1.dex"
    }
}