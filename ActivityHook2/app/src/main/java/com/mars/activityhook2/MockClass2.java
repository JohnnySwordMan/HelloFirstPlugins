package com.mars.activityhook2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mars.activityhook2.util.RefInvoke;

/**
 * Created by geyan on 2020/10/31
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
        Object obj = msg.obj;
        Intent raw = (Intent) RefInvoke.getFieldObject(obj, "intent");
        Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        raw.setComponent(target.getComponent());
        Log.e("gy", " hook ActivityThread success");
    }
}
