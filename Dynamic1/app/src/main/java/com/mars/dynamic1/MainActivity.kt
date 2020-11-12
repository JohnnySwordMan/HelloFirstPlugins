package com.mars.dynamic1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.mars.pluginapi.ICallback
import com.mars.pluginapi.IUser
import dalvik.system.DexClassLoader
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mTvText: TextView
    private lateinit var mBtnStart: Button
    private lateinit var mBtnRegister: Button
    private lateinit var mClassLoader: DexClassLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvText = findViewById(R.id.tv_text)
        mBtnStart = findViewById(R.id.btn_start)
        mBtnRegister = findViewById(R.id.btn_register)

        mBtnStart.setOnClickListener {
            onClickButton()
        }

        mBtnRegister.setOnClickListener {
            onClickRegister()
        }

        val extractFile: File = getFileStreamPath(HostApplication.APK_NAME)
        val dexPath = extractFile.path
        val fileRelease = getDir("dex", Context.MODE_PRIVATE)
        // 打印结果：dexPath = /data/user/0/com.mars.dynamic0/files/plugin1.apk
        Log.e("gy", "MainActivity---dexPath = $dexPath")
        // 打印结果：fileRelease.getAbsolutePath() = /data/user/0/com.mars.dynamic0/app_dex
        Log.e("gy", "MainActivity---fileRelease.getAbsolutePath() = ${fileRelease.absolutePath}")

        mClassLoader = DexClassLoader(dexPath, fileRelease.absolutePath, null, classLoader)
        Log.e("gy", "MainActivity---mClassLoader = $mClassLoader")
        Log.e("gy", "MainActivity---宿主ClassLoader = ${this.classLoader}")
    }

    private fun onClickRegister() {
        val mLoadClassBen = mClassLoader.loadClass("com.mars.plugin.User")

        val iUser = mLoadClassBen.newInstance() as IUser

        iUser.register(object : ICallback {
            override fun sendResult(result: String) {
                mTvText.text = result
            }
        })
    }

    private fun onClickButton() {
        val mLoadClassBen = mClassLoader.loadClass("com.mars.plugin.User")

        val iUser = mLoadClassBen.newInstance() as IUser

        Log.e("gy", "init name = ${iUser.getName()}, age = ${iUser.getAge()}")

        iUser.setName("JohnnySwordMan")
        iUser.setAge(28)

        mTvText.text = "I am ${iUser.getName()}, age is = ${iUser.getAge()}"
    }
}