package com.mars.chapter5hook31

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * 启动没有在AndroidManifest中声明的Activity
 * Hook上半场：App将启动Activity信息发送给AMS之前，有三个地方可以Hook
 * 1. Activity的mInstrumentation字段进行Hook，适用于Activity的startActivity方式
 * 2. ActivityThread的mInstrumentation字段进行Hook，适用于Context的startActivity方式
 * 3. 对AMN进行Hook，适用于Activity和Context的startActivity
 *
 * Hook下半场：AMS将启动Activity信息发送给ActivityThread，有两个地方可以Hook
 * 1. 对H类的mCallback字段进行Hook
 * 2. 对ActivityThread的mInstrumentation字段进行Hook
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mBtnStartTarget: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnStartTarget = findViewById(R.id.btn_start_activity)
        mBtnStartTarget.setOnClickListener {
            startTargetActivity()
        }
    }

    private fun startTargetActivity() {
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }
}