package com.mars.chapter5hook31;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by geyan on 2020/12/26
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
        Intent intent = (Intent) FieldUtils.getFieldObject(obj, "intent");
        Intent targetIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        if (targetIntent != null) {
            intent.setComponent(targetIntent.getComponent());
        }
    }
}
