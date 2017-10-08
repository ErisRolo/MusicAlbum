package com.example.guohouxiao.musicalbum.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.InboxStatusFindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.StatusFollowAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.StatusFollow;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guohouxiao on 2017/9/28.
 * 关注通知
 */

public class StatusFollowActivity extends BaseActivity {

    private Toolbar mToolbar;

    private TextView tv_no_status;

    private RecyclerView rv_status;
    private LinearLayoutManager mLayoutManager;
    private StatusFollowAdapter mAdapter;
    private List<StatusFollow> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_follow);

        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_no_status = (TextView) findViewById(R.id.tv_no_status);
        rv_status = (RecyclerView) findViewById(R.id.rv_status);
        mLayoutManager = new LinearLayoutManager(this);
        rv_status.setLayoutManager(mLayoutManager);
        setupAdapter();

    }

    private void setupAdapter() {
        AVStatusQuery indexQuery = AVStatus.inboxQuery(mCurrentUser, AVStatus.INBOX_TYPE.PRIVATE.toString());
        indexQuery.setLimit(50);
        indexQuery.findInBackground(new InboxStatusFindCallback() {
            @Override
            public void done(List<AVStatus> list, AVException e) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        AVStatus status = list.get(i);
                        Map<String, Object> data = status.getData();
                        if (((String) data.get("type")).equals("StatusFollow")) {
                            StatusFollow statusFollow = new StatusFollow();
                            statusFollow.setAvatar((String) data.get(Config.AVATAR));
                            statusFollow.setNickname((String) data.get(Config.NICKNAME));
                            statusFollow.setUserId((String) data.get(Config.USERID));
                            mList.add(statusFollow);
                            mAdapter = new StatusFollowAdapter(StatusFollowActivity.this, mList);
                            rv_status.setAdapter(mAdapter);
                        }
                    }
                } else {
                    tv_no_status.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
