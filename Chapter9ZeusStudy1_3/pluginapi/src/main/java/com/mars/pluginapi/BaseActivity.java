package com.mars.pluginapi;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Created by geyan on 2021/1/19
 */
public class BaseActivity extends Activity {

    @Override
    public Resources getResources() {
        return PluginManager.mCurResources;
    }
}
