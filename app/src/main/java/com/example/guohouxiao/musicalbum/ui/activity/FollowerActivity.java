package com.example.guohouxiao.musicalbum.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.UserAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/1.
 * 粉丝列表
 */

public class FollowerActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private String userId;

    private Toolbar mToolbar;

    private RecyclerView rv_follower;
    private LinearLayoutManager layoutManager;
    private UserAdapter adapter;
    private List<User> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        userId = getIntent().getStringExtra(USER_ID);

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

        rv_follower = (RecyclerView) findViewById(R.id.rv_follower);
        layoutManager = new LinearLayoutManager(this);
        rv_follower.setLayoutManager(layoutManager);

        //判断是当前用户的关注列表还是通过其他用户的个人中心界面跳转的粉丝列表
        //如果传来的id为空，则证明为当前用户
        if (userId == null) {
            try {
                AVQuery<User> followerQuery = mCurrentUser.followerQuery(User.class);
                followerQuery.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> list, AVException e) {
                        if (list != null) {
                            if (list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    AVQuery<User> query = AVObject.getQuery(User.class);
                                    query.whereEqualTo("objectId", list.get(i).getObjectId());
                                    query.findInBackground(new FindCallback<User>() {
                                        @Override
                                        public void done(List<User> list, AVException e) {
                                            if (list != null) {
                                                if (list.size() != 0) {
                                                    mList.add(list.get(0));
                                                    adapter = new UserAdapter(FollowerActivity.this, mList);
                                                    rv_follower.setAdapter(adapter);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            } catch (AVException e) {
                e.printStackTrace();
            }
        } else {
            AVQuery<User> followerQuery = User.followerQuery(userId, User.class);
            followerQuery.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                AVQuery<User> query = AVObject.getQuery(User.class);
                                query.whereEqualTo("objectId", list.get(i).getObjectId());
                                query.findInBackground(new FindCallback<User>() {
                                    @Override
                                    public void done(List<User> list, AVException e) {
                                        if (list != null) {
                                            if (list.size() != 0) {
                                                mList.add(list.get(0));
                                                adapter = new UserAdapter(FollowerActivity.this, mList);
                                                rv_follower.setAdapter(adapter);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

}
