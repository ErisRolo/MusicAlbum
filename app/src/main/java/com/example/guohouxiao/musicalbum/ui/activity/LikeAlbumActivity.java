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
import com.example.guohouxiao.musicalbum.adapter.AlbumAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/21.
 * 喜欢的相册列表
 */

public class LikeAlbumActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private String userId;

    private Toolbar mToolbar;

    private RecyclerView rv_like_album;
    private LinearLayoutManager mLayoutManager;
    private AlbumAdapter mAdapter;
    private List<MusicAlbum> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_album);

        userId = getIntent().getStringExtra(USER_ID);

        initView();
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

        rv_like_album = (RecyclerView) findViewById(R.id.rv_like_album);
        mLayoutManager = new LinearLayoutManager(this);
        rv_like_album.setLayoutManager(mLayoutManager);

        setLikeAdapter();

    }

    private void refreshView() {
        mList.clear();
        setLikeAdapter();
    }

    //获得数据并设置适配器
    private void setLikeAdapter() {
        //判断是当前用户的喜欢列表还是通过其他用户的个人中心界面跳转的喜欢列表
        //如果传来的id为空，则证明为当前用户
        if (userId == null) {
            if (mCurrentUser.getLikeAlbum() != null) {
                if (mCurrentUser.getLikeAlbum().size() != 0) {
                    for (int i = 0; i < mCurrentUser.getLikeAlbum().size(); i++) {
                        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                        query.whereEqualTo("objectId", mCurrentUser.getLikeAlbum().get(i));
                        query.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                if (list != null) {
                                    if (list.size() != 0) {
                                        for (int j = 0; j < list.size(); j++) {
                                            mList.add(list.get(j));
                                            mAdapter = new AlbumAdapter(LikeAlbumActivity.this, mList);
                                            rv_like_album.setAdapter(mAdapter);
                                        }
                                    }
                                }
                            }
                        });
                    }
                } else {
                    rv_like_album.removeAllViews();
                }
            }
        } else {
            AVQuery<User> query = AVObject.getQuery(User.class);
            query.whereEqualTo("objectId", userId);
            query.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> list, AVException e) {
                    if (list.get(0).getLikeAlbum() != null) {
                        if (list.get(0).getLikeAlbum().size() != 0) {
                            for (int i = 0; i < list.get(0).getLikeAlbum().size(); i++) {
                                AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                                query.whereEqualTo("objectId", list.get(0).getLikeAlbum().get(i));
                                query.findInBackground(new FindCallback<MusicAlbum>() {
                                    @Override
                                    public void done(List<MusicAlbum> list, AVException e) {
                                        if (list != null) {
                                            if (list.size() != 0) {
                                                for (int j = 0; j < list.size(); j++) {
                                                    mList.add(list.get(j));
                                                    mAdapter = new AlbumAdapter(LikeAlbumActivity.this, mList);
                                                    rv_like_album.setAdapter(mAdapter);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            rv_like_album.removeAllViews();
                        }
                    }
                }
            });
        }
    }

}
