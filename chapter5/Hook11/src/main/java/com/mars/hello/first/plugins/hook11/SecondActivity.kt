package com.mars.hello.first.plugins.hook11

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SecondActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}