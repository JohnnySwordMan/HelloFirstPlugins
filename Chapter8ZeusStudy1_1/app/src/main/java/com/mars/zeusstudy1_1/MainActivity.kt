package com.mars.zeusstudy1_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mars.pluginapi.PluginManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStartPlugin).setOnClickListener {
            val intent = Intent()
            val activityName =
                "${PluginManager.getPlugins()[0].packageInfo.packageName}.SimpleToolActivity"
            intent.setClass(this, Class.forName(activityName))
            startActivity(intent)
        }
    }
}