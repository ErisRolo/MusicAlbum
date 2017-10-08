package com.example.guohouxiao.musicalbum.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.guohouxiao.musicalbum.bean.User;

/**
 * Created by guohouxiao on 2017/8/31.
 * Fragment基类
 */

public class BaseFragment extends Fragment {

    protected User mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUser = User.getCurrentUser(User.class);

    }
}
