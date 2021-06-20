package com.mars.hello.first.plugins.hook13;

import android.os.Handler;
import android.os.Message;

import com.mars.hello.first.plugins.common.base.Logger;

/**
 * Created by geyan on 2021/6/20
 */
public class HookCallbackOfHandler implements Handler.Callback {

    private static final String TAG = "HookCallbackOfHandler";

    Handler mHandler;

    public HookCallbackOfHandler(Handler handler) {
        Logger.e(TAG, "invoke <init>");
        mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
        }
        Logger.e(TAG, "invoke handleMessage msg = " + msg.obj);
        mHandler.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Logger.e(TAG, "invoke handleLaunchActivity msg = " + msg.obj);
    }
}
