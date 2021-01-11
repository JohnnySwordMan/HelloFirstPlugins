package com.mars.plugin1

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mars.pluginapi.BaseActivity

class SimpleToolActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_tool)
        Log.e("gy", "SimpleToolActivity onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.e("gy", "SimpleToolActivity onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("gy", "SimpleToolActivity onDestroy")
    }
}