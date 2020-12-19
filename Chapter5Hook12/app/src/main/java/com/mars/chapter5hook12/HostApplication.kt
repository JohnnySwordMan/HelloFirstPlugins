package com.mars.chapter5hook12

import android.app.Application
import android.content.Context
import com.mars.chapter5hook12.hook.AMSHookHelper

/**
 * Created by geyan on 2020/12/19
 * 在插件化框架中，通常是在自定义的Application的attachBaseContext方法中执行AMSHookHelper的hookAMN方法
 */
class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AMSHookHelper.hookAMN()
    }
}