package com.mars.chapter5hook13;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Created by geyan on 2020/12/19
 */
class MockClass2 implements Handler.Callback {

    Handler target;

    public MockClass2(Handler base) {
        target = base;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
        }
        target.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Log.e("gy", "MockClass2---handleLaunchActivity---msg = " + msg.obj.toString());
    }
}
