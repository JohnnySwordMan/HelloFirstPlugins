package com.mars.activityhook1;

import android.app.Application;
import android.content.Context;

/**
 * Created by geyan on 2020/10/27
 */
public class HostApplication extends Application {

    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
    }

    public static Context getContext() {
        return mContext;
    }
}
