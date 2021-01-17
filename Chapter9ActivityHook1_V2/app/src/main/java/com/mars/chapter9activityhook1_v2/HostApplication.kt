package com.mars.chapter9activityhook1_v2

import android.app.Application
import android.content.Context
import com.mars.chapter9activityhook1_v2.hook.AMSHookHelper
import com.mars.chapter9activityhook1_v2.hook.LoadedApkClassLoaderHookHelper
import com.mars.components.util.file.FileCopyUtils

/**
 * Created by geyan on 2021/1/16
 */
class HostApplication : Application() {

    companion object {
        private var mContext: Context? = null

        @JvmStatic
        fun getContext(): Context? {
            return mContext
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mContext = base
        FileCopyUtils.extractAssets(base, "plugin.apk")
        LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath("plugin.apk"))
        AMSHookHelper.hookActivityManagerNative()
        AMSHookHelper.hookActivityThread()
    }

}