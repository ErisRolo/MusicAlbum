package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.ui.activity.StatusBookmarkActivity;
import com.example.guohouxiao.musicalbum.ui.activity.StatusFollowActivity;
import com.example.guohouxiao.musicalbum.ui.activity.StatusLikeActivity;
import com.example.guohouxiao.musicalbum.ui.activity.StatusAddActivity;
import com.example.guohouxiao.musicalbum.utils.FragmentUtils;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

/**
 * Created by guohouxiao on 2017/7/18.
 * 通知
 */

public class NotificationFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fm;

    private LinearLayout ll_status;
    private LinearLayout ll_followee;
    private LinearLayout ll_like;
    private LinearLayout ll_bookmark;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);

        fm = getChildFragmentManager();

        findView(view);

        return view;
    }

    private void findView(View view) {

        FragmentUtils.addFragment(fm, R.id.fragment_container, new LCIMConversationListFragment());

        ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        ll_followee = (LinearLayout) view.findViewById(R.id.ll_followee);
        ll_like = (LinearLayout) view.findViewById(R.id.ll_like);
        ll_bookmark = (LinearLayout) view.findViewById(R.id.ll_bookmark);

        ll_status.setOnClickListener(this);
        ll_followee.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_bookmark.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_status:
                startActivity(new Intent(getActivity(), StatusAddActivity.class));
                break;
            case R.id.ll_followee:
                startActivity(new Intent(getActivity(), StatusFollowActivity.class));
                break;
            case R.id.ll_like:
                startActivity(new Intent(getActivity(), StatusLikeActivity.class));
                break;
            case R.id.ll_bookmark:
                startActivity(new Intent(getActivity(), StatusBookmarkActivity.class));
                break;
        }
    }
}
