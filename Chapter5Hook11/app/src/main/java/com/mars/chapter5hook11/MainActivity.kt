package com.mars.chapter5hook11

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.mars.chapter5hook11.util.FieldUtils

/**
 * hook Instrumentation的execStartActivity方法，
 * 由于传入的target是this，即只修改了当前Activity的mInstrumentation字段。
 * 或者将其写到BaseActivity的onCreate方法中，这样就对所有的Activity生效
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hookInstrumentation()

        findViewById<Button>(R.id.hook_startActivity).setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hookInstrumentation() {
        val mInstrumentation = FieldUtils.getFieldObject(
            Activity::class.java,
            this,
            "mInstrumentation"
        ) as Instrumentation

        val marsInstrumentation: Instrumentation =
            MarsInstrumentation(mInstrumentation)

        FieldUtils.setFieldObject(
            Activity::class.java,
            this,
            "mInstrumentation",
            marsInstrumentation
        )
    }
}