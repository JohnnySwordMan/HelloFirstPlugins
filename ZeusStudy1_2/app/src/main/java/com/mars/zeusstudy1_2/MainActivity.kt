package com.mars.zeusstudy1_2

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mars.pluginservice.PluginManager

class MainActivity : Activity() {

    private lateinit var mBtnStartPlugin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnStartPlugin = findViewById(R.id.start_activity_in_plugin)

        mBtnStartPlugin.setOnClickListener {
            startActivityInPlugin()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("gy", "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("gy", "MainActivity onPause")
    }

    private fun startActivityInPlugin() {
        startActivity(Intent().apply {
            val activityName = PluginManager.getPlugins()[0].packageInfo.packageName + ".RecorderActivity"
            component = ComponentName(PluginManager.getPlugins()[0].packageInfo.packageName, activityName)
        })
    }
}