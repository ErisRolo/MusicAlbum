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
import com.example.guohouxiao.musicalbum.adapter.AlbumAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/10/4.
 * 相关相册
 */

public class SearchAlbumActivity extends BaseActivity {

    private static final String SEARCH_RESULT = "searchresult";
    private String search;

    private Toolbar mToolbar;

    private RecyclerView rv_album;
    private TextView tv_nosearch_album;
    private LinearLayoutManager mLayoutManager;
    private AlbumAdapter mAdapter;
    private List<MusicAlbum> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_album);

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

        rv_album = (RecyclerView) findViewById(R.id.rv_album);
        tv_nosearch_album = (TextView) findViewById(R.id.tv_nosearch_album);
        mLayoutManager = new LinearLayoutManager(this);
        rv_album.setLayoutManager(mLayoutManager);
        setupAdapter();

    }

    private void setupAdapter() {

        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        query.whereContains(Config.ALBUMNAME, search);
        query.whereEqualTo(Config.ISPUBLIC, true);
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        mList.add(list.get(i));
                        mAdapter = new AlbumAdapter(SearchAlbumActivity.this, mList);
                        rv_album.setAdapter(mAdapter);
                    }
                } else {
                    tv_nosearch_album.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
