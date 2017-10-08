package com.example.guohouxiao.musicalbum.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by guohouxiao on 2017/7/15.
 * 提示框封装
 */

public class ShowToast {

    public static void showShortToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}
