package com.mars.chapter5hook31

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2020/12/26
 */
class HostApplication : Application() {

    /**
     * 可以放在MainActivity的attachBaseContext中执行；
     * 如果放在Application中Hook的话，需要判断targetIntent不为空
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AMSHookHelper.hookAMN()
//        AMSHookHelper.hookCallbackInH()
        AMSHookHelper.hookActivityThread()
    }
}