package com.mars.zeusstudy1_3;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.mars.components.util.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geyan on 2021/1/19
 */
public class ActivityThreadHook implements Handler.Callback {

    private Handler target;

    public ActivityThreadHook(Handler base) {
        target = base;
    }

    /**
     * 详见ActivityThread#handleMessage方法
     */
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
            case 112:
                handleNewIntent(msg);
                break;
        }
        target.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Object obj = msg.obj;
        Intent intent = (Intent) FieldUtils.readField(obj, "intent");
        Intent targetIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        // 如果Hook操作是在Application中调用的，那么targetIntent需要判空；如果放在MainActivity中执行，则无需判空
        if (targetIntent == null) {
            return;
        }
//        intent.setComponent(targetIntent.getComponent());
        FieldUtils.writeField(obj, "intent", targetIntent);
    }

    private void handleNewIntent(Message msg) {
        Object obj = msg.obj;
        List intents = (ArrayList) FieldUtils.readField(obj, "intents");

        for (Object object : intents) {
            Intent raw = (Intent) object;
            Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
            if (target != null) {
                raw.setComponent(target.getComponent());
                if (target.getExtras() != null) {
                    raw.putExtras(target.getExtras());
                }
                break;
            }
        }
    }
}
