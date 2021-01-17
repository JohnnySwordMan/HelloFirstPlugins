package com.mars.chapter9zeusstudy1_4

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mars.pluginapi.BaseActivity
import com.mars.pluginapi.PluginManager

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            val intent = Intent()
            val pluginPackageName = PluginManager.plugins[0].packageInfo.packageName
            val activityName = "$pluginPackageName.PluginActivity"
            intent.component = ComponentName(pluginPackageName, activityName)
            startActivity(intent)
        }
    }
}