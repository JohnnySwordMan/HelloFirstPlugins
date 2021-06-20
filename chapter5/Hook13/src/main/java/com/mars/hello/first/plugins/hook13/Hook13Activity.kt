package com.mars.hello.first.plugins.hook13

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hook13.*

class Hook13Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook13)

        btnStart?.setOnClickListener {
            val intent = Intent(this, TargetActivity::class.java)
            startActivity(intent)
        }
    }
}