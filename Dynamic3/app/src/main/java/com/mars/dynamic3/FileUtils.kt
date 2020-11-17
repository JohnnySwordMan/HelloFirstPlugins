package com.mars.dynamic3

import android.content.Context
import android.util.Log
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by geyan on 2020/11/11
 */
object FileUtils {

    /**
     * 模拟插件下载
     * 从assets目录下读取插件apk，复制到/data/data/包名/files目录下，即/data/user/0/com.mars.dynamic0/files/plugin1.apk
     *
     * getFileStreamPath(assetName)：返回以/data/user/0/com.mars.dynamic0/files/assetName作为路径的File
     */
    fun copyFromAssets(context: Context, assetName: String): String? {
        val destFile = context.getFileStreamPath(assetName)
        Log.e("gy", "copyFromAssets---assetName = $assetName, destFile = $destFile")
        val destFilePath = copyFromAssets(context, assetName, destFile)
        Log.e("gy", "copyFromAssets---拷贝成功，路径为$destFilePath")
        return destFilePath
    }

    fun copyFromAssets(context: Context, assetName: String, destDirPath: String): String? {
        val destDir = File(destDirPath)
        if (!destDir.exists()) {
            val mkdirsResult = destDir.mkdirs()
            if (!mkdirsResult) {
                return null
            }
        }
        val destFile = File("${destDir.absoluteFile}/$assetName")
        return copyFromAssets(context, assetName, destFile)
    }


    fun copyFromAssets(context: Context, assetName: String, destFile: File): String? {
        if (destFile.exists()) {
            return destFile.absolutePath
        }
        val result = tryCopyFile(context, assetName, destFile)
        if (result) {
            return destFile.absolutePath
        }
        return null
    }

    private fun tryCopyFile(context: Context, assetName: String, destFile: File): Boolean {
        val assetManager = context.assets
        try {
            return copyStream(assetManager.open(assetName), FileOutputStream(destFile))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun copyStream(inputStream: InputStream, outputStream: FileOutputStream): Boolean {
        val buffer = ByteArray(1024)
        var count = 0
        try {
            while (inputStream.read(buffer).also { count = it } > 0) {
                outputStream.write(buffer, 0, count)
            }
            outputStream.flush()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeSilently(inputStream);
            closeSilently(outputStream);
        }
        return false
    }

    private fun closeSilently(closeable: Closeable?) {
        if (closeable == null) {
            return
        }
        try {
            closeable.close()
        } catch (e: Throwable) {
            // ignore
        }
    }
}