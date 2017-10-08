package com.example.guohouxiao.musicalbum.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.guohouxiao.musicalbum.bean.User;

/**
 * Created by guohouxiao on 2017/7/15.
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    protected User mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUser = User.getCurrentUser(User.class);

    }

}
