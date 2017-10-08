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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/21.
 * 收藏的相册列表
 */

public class BookmarkAlbumActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_bookmark_album;
    private LinearLayoutManager mLayoutManager;
    private AlbumAdapter mAdapter;
    private List<MusicAlbum> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_album);

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

        rv_bookmark_album = (RecyclerView) findViewById(R.id.rv_bookmark_album);
        mLayoutManager = new LinearLayoutManager(this);
        rv_bookmark_album.setLayoutManager(mLayoutManager);

        setBookmarkAdapter();

    }

    private void refreshView() {
        mList.clear();
        setBookmarkAdapter();
    }

    //获取数据并设置适配器
    private void setBookmarkAdapter() {
        if (mCurrentUser.getBookmarkAlbum() != null) {
            if (mCurrentUser.getBookmarkAlbum().size() != 0) {
                for (int i = 0; i < mCurrentUser.getBookmarkAlbum().size(); i++) {
                    AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                    query.whereEqualTo("objectId", mCurrentUser.getBookmarkAlbum().get(i));
                    query.findInBackground(new FindCallback<MusicAlbum>() {
                        @Override
                        public void done(List<MusicAlbum> list, AVException e) {
                            if (list != null) {
                                if (list.size() != 0) {
                                    for (int j = 0; j < list.size(); j++) {
                                        mList.add(list.get(j));
                                        mAdapter = new AlbumAdapter(BookmarkAlbumActivity.this, mList);
                                        rv_bookmark_album.setAdapter(mAdapter);
                                    }
                                }
                            }
                        }
                    });
                }
            } else {
                rv_bookmark_album.removeAllViews();
            }
        }

    }

}
