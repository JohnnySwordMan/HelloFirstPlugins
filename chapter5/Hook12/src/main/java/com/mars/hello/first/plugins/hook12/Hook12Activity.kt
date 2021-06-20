package com.mars.hello.first.plugins.hook12

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hook12.*

/**
 * 对AMN的getDefault方法进行Hook
 */
class Hook12Activity : Activity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook12)

        btnStart?.setOnClickListener {
            val intent = Intent(this, TargetActivity::class.java)
            startActivity(intent)
        }

    }
}