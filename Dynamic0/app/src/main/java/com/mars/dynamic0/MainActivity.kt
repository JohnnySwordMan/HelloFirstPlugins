package com.mars.dynamic0

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Method


/**
 * mClassLoader = dalvik.system.DexClassLoader[DexPathList[[zip file "/data/user/0/com.mars.dynamic0/files/plugin1.apk"],nativeLibraryDirectories=[/system/lib, /vendor/lib]]]
 * 宿主ClassLoader = dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.mars.dynamic0-2/base.apk"],nativeLibraryDirectories=[/data/app/com.mars.dynamic0-2/lib/x86, /system/lib, /vendor/lib]]]
 *
 * mClassLoader的parent ClassLoader就是宿主ClassLoader，构造的时候传入的。因此，也可以使用mClassLoader拿到宿主中的类
 *
 * 虽然可以通过loadClass的方法获取插件中的User类，但是宿主中是没有User类的，因此我们不能直接使用，
 * 而是通过反射的方式实例化User并调用其getNickName方法
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mTvNickName: TextView
    private lateinit var mBtnStart: Button

    private lateinit var mClassLoader: DexClassLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvNickName = findViewById(R.id.tv_nickName)
        mBtnStart = findViewById(R.id.btnStart)

        mBtnStart.setOnClickListener {
            val mLoadClassBean: Class<*>
            try {
                mLoadClassBean = mClassLoader.loadClass("com.mars.plugin.User")
                val methodGetNickName: Method = mLoadClassBean.getMethod("getNickName")
//                mLoadClassBean = mClassLoader.loadClass("com.mars.dynamic0.Music")
//                val methodGetMusicName: Method = mLoadClassBean.getMethod("getMusicName")
                val userObj = mLoadClassBean.newInstance()
                methodGetNickName.isAccessible = true
                val name = methodGetNickName.invoke(userObj) as String
                mTvNickName.text = name
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
}