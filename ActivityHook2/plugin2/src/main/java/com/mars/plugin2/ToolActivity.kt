package com.mars.plugin2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ToolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool)
        Log.e("gy", "ToolActivity onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("gy", "ToolActivity onDestroy")
    }
}