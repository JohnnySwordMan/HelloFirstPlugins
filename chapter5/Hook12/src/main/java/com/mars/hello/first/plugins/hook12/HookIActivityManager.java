package com.mars.hello.first.plugins.hook12;

import com.mars.hello.first.plugins.common.base.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2021/6/20
 */
public class HookIActivityManager implements InvocationHandler {

    private static final String TAG = "HookIActivityManager";

    Object mIActivityManager;

    public HookIActivityManager(Object obj) {
        mIActivityManager = obj;
    }

    /**
     * 拦截AMN的getDefault方法的startActivity
     * 即，拦截IActivityManager实现类的startActivity
     * 即，拦截AMP的startActivity
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Logger.e(TAG, "start hook startActivity");
        }
        return method.invoke(mIActivityManager, args);
    }
}
