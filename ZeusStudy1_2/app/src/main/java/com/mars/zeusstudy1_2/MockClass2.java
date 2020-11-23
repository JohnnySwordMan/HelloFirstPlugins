package com.mars.zeusstudy1_2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mars.pluginservice.RefInvoke;

/**
 * Created by geyan on 2020/11/23
 */
public class MockClass2 implements Handler.Callback {

    Handler target;

    public MockClass2(Handler base) {
        target = base;
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            // ActivityThread里的LAUNCH_ACTIVITY的值是100
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
        Log.e("gy", "MockClass2---newIntent = " + raw);
        Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        Log.e("gy", "MockClass2---targetIntent = " + target);
        if (target != null) {
            raw.setComponent(target.getComponent());
        }
    }
}
