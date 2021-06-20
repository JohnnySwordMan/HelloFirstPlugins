package com.mars.hello.first.plugins.hook13

import android.os.Handler
import com.mars.components.util.reflect.FieldUtils

/**
 * Created by geyan on 2021/6/20
 */
object HookHelper {

    fun hookCallbackOfHandler() {

        /**
         * 通过反射，获取ActivityThread类中的sCurrentActivityThread
         * private static volatile ActivityThread sCurrentActivityThread;
         */
        val sCurrentActivityThread =
            FieldUtils.readStaticField("android.app.ActivityThread", "sCurrentActivityThread")

        /**
         * 获取当前ActivityThread的mH成员变量
         * final H mH = new H();
         */
        val mH = FieldUtils.readField(sCurrentActivityThread, "mH")

        /**
         * 将mH的mCallback替换成我们自定义的
         * final Callback mCallback;
         */
//        FieldUtils.writeField(mH, "mCallback", HookCallbackOfHandler(mH as Handler))

        // 反射写数据的时候，最好指明类名，即Handler::class.java，上述的写法，mH.getClass()的时候，获取到的是Object
        FieldUtils.writeField(
            Handler::class.java,
            mH,
            "mCallback",
            HookCallbackOfHandler(mH as Handler)
        )
    }
}