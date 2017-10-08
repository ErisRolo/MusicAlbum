package com.example.guohouxiao.musicalbum.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.guohouxiao.musicalbum.R;

/**
 * Created by guohouxiao on 2017/7/19.
 * Fragment的add和replace封装
 */

public class FragmentUtils {

    public static void addFragment(FragmentManager mFragmentManager, int frameId,
                                   Fragment mFragment) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(frameId, mFragment);
        mFragmentTransaction.commit();
    }

    public static void replaceFragment(FragmentManager mFragmentManager, int frameId,
                                       Fragment mFragment) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(frameId, mFragment);
        mFragmentTransaction.commit();
    }

}
