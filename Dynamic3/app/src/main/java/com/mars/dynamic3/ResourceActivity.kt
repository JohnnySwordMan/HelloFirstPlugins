package com.mars.dynamic3

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class ResourceActivity : BaseActivity() {

    private lateinit var mTvText: TextView
    private lateinit var mIvImg: ImageView
    private lateinit var mLayout: LinearLayout
    private lateinit var mBtn1: Button
    private lateinit var mBtn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)

        mBtn1 = findViewById(R.id.btn1)
        mBtn2 = findViewById(R.id.btn2)
        mTvText = findViewById(R.id.tv_text)
        mIvImg = findViewById(R.id.iv_img)
        mLayout = findViewById(R.id.layout)

        mBtn1.setOnClickListener {
            loadResources1()
            val clazz = mClassLoader1.loadClass("com.mars.plugin1.UIUtil")
//            doSomething(clazz)
            doSomeThingV2ForPlugin1(mClassLoader1)
        }
        mBtn2.setOnClickListener {
            loadResources2()
            val clazz = mClassLoader2.loadClass("com.mars.plugin2.UIUtil")
//            doSomething(clazz)
            doSomeThingV2ForPlugin2(mClassLoader2)
        }
    }

    /**
     * 方案二
     */
    private fun doSomeThingV2ForPlugin1(cl: ClassLoader) {
        val strClazz = cl.loadClass("com.mars.plugin1.R\$string")
        val resId1 = RefInvoke.getStaticFieldObject(strClazz, "hello_message") as Int
        mTvText.text = resources.getString(resId1)

        val drawableClazz = cl.loadClass("com.mars.plugin1.R\$drawable")
        val resId2 = RefInvoke.getStaticFieldObject(drawableClazz, "robert") as Int
        mIvImg.background = resources.getDrawable(resId2)

        val layoutClazz = cl.loadClass("com.mars.plugin1.R\$layout")
        val resId3 = RefInvoke.getStaticFieldObject(layoutClazz, "activity_main") as Int
        val view = LayoutInflater.from(this).inflate(resId3, null)
        mLayout.removeAllViews()
        mLayout.addView(view)
    }


    private fun doSomeThingV2ForPlugin2(cl: ClassLoader) {
        val strClazz = cl.loadClass("com.mars.plugin2.R\$string")
        val resId1 = RefInvoke.getStaticFieldObject(strClazz, "hello_message") as Int
        mTvText.text = resources.getString(resId1)

        val drawableClazz = cl.loadClass("com.mars.plugin2.R\$drawable")
        val resId2 = RefInvoke.getStaticFieldObject(drawableClazz, "robert") as Int
        mIvImg.background = resources.getDrawable(resId2)

        val layoutClazz = cl.loadClass("com.mars.plugin2.R\$layout")
        val resId3 = RefInvoke.getStaticFieldObject(layoutClazz, "activity_main") as Int
        val view = LayoutInflater.from(this).inflate(resId3, null)
        mLayout.removeAllViews()
        mLayout.addView(view)
    }

    /**
     * 方案一
     */
    private fun doSomething(clazz: Class<*>) {
        val str = RefInvoke.invokeStaticMethod(
            clazz, "getTextString",
            Context::class.java, this
        ) as String
        mTvText.text = str

        val drawable = RefInvoke.invokeStaticMethod(
            clazz, "getImageDrawable",
            Context::class.java, this
        ) as Drawable
        mIvImg.background = drawable

        mLayout.removeAllViews()
        val view: View = RefInvoke.invokeStaticMethod(
            clazz, "getLayout",
            Context::class.java, this
        ) as View
        mLayout.addView(view)
    }
}