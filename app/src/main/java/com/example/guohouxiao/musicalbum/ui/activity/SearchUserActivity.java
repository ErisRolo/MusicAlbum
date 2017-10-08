package com.example.guohouxiao.musicalbum.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.UserAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/10/4.
 * 相关用户
 */

public class SearchUserActivity extends BaseActivity {

    private static final String SEARCH_RESULT = "searchresult";
    private String search;

    private Toolbar mToolbar;

    private RecyclerView rv_user;
    private TextView tv_nosearch_user;
    private LinearLayoutManager mLayoutManager;
    private UserAdapter mAdapter;
    private List<User> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        search = getIntent().getStringExtra(SEARCH_RESULT);

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

        rv_user = (RecyclerView) findViewById(R.id.rv_user);
        tv_nosearch_user = (TextView) findViewById(R.id.tv_nosearch_user);
        mLayoutManager = new LinearLayoutManager(this);
        rv_user.setLayoutManager(mLayoutManager);
        setupAdapter();

    }

    private void setupAdapter() {

        AVQuery<User> query = AVObject.getQuery(User.class);
        query.whereContains(Config.NICKNAME, search);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        mList.add(list.get(i));
                        mAdapter = new UserAdapter(SearchUserActivity.this, mList);
                        rv_user.setAdapter(mAdapter);
                    }
                } else {
                    tv_nosearch_user.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
