package com.example.guohouxiao.musicalbum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;

/**
 * Created by guohouxiao on 2017/7/18.
 * 发现
 */

public class FindFragment extends BaseFragment {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private MymapFragment mMymapFragment;
    private SearchFragment mSearchFragment;

    private FloatingActionButton fab_switch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, null);

        fm = getChildFragmentManager();

        if (savedInstanceState == null) {
            //mMymapFragment = new MymapFragment();
            mSearchFragment = new SearchFragment();
            ft = fm.beginTransaction();
            //ft.add(R.id.fragment_container, mMymapFragment);
            ft.add(R.id.fragment_container, mSearchFragment);
            ft.commit();
            findView(view);
        } else {
            findView(view);
        }

        return view;
    }

    private void findView(View view) {
        fab_switch = (FloatingActionButton) view.findViewById(R.id.fab_switch);
        fab_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = fm.findFragmentById(R.id.fragment_container);
                String fragment_type = fragment.getClass().getName().trim();
                if (fragment_type.equals("com.example.guohouxiao.musicalbum.ui.fragment.MymapFragment")) {
                    mSearchFragment = new SearchFragment();
                    ft = fm.beginTransaction();
                    //ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                    ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
                    ft.replace(R.id.fragment_container, mSearchFragment);
                    ft.commit();
                }
                if (fragment_type.equals("com.example.guohouxiao.musicalbum.ui.fragment.SearchFragment")) {
                    mMymapFragment = new MymapFragment();
                    ft = fm.beginTransaction();
                    //ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
                    ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                    ft.replace(R.id.fragment_container, mMymapFragment);
                    ft.commit();
                }
            }
        });
    }

}
