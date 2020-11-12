package com.mars.plugin

import com.mars.pluginapi.ICallback
import com.mars.pluginapi.IUser

/**
 * Created by geyan on 2020/11/13
 */
class User : IUser {

    private var name = "mars"
    private var age = 27
    private var mCallback: ICallback? = null

    override fun getName(): String {
        return name
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun getAge(): Int {
        return age
    }

    override fun setAge(age: Int) {
        this.age = age
    }

    override fun register(callback: ICallback) {
        this.mCallback = callback

        clickButton()
    }

    private fun clickButton() {
        mCallback?.sendResult("Hello, this is plugin")
    }


}