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
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/25.
 * 公开相册列表
 */

public class PublicAlbumActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private String userId;

    private Toolbar mToolbar;

    private RecyclerView rv_public_album;
    private LinearLayoutManager mLayoutManager;
    private AlbumAdapter mAdapter;
    private List<MusicAlbum> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_album);

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

        rv_public_album = (RecyclerView) findViewById(R.id.rv_public_album);
        mLayoutManager = new LinearLayoutManager(this);
        rv_public_album.setLayoutManager(mLayoutManager);

        setPublicAdapter();

    }

    private void refreshView() {
        mList.clear();
        setPublicAdapter();
    }

    //获取数据并设置适配器
    private void setPublicAdapter() {
        //判断是当前用户的喜欢列表还是通过其他用户的个人中心界面跳转的公开相册列表
        //如果传来的id为空，则证明为当前用户
        if (userId == null) {
            AVQuery<MusicAlbum> queryUser = AVObject.getQuery(MusicAlbum.class);
            queryUser.whereEqualTo(Config.USERID, mCurrentUser.getObjectId());
            AVQuery<MusicAlbum> queryIsPublic = AVObject.getQuery(MusicAlbum.class);
            queryIsPublic.whereEqualTo(Config.ISPUBLIC, true);
            AVQuery<MusicAlbum> query = AVQuery.and(Arrays.asList(queryUser, queryIsPublic));
            query.findInBackground(new FindCallback<MusicAlbum>() {
                @Override
                public void done(List<MusicAlbum> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i));
                                mAdapter = new AlbumAdapter(PublicAlbumActivity.this, mList);
                                rv_public_album.setAdapter(mAdapter);
                            }
                        } else {
                            rv_public_album.removeAllViews();
                        }
                    }
                }
            });
        } else {
            AVQuery<MusicAlbum> queryUser = AVObject.getQuery(MusicAlbum.class);
            queryUser.whereEqualTo(Config.USERID, userId);
            AVQuery<MusicAlbum> queryIsPublic = AVObject.getQuery(MusicAlbum.class);
            queryIsPublic.whereEqualTo(Config.ISPUBLIC, true);
            AVQuery<MusicAlbum> query = AVQuery.and(Arrays.asList(queryUser, queryIsPublic));
            query.findInBackground(new FindCallback<MusicAlbum>() {
                @Override
                public void done(List<MusicAlbum> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i));
                                mAdapter = new AlbumAdapter(PublicAlbumActivity.this, mList);
                                rv_public_album.setAdapter(mAdapter);
                            }
                        } else {
                            rv_public_album.removeAllViews();
                        }
                    }
                }
            });
        }
    }
}
