package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guohouxiao on 2017/9/14.
 * 相册详情界面
 * 可以查看具体信息、播放以及喜欢收藏
 */

/**
 * 喜欢
 * 用户表加一个喜欢的相册列表
 * 一个收藏的相册列表
 * 相册表加一个喜欢该相册的用户数
 * <p>
 * 先判断该相册是否在用户的列表内
 */

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AlbumActivity";

    private static final String OTHER_USER_ID = "other_user_id";
    private String author_id;

    private static final String ALBUM_ID = "album_id";
    private static final String ALBUM_NAME = "album_name";
    private static final String PLAYMODE = "playmode";
    private String album_id;
    private String album_name;
    private String playmode;

    private static final String GO_PLACE = "goplace";
    private static final String GO_LATITUDE = "golatitude";
    private static final String GO_LONGITUDE = "golongitude";
    private String go_place;
    private AVGeoPoint go_location;
    private double latitude;
    private double longitude;


    private Toolbar mToolbar;

    private TextView tv_album_name;
    private ImageView iv_album_cover;
    private TextView tv_album_author;
    private TextView tv_album_place;
    private LinearLayout ll_play;
    private LinearLayout ll_go;
    private ImageView iv_bookmark;
    private ImageView iv_like;
    private TextView tv_like_number;

    private Switch mSwitch;
    private boolean isPublic;

    private boolean isBookmarked = false;
    private boolean isLiked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        album_id = getIntent().getStringExtra(ALBUM_ID);

        initView();

        isBookmark();
        isLike();

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

        tv_album_name = (TextView) findViewById(R.id.tv_album_name);
        iv_album_cover = (ImageView) findViewById(R.id.iv_album_cover);
        tv_album_author = (TextView) findViewById(R.id.tv_album_author);
        tv_album_place = (TextView) findViewById(R.id.tv_album_place);
        tv_like_number = (TextView) findViewById(R.id.tv_like_number);

        ll_play = (LinearLayout) findViewById(R.id.ll_play);
        ll_go = (LinearLayout) findViewById(R.id.ll_go);
        iv_bookmark = (ImageView) findViewById(R.id.iv_bookmark);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        mSwitch = (Switch) findViewById(R.id.mSwitch);

        tv_album_author.setOnClickListener(this);
        ll_play.setOnClickListener(this);
        ll_go.setOnClickListener(this);
        iv_bookmark.setOnClickListener(this);
        iv_like.setOnClickListener(this);

        AVQuery<MusicAlbum> queryAlbum = AVObject.getQuery(MusicAlbum.class);
        queryAlbum.whereEqualTo("objectId", album_id);
        queryAlbum.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(final List<MusicAlbum> listAlbum, AVException e) {

                author_id = listAlbum.get(0).getUserId();

                album_name = listAlbum.get(0).getAlbumname();
                tv_album_name.setText(album_name);
                Glide.with(AlbumActivity.this).load(listAlbum.get(0).getCover()).into(iv_album_cover);
                tv_album_place.setText(listAlbum.get(0).getPlace());
                tv_like_number.setText(listAlbum.get(0).getLikenumber());
                playmode = listAlbum.get(0).getPlaymode();

                if (listAlbum.get(0).getUserId().equals(mCurrentUser.getObjectId())) {
                    mSwitch.setVisibility(View.VISIBLE);
                    isPublic = listAlbum.get(0).getIsPublic();
                    mSwitch.setChecked(isPublic);
                    mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                isPublic = isChecked;
                                listAlbum.get(0).setIsPublic(isPublic);
                                listAlbum.get(0).saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        ShowToast.showShortToast(AlbumActivity.this, "您已将该相册设为所有人可见。");
                                    }
                                });
                            } else {
                                isPublic = isChecked;
                                listAlbum.get(0).setIsPublic(isPublic);
                                listAlbum.get(0).saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        ShowToast.showShortToast(AlbumActivity.this, "您已将该相册设为仅自己可见。");
                                    }
                                });
                            }
                        }
                    });
                } else {
                    mSwitch.setVisibility(View.GONE);
                }

                go_place = listAlbum.get(0).getPlace();
                go_location = listAlbum.get(0).getWhereCreated();
                latitude = go_location.getLatitude();
                longitude = go_location.getLongitude();

                AVQuery<User> queryUser = AVObject.getQuery(User.class);
                queryUser.whereEqualTo("objectId", listAlbum.get(0).getUserId());
                queryUser.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> listUser, AVException e) {
                        if (listUser != null) {
                            if (listUser.size() != 0) {
                                tv_album_author.setText(listUser.get(0).getNickname());
                            }
                        }
                    }
                });
            }
        });

    }

    //判断当前相册是否被当前用户收藏
    private void isBookmark() {
        if (mCurrentUser.getBookmarkAlbum() != null) {
            if (mCurrentUser.getBookmarkAlbum().contains(album_id)) {
                isBookmarked = true;
                iv_bookmark.setImageResource(R.drawable.ic_user_bookmarked);
            } else {
                isBookmarked = false;
                iv_bookmark.setImageResource(R.drawable.ic_user_bookmark);
            }
        }
    }

    //判断当前相册是否被当前用户喜欢
    private void isLike() {
        if (mCurrentUser.getLikeAlbum() != null) {
            if (mCurrentUser.getLikeAlbum().contains(album_id)) {
                isLiked = true;
                iv_like.setImageResource(R.drawable.ic_user_liked);
            } else {
                isLiked = false;
                iv_like.setImageResource(R.drawable.ic_user_like);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album_author:
                Intent intent_author = new Intent(this, OtherUserActivity.class);
                intent_author.putExtra(OTHER_USER_ID, author_id);
                startActivity(intent_author);
                break;
            case R.id.ll_play:
                Intent intent_play = new Intent(this, PlayActivity.class);
                intent_play.putExtra(ALBUM_ID, album_id);
                intent_play.putExtra(ALBUM_NAME, album_name);
                intent_play.putExtra(PLAYMODE, playmode);
                startActivity(intent_play);
                break;
            case R.id.ll_go:
                Intent intent_go = new Intent(this, GoActivity.class);
                intent_go.putExtra(GO_PLACE, go_place);
                intent_go.putExtra(GO_LATITUDE, latitude);
                intent_go.putExtra(GO_LONGITUDE, longitude);
                startActivity(intent_go);
                break;
            case R.id.iv_bookmark:
                //如果已经收藏，点击则为取消收藏
                if (isBookmarked) {
                    isBookmarked = false;
                    iv_bookmark.setImageResource(R.drawable.ic_user_bookmark);
                    //如果已经收藏，则收藏列表肯定不为空，判断是否包含该相册，然后取消收藏
                    if (mCurrentUser.getBookmarkAlbum().contains(album_id)) {
                        mCurrentUser.getBookmarkAlbum().remove(album_id);
                        mCurrentUser.setBookmarkAlbum(mCurrentUser.getBookmarkAlbum());
                        mCurrentUser.saveInBackground();
                        ShowToast.showShortToast(AlbumActivity.this, "您已将该相册移出收藏列表！");
                    }

                } else {
                    //如果未收藏，点击则为收藏
                    isBookmarked = true;
                    iv_bookmark.setImageResource(R.drawable.ic_user_bookmarked);
                    //先判断收藏列表是否为空，如果收藏列表不为空，判断是否包含该相册，然后添加
                    if (mCurrentUser.getBookmarkAlbum() != null) {
                        if (!mCurrentUser.getBookmarkAlbum().contains(album_id)) {
                            mCurrentUser.getBookmarkAlbum().add(album_id);
                            mCurrentUser.setBookmarkAlbum(mCurrentUser.getBookmarkAlbum());
                            mCurrentUser.saveInBackground();

                            //如果当前用户不是当前相册的作者
                            if (!author_id.equals(mCurrentUser.getObjectId())) {
                                //通知对方收藏了相册
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("type", "StatusBookmark");
                                data.put(Config.USERID, mCurrentUser.getObjectId());
                                data.put(Config.NICKNAME, mCurrentUser.getNickname());
                                data.put("album_id", album_id);
                                data.put(Config.ALBUMNAME, album_name);
                                AVStatus status = AVStatus.createStatusWithData(data);
                                AVStatus.sendPrivateStatusInBackgroud(status, author_id, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Log.i(TAG, "Send private status finished.");

                                    }
                                });
                            }

                            ShowToast.showShortToast(AlbumActivity.this, "您已成功收藏该相册！");
                        }
                    } else {
                        //如果收藏列表为空，新建一个列表，添加该相册，并赋值
                        List<String> newList = new ArrayList<>();
                        newList.add(album_id);
                        mCurrentUser.setBookmarkAlbum(newList);
                        mCurrentUser.saveInBackground();

                        //如果当前用户不是当前相册的作者
                        if (!author_id.equals(mCurrentUser.getObjectId())) {
                            //通知对方收藏了相册
                            Map<String, Object> data = new HashMap<String, Object>();
                            data.put("type", "StatusBookmark");
                            data.put(Config.USERID, mCurrentUser.getObjectId());
                            data.put(Config.NICKNAME, mCurrentUser.getNickname());
                            data.put("album_id", album_id);
                            data.put(Config.ALBUMNAME, album_name);
                            AVStatus status = AVStatus.createStatusWithData(data);
                            AVStatus.sendPrivateStatusInBackgroud(status, author_id, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    Log.i(TAG, "Send private status finished.");

                                }
                            });
                        }

                        ShowToast.showShortToast(AlbumActivity.this, "您已成功收藏该相册！");
                    }
                }
                break;
            case R.id.iv_like:
                //如果已经喜欢，点击则为取消喜欢
                if (isLiked) {
                    isLiked = false;
                    iv_like.setImageResource(R.drawable.ic_user_like);
                    //如果已经喜欢，则喜欢列表肯定不为空，判断当前列表是否包含该相册，然后移出列表
                    if (mCurrentUser.getLikeAlbum().contains(album_id)) {
                        mCurrentUser.getLikeAlbum().remove(album_id);
                        mCurrentUser.setLikeAlbum(mCurrentUser.getLikeAlbum());
                        mCurrentUser.saveInBackground();
                        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                        query.whereEqualTo("objectId", album_id);
                        query.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                if (list != null) {
                                    if (list.size() != 0) {
                                        int likeNumber = Integer.parseInt(list.get(0).getLikenumber());
                                        likeNumber--;
                                        list.get(0).setLikenumber(String.valueOf(likeNumber));
                                        list.get(0).saveInBackground();
                                        tv_like_number.setText(list.get(0).getLikenumber());
                                    }
                                }
                            }
                        });
                        ShowToast.showShortToast(AlbumActivity.this, "您已将该相册移出喜欢列表！");
                    }

                } else {
                    //如果未喜欢，点击则为喜欢
                    isLiked = true;
                    iv_like.setImageResource(R.drawable.ic_user_liked);
                    //先判断喜欢列表是否为空，如果喜欢的列表不为空，判断列表是否包含该相册，然后添加
                    if (mCurrentUser.getLikeAlbum() != null) {
                        if (!mCurrentUser.getLikeAlbum().contains(album_id)) {
                            mCurrentUser.getLikeAlbum().add(album_id);
                            mCurrentUser.setLikeAlbum(mCurrentUser.getLikeAlbum());
                            mCurrentUser.saveInBackground();
                            AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                            query.whereEqualTo("objectId", album_id);
                            query.findInBackground(new FindCallback<MusicAlbum>() {
                                @Override
                                public void done(List<MusicAlbum> list, AVException e) {
                                    if (list != null) {
                                        if (list.size() != 0) {
                                            int likeNumber = Integer.parseInt(list.get(0).getLikenumber());
                                            likeNumber++;
                                            list.get(0).setLikenumber(String.valueOf(likeNumber));
                                            list.get(0).saveInBackground();
                                            tv_like_number.setText(list.get(0).getLikenumber());
                                        }
                                    }
                                }
                            });

                            //如果当前用户不是当前相册的作者
                            if (!author_id.equals(mCurrentUser.getObjectId())) {
                                //通知对方喜欢了相册
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("type", "StatusLike");
                                data.put(Config.USERID, mCurrentUser.getObjectId());
                                data.put(Config.NICKNAME, mCurrentUser.getNickname());
                                data.put("album_id", album_id);
                                data.put(Config.ALBUMNAME, album_name);
                                AVStatus status = AVStatus.createStatusWithData(data);
                                AVStatus.sendPrivateStatusInBackgroud(status, author_id, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Log.i(TAG, "Send private status finished.");

                                    }
                                });
                            }

                            ShowToast.showShortToast(AlbumActivity.this, "您已将该相册标记为喜欢！");
                        }
                    } else {
                        //如果喜欢的列表为空，新建一个列表，添加该相册并赋值
                        List<String> newList = new ArrayList<>();
                        newList.add(album_id);
                        mCurrentUser.setLikeAlbum(newList);
                        mCurrentUser.saveInBackground();
                        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                        query.whereEqualTo("objectId", album_id);
                        query.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                if (list != null) {
                                    if (list.size() != 0) {
                                        int likeNumber = Integer.parseInt(list.get(0).getLikenumber());
                                        likeNumber++;
                                        list.get(0).setLikenumber(String.valueOf(likeNumber));
                                        list.get(0).saveInBackground();
                                        tv_like_number.setText(list.get(0).getLikenumber());
                                    }
                                }
                            }
                        });

                        //如果当前用户不是当前相册的作者
                        if (!author_id.equals(mCurrentUser.getObjectId())) {
                            //通知对方喜欢了相册
                            Map<String, Object> data = new HashMap<String, Object>();
                            data.put("type", "StatusLike");
                            data.put(Config.USERID, mCurrentUser.getObjectId());
                            data.put(Config.NICKNAME, mCurrentUser.getNickname());
                            data.put("album_id", album_id);
                            data.put(Config.ALBUMNAME, album_name);
                            AVStatus status = AVStatus.createStatusWithData(data);
                            AVStatus.sendPrivateStatusInBackgroud(status, author_id, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    Log.i(TAG, "Send private status finished.");

                                }
                            });
                        }

                        ShowToast.showShortToast(AlbumActivity.this, "您已将该相册标记为喜欢！");
                    }
                }
                break;
        }
    }
}
