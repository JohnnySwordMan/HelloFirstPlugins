package com.mars.dynamic1_3

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.mars.pluginapi.IDynamic
import dalvik.system.DexClassLoader

/**
 * 首先插件的Resource对象和宿主的Resource对象肯定是不一样的，
 * Apk---AssetManager---Resources
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mTvText: TextView
    private lateinit var mBtnStart: Button
    private lateinit var dexPath: String
    private var mAssetManager: AssetManager? = null
    private var mResources: Resources? = null
    private lateinit var mClassLoader: DexClassLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvText = findViewById(R.id.tv_text)
        mBtnStart = findViewById(R.id.btn_start)

        mBtnStart.setOnClickListener {
            onClickBtnStart()
        }

        val extractFile = getFileStreamPath(HostApplication.APK_NAME)
        dexPath = extractFile.path
        val fileRelease = getDir("dex", Context.MODE_PRIVATE)
        mClassLoader = DexClassLoader(dexPath, fileRelease.absolutePath, null, this.classLoader)

        // 宿主 Resources = android.content.res.Resources@539a707
        Log.e("gy", "宿主 Resources = $resources")

        mTvText.text = resources.getString(R.string.host_hello_1)
    }

    private fun onClickBtnStart() {
        loadResources()
        val mLoadClassDynamic = mClassLoader.loadClass("com.mars.plugin.Dynamic")
        val iDynamic = mLoadClassDynamic.newInstance() as IDynamic
        val stringInPlugin = iDynamic.getStringResId(this)
        mTvText.text = stringInPlugin

        // 必现Crash，因为此时getResources方法返回的是mResources对象，即插件apk对应的Resources对象，
        // 用插件apk对应的Resources对象又怎么能拿到宿主的资源呢？
        mTvText.text = resources.getString(R.string.host_hello_2)
    }

    /**
     * 反射调用AssetManager的addAssetPath方法，将插件apk添加到路径中
     * 通过反射，创建AssetManager对象，调用addAssetPath方法，把插件plugin的路径添加到AssetManager对象中，从此这个AssetManager对象就只为插件服务。
     * 如果这个AssetManager为空，则调用父类ContextImpl的getAssets方法，这个时候得到的AssetManager对象就指向宿主HostApp了
     */
    private fun loadResources() {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, dexPath)
        mAssetManager = assetManager

        mResources = Resources(
            assetManager,
            super.getResources().displayMetrics,
            super.getResources().configuration
        )
        // 插件 Resources = android.content.res.Resources@85a1894
        Log.e("gy", "插件 Resources = $mResources")
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