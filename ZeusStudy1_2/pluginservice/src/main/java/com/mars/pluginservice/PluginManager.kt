package com.mars.pluginservice

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.mars.pluginservice.model.PluginItem
import java.util.ArrayList

/**
 * Created by geyan on 2020/11/21
 */
object PluginManager {

    // 原始的application的BaseContext，不能是其他的，否则可能会泄露
    @Volatile
    private lateinit var mBaseContext: Context

    // 当前正在使用的Resources
    @Volatile
    public lateinit var mCurResources: Resources

    // ContextImpl中的LoadedApk对象的mPackageInfo
    private lateinit var mPackageInfo: Any

    private val plugins = arrayListOf<PluginItem>()

    /**
     * 1. 将所有插件dex合并到宿主
     */
    fun init(application: Application) {
        mBaseContext = application.baseContext
        mCurResources = mBaseContext.resources
        mPackageInfo = RefInvoke.getFieldObject(mBaseContext, "mPackageInfo")
        val assetManager = application.assets
        val paths = assetManager.list("")
        if (paths == null || paths.isEmpty()) {
            return
        }
        val pluginPaths = arrayListOf<String>()
        for (path: String in paths) {
            if (path.endsWith(".apk")) {
                val apkName = path
                val dexName = apkName.replace(".apk", ".dex")
                // 模拟插件的下载
                FileUtils.copyFromAssets(mBaseContext, apkName)
                mergeDexs(apkName, dexName)
                with(generatePluginItem(apkName)) {
                    plugins.add(this)
                    pluginPaths.add(this.pluginPath)
                }
            }
        }
        reloadInstalledPluginResources(pluginPaths)
    }


    private fun reloadInstalledPluginResources(pluginPaths: ArrayList<String>) {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMethod = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
        // 宿主资源
        addAssetPathMethod.invoke(assetManager, mBaseContext.packageResourcePath)
        // 将所有插件apk都添加到AssetManager
        for (pluginPath: String in pluginPaths) {
            addAssetPathMethod.invoke(assetManager, pluginPath)
        }
        val newResources = Resources(assetManager, mBaseContext.resources.displayMetrics, mBaseContext.resources.configuration)
        // ContextImpl的mResources字段重新赋值
        RefInvoke.setFieldObject(mBaseContext, "mResources", newResources)
        // 这是最主要的需要替换的，如果不支持插件运行时更新，只留这一个即可
        RefInvoke.setFieldObject(mPackageInfo, "mResources", newResources)
        mCurResources = newResources
        // 需要清理mTheme对象，否则通过inflate方式加载资源会报错
        // 如果是Activity动态加载插件，则需要把activity的mTheme对象也设置为null
//        RefInvoke.setFieldObject(mBaseContext, "mTheme", null)
    }

    private fun mergeDexs(apkName: String, dexName: String) {
        // apk存放的具体地址
        val dexFile = mBaseContext.getFileStreamPath(apkName)
        // dex优化之后存放的地址
        val optDexFile = mBaseContext.getFileStreamPath(dexName)
        BaseDexClassLoaderHookHelper.pathClassLoader(mBaseContext.classLoader, dexFile, optDexFile)
    }

    private fun generatePluginItem(apkName: String): PluginItem {
        val apkFile = mBaseContext.getFileStreamPath(apkName)
        val packageInfo = AppUtils.getPackageInfo(mBaseContext, apkFile.absolutePath)
        return PluginItem(packageInfo!!, apkFile.absolutePath)
    }

    fun getPlugins(): ArrayList<PluginItem> {
        return plugins
    }

    fun getResources(): Resources {
        return mCurResources
    }
}