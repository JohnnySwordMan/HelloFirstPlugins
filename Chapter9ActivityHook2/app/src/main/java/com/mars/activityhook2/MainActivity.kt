package com.mars.activityhook2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mars.activityhook2.util.Utils

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        // 模拟插件的下载
        Utils.extractAssets(newBase, apkName)

        val dexFile = getFileStreamPath(apkName)
        val optDexFile = getFileStreamPath("plugin2.dex")

        // 方案二：合并多个dex
        BaseDexClassLoaderHookHelper.patchClassLoader(classLoader, dexFile, optDexFile)

        AMSHookHelper.hookAMN()
        AMSHookHelper.hookActivityThread()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStartPlugin).setOnClickListener {

            val intent = Intent()
            intent.component = ComponentName("com.mars.plugin2", "com.mars.plugin2.ToolActivity")
            startActivity(intent)
        }
    }


    companion object {

        private const val apkName = "plugin2.apk"
    }
}