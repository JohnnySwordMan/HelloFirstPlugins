package com.mars.chapter5hook13

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2020/12/19
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        HookHelper.hook()
    }
}