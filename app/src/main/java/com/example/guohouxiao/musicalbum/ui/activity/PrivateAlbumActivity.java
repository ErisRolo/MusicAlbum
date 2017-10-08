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
 * 私有相册列表
 */

public class PrivateAlbumActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_private_album;
    private LinearLayoutManager mLayoutManager;
    private AlbumAdapter mAdapter;
    private List<MusicAlbum> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_album);

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

        rv_private_album = (RecyclerView) findViewById(R.id.rv_private_album);
        mLayoutManager = new LinearLayoutManager(this);
        rv_private_album.setLayoutManager(mLayoutManager);

        setPrivateAdapter();

    }

    private void refreshView() {
        mList.clear();
        setPrivateAdapter();
    }

    //获取数据并设置适配器
    private void setPrivateAdapter() {
        AVQuery<MusicAlbum> queryUser = AVObject.getQuery(MusicAlbum.class);
        queryUser.whereEqualTo(Config.USERID, mCurrentUser.getObjectId());
        AVQuery<MusicAlbum> queryIsPublic = AVObject.getQuery(MusicAlbum.class);
        queryIsPublic.whereEqualTo(Config.ISPUBLIC, false);
        AVQuery<MusicAlbum> query = AVQuery.and(Arrays.asList(queryUser, queryIsPublic));
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mList.add(list.get(i));
                            mAdapter = new AlbumAdapter(PrivateAlbumActivity.this, mList);
                            rv_private_album.setAdapter(mAdapter);
                        }
                    } else {
                        rv_private_album.removeAllViews();
                    }
                }
            }
        });
    }
}
