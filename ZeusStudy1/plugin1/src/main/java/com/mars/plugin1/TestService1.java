package com.mars.plugin1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by mars on 2020/11/3
 */
public class TestService1 extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("gy", "TestService1---onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("gy", "TestService1---onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("gy", "TestService1---onDestroy");
    }
}
