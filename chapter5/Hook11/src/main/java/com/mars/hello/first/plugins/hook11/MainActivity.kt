package com.mars.hello.first.plugins.hook11

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mars.components.util.reflect.FieldUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        // TODO 为啥在这里反射设置自定义的Instrumentation，就没生效呢？
//        hookInstrumentation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hookInstrumentation()

        btnStart.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * hook Activity的mInstrumentation字段
     */
    private fun hookInstrumentation() {
        val originInstrumentation =
            FieldUtils.readField(Activity::class.java, this, "mInstrumentation") as Instrumentation?

        val instrumentationWrapper = MarsInstrumentation(originInstrumentation)

        FieldUtils.writeField(
            Activity::class.java,
            this,
            "mInstrumentation",
            instrumentationWrapper
        )
    }
}