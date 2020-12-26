package com.mars.chapter5hook31;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

/**
 * Created by geyan on 2020/12/26
 */
class MarsInstrumentation extends Instrumentation {

    Instrumentation base;

    public MarsInstrumentation(Instrumentation instrumentation) {
        base = instrumentation;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Intent originIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        // 为空，表示要启动的Activity是在AndroidManifest中声明了
        if (originIntent == null) {
            return base.newActivity(cl, className, intent);
        }
        String newClassName = originIntent.getComponent().getClassName();
        return base.newActivity(cl, newClassName, originIntent);
    }
}
