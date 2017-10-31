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
import com.example.guohouxiao.musicalbum.adapter.InterestingAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by guohouxiao on 2017/10/14.
 * 可能感兴趣的人
 */

public class InterestingActivity extends BaseActivity {

    private static final String TAG = "InterestingActivity";

    private Toolbar mToolbar;

    private TextView tv_no_interesting;

    private RecyclerView rv_interesting;
    private LinearLayoutManager mLayoutManager;
    private InterestingAdapter mAdapter;
    private List<User> mList = new ArrayList<>();//推荐用户

    private List<User> allList = new ArrayList<>();//全部用户
    private List<User> alFollowList = new ArrayList<>();//已关注用户
    private List<User> noFollowList = new ArrayList<>();//未关注用户

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshView();
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

        tv_no_interesting = (TextView) findViewById(R.id.tv_no_interesting);
        rv_interesting = (RecyclerView) findViewById(R.id.rv_interesting);
        mLayoutManager = new LinearLayoutManager(this);
        rv_interesting.setLayoutManager(mLayoutManager);
        setupAdapter();

    }


    private void refreshView() {
        mList.clear();
        setupAdapter();
    }

    private void setupAdapter() {

        //获取全部用户列表
        AVQuery<User> allQuery = AVObject.getQuery(User.class);
        allQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> listAll, AVException e) {
                if (listAll.size() != 0) {
                    allList.addAll(listAll);
                    allList.remove(mCurrentUser);
                }
            }
        });

        //获取已关注用户列表
        try {
            AVQuery<User> followeeQuery = mCurrentUser.followeeQuery(User.class);
            followeeQuery.findInBackground(new FindCallback<User>() {
                @Override
                public void done(final List<User> listFollowee, AVException e) {
                    if (listFollowee.size() != 0) {
                        for (int i = 0; i < listFollowee.size(); i++) {
                            alFollowList.add(listFollowee.get(i));
                            try {
                                User user = AVObject.createWithoutData(User.class, listFollowee.get(i).getObjectId());
                                allList.remove(user);
                                if (i == listFollowee.size() - 1) {
                                    noFollowList.addAll(allList);

                                    //比较用户的关注列表
                                    if (noFollowList.size() != 0) {
                                        for (i = 0; i < noFollowList.size(); i++) {
                                            AVQuery<User> followeeQuery = User.followeeQuery(noFollowList.get(i).getObjectId(), User.class);
                                            final int finalI1 = i;
                                            followeeQuery.findInBackground(new FindCallback<User>() {
                                                @Override
                                                public void done(List<User> list, AVException e) {
                                                    if (list != null) {
                                                        if (list.size() != 0) {
                                                            //判断是否有相同关注
                                                            if (existSameFollow(alFollowList, list)) {
                                                                mList.add(noFollowList.get(finalI1));
                                                                mList = new ArrayList<>(new HashSet<User>(mList));//去重
                                                                if (mList != null) {
                                                                    if (mList.size() != 0) {
                                                                        mAdapter = new InterestingAdapter(InterestingActivity.this, mList);
                                                                        rv_interesting.setAdapter(mAdapter);
                                                                    } else {
                                                                        tv_no_interesting.setVisibility(View.VISIBLE);
                                                                    }
                                                                } else {
                                                                    tv_no_interesting.setVisibility(View.VISIBLE);
                                                                }
                                                            } else {
                                                                tv_no_interesting.setVisibility(View.VISIBLE);
                                                            }
                                                        } else {
                                                            tv_no_interesting.setVisibility(View.VISIBLE);
                                                        }
                                                    } else {
                                                        tv_no_interesting.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        tv_no_interesting.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (AVException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (AVException e1) {
            e1.printStackTrace();
        }

    }

    private boolean existSameFollow(List<User> currentList, List<User> otherList) {
        boolean isExist = false;
        for (int i = 0; i < otherList.size(); i++) {
            if (currentList.contains(otherList.get(i))) {
                isExist = true;
            } else {
                isExist = false;
            }
        }
        return isExist;
    }

}
