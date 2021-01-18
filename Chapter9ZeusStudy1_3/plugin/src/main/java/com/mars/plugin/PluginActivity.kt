package com.mars.plugin

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.mars.pluginapi.BaseActivity


class PluginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin)

        findViewById<Button>(R.id.btn_go_test).setOnClickListener {
            try {
                val intent = Intent()
                val activityName = "com.mars.plugin.PluginActivity2"
                intent.component = ComponentName("com.mars.plugin", activityName)
                intent.putExtra("UserName", "Mars")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        val username = intent!!.getStringExtra("UserName")
        val tv = findViewById<TextView>(R.id.tv_content)
        tv.text = username
    }
}