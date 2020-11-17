package com.mars.plugin2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by geyan on 2020/11/18
 */
public class UIUtil {
    public static String getTextString(Context context){
        return context.getResources().getString(R.string.hello_message);
    }

    public static Drawable getImageDrawable(Context context){
        return context.getResources().getDrawable(R.drawable.robert);
    }

    public static View getLayout(Context context){
        return LayoutInflater.from(context).inflate(R.layout.activity_main, null);
    }
}