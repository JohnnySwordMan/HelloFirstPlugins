package com.mars.dynamic3

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import java.io.File

/**
 * Created by geyan on 2020/11/18
 *
 * 1. 加载两个插件
 * 2. 生成两个ClassLoader
 * 3. 重写getAssets、getResources、getTheme方法
 */
open class BaseActivity : AppCompatActivity() {

    protected lateinit var mClassLoader1: DexClassLoader
    protected lateinit var mClassLoader2: DexClassLoader
    private lateinit var dexPath1: String
    private lateinit var dexPath2: String
    private var mAssetManager: AssetManager? = null
    private var mResources: Resources? = null

    // 存放目录
    private lateinit var mFileRelease: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFileRelease = getDir("dex", Context.MODE_PRIVATE)

        val extractFile1 = getFileStreamPath(HostApplication.PLUGIN1_APK_NAME)
        dexPath1 = extractFile1.path
        mClassLoader1 = DexClassLoader(dexPath1, mFileRelease.absolutePath, null, this.classLoader)

        val extractFile2 = getFileStreamPath(HostApplication.PLUGIN2_APK_NAME)
        dexPath2 = extractFile2.path
        mClassLoader2 = DexClassLoader(dexPath2, mFileRelease.absolutePath, null, this.classLoader)
    }

    protected fun loadResources1() {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, dexPath1)
        mAssetManager = assetManager
        mResources = Resources(
            assetManager,
            super.getResources().displayMetrics,
            super.getResources().configuration
        )
    }

    protected fun loadResources2() {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, dexPath2)
        mAssetManager = assetManager
        mResources = Resources(
            assetManager,
            super.getResources().displayMetrics,
            super.getResources().configuration
        )
    }

    override fun getAssets(): AssetManager {
        return if (mAssetManager == null) {
            super.getAssets()
        } else {
            mAssetManager!!
        }
    }

    override fun getResources(): Resources {
        return if (mResources == null) {
            super.getResources()
        } else {
            mResources!!
        }
    }

}