package com.mars.pluginapi

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources

/**
 * Created by geyan on 2020/11/6
 */
object PluginManager {

    private val plugins = arrayListOf<PluginItem>()

    // 原始的application的BaseContext，不能是其他的，否则会内存泄露
    @Volatile
    private lateinit var mBaseContext: Context

    // 当前正在使用的Resources
    @Volatile
    private lateinit var mCurResources: Resources

    // ContextImpl中的LoadedApk对象的mPackageInfo
    private lateinit var mPackageInfo: Any

    fun init(application: Application) {
        mBaseContext = application.baseContext
        mCurResources = mBaseContext.resources
        mPackageInfo = RefInvoke.getFieldObject(mBaseContext, "mPackageInfo")

        try {
            val assetManager = application.assets
            val paths = assetManager.list("")
            val pluginPaths = arrayListOf<String>()

            for (path: String in paths!!) {
                if (path.endsWith(".apk")) {
                    val apkName = path
                    val dexName = apkName.replace(".apk", ".dex")
                    Utils.extractAssets(mBaseContext, apkName)
                    mergeDexs(apkName, dexName)
                    with(generatePluginItem(apkName)) {
                        plugins.add(this)
                        pluginPaths.add(this.pluginPath)
                    }
                }
            }
            reloadInstalledPluginResources(pluginPaths)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generatePluginItem(apkName: String): PluginItem {
        val file = mBaseContext.getFileStreamPath(apkName)
        return PluginItem(Utils.getPackageInfo(mBaseContext, file.absolutePath), file.absolutePath)
    }

    private fun mergeDexs(apkName: String, dexName: String) {
        val dexFile = mBaseContext.getFileStreamPath(apkName)
        val optDexFile = mBaseContext.getFileStreamPath(dexName)

        BaseDexClassLoaderHookHelper.patchClassLoader(mBaseContext.classLoader, dexFile, optDexFile)
    }

    private fun reloadInstalledPluginResources(pluginPaths: ArrayList<String>) {
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath =
                AssetManager::class.java.getMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, mBaseContext.packageResourcePath)
            for (pluginPath: String in pluginPaths) {
                addAssetPath.invoke(assetManager, pluginPath)
            }
            val newResources = Resources(
                assetManager,
                mBaseContext.resources.displayMetrics,
                mBaseContext.resources.configuration
            )
            RefInvoke.setFieldObject(mBaseContext, "mResources", newResources)
            // 这是最主要的需要替换的，如果不支持插件运行时更新，只留这一个即可
            RefInvoke.setFieldObject(mPackageInfo, "mResources", newResources)
            mCurResources = newResources
            // 需要清理mTheme对象，否则通过inflate方式加载资源会报错
            // 如果是Activity动态加载插件，则需要把activity的mTheme对象也设置为null
            RefInvoke.setFieldObject(mBaseContext, "mTheme", null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPlugins(): ArrayList<PluginItem> {
        return plugins
    }

    fun getResources(): Resources {
        return mCurResources
    }
}