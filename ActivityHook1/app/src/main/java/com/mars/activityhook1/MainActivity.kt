package com.mars.activityhook1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mars.activityhook1.classloader_hook.LoadedApkClassLoaderHookHelper
import com.mars.activityhook1.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 启动没有 在AndroidManifest中声明的插件Activity
 */
class MainActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Utils.extractAssets(newBase, "plugin1.apk")
        LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath("plugin1.apk"))
        AMSHookHelper.hookAMN()
        AMSHookHelper.hookActivityThread()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("gy", "context classloader: ${applicationContext.classLoader}")

        btnStartPlugin.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.mars.plugin1", "com.mars.plugin1.RecorderActivity")
            startActivity(intent)
        }
    }
}