package com.mars.plugin

import android.app.Activity
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mars.pluginservice.PluginManager

class RecorderActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
    }

    override fun onResume() {
        super.onResume()
        Log.e("gy", "RecorderActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("gy", "RecorderActivity onPause")
    }

    override fun getResources(): Resources {
        return PluginManager.mCurResources
    }
}