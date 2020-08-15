/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.parking.Navi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;



public class NormalUtils {


    public static String getTTSAppID() {
        return "11213224";
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
