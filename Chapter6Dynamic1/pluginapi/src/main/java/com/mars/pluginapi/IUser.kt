package com.mars.pluginapi

/**
 * Created by geyan on 2020/11/13
 */
interface IUser {

    fun getName(): String

    fun setName(name: String)

    fun getAge(): Int

    fun setAge(age: Int)

    fun register(callback: ICallback)
}