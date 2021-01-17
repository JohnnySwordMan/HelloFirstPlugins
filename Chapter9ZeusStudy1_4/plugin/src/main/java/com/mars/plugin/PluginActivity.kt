package com.mars.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mars.pluginapi.BaseActivity

class PluginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin)

        Log.e("gy", "PluginActivity onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.e("gy", "PluginActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("gy", "PluginActivity onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("gy", "PluginActivity onDestroy")
    }
}