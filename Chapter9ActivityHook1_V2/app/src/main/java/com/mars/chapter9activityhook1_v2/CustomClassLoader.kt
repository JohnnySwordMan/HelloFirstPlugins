package com.mars.chapter9activityhook1_v2

import android.util.Log
import dalvik.system.DexClassLoader

/**
 * Created by geyan on 2021/1/17
 */
class CustomClassLoader(
    private val dexPath: String,
    private val optimizedDirectory: String,
    private val librarySearchPath: String,
    private val parentClassLoader: ClassLoader? = null
) : DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parentClassLoader) {

    init {
        Log.e(
            "gy", "CustomClassLoader---dexPath = $dexPath, " +
                    "optimizedDirectory = $optimizedDirectory, " +
                    "librarySearchPath = $librarySearchPath, " +
                    "parentClassLoader = $parentClassLoader"
        )
    }
}