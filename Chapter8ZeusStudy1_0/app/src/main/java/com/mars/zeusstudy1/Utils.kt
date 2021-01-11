package com.mars.zeusstudy1

import android.content.Context
import java.io.Closeable
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by mars on 2020/11/3
 */
object Utils {

    /**
     * 模拟插件下载
     * 从assets目录下读取插件apk，复制到/data/data/com.mars.zeusstudy1/files目录下
     */
    fun extractAssets(context: Context, sourceName: String) {
        val assetManager = context.assets
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            inputStream = assetManager.open(sourceName)
            val extractFile = context.getFileStreamPath(sourceName)
            outputStream = FileOutputStream(extractFile)
            val buffer = ByteArray(1024)
            var count = 0
            while (inputStream.read(buffer).also { count = it } > 0) {
                outputStream.write(buffer, 0, count)
            }
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeSilently(inputStream)
            closeSilently(outputStream)
        }
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