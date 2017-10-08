package com.example.guohouxiao.musicalbum;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.guohouxiao.musicalbum.bean.CustomUserProvider;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.API;

import cn.leancloud.chatkit.LCChatKit;


/**
 * Created by guohouxiao on 2017/7/15.
 * Application
 * 全局context
 * 初始化各种API
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        AVUser.registerSubclass(User.class);
        AVUser.alwaysUseSubUserClass(User.class);

        AVObject.registerSubclass(MusicAlbum.class);

        //初始化LeanCloud
        AVOSCloud.initialize(this, API.AVOS_APP_ID, API.AVOS_APP_KAY);

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), API.AVOS_APP_ID, API.AVOS_APP_KAY);

    }

    public static Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
