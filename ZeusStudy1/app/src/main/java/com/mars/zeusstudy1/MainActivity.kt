package com.mars.zeusstudy1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val intent = Intent()
            val serviceName = "com.mars.plugin1.TestService1"
            intent.setClassName(this, serviceName)
            startService(intent)
        }
    }
}