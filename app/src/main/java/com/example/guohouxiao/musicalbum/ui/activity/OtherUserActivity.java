package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/9/2.
 * 其他用户的个人中心界面
 */

public class OtherUserActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OtherUserActivity";

    private static final String OTHER_USER_ID = "other_user_id";
    private static final String USER_ID = "user_id";
    private String otherUserId;//用户的objectId
    private String otherUsername;//用于开启聊天
    private boolean isFollow = true;//关注状态
    private List<String> mList = new ArrayList<>();

    private Toolbar mToolbar;

    private CircleImageView iv_other_avatar;
    private TextView tv_other_nickname;
    private TextView tv_other_desc;
    private Button btn_follow;
    private Button btn_private_msg;
    private TextView tv_followee_number;
    private TextView tv_follower_number;
    private TextView tv_public_number;
    private TextView tv_like_number;
    private LinearLayout ll_followee;
    private LinearLayout ll_follower;
    private LinearLayout ll_public_album;
    private LinearLayout ll_like_album;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        otherUserId = getIntent().getStringExtra(OTHER_USER_ID);

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

        iv_other_avatar = (CircleImageView) findViewById(R.id.iv_other_avatar);
        tv_other_nickname = (TextView) findViewById(R.id.tv_other_nickname);
        tv_other_desc = (TextView) findViewById(R.id.tv_other_desc);
        tv_followee_number = (TextView) findViewById(R.id.tv_followee_number);
        tv_follower_number = (TextView) findViewById(R.id.tv_follower_number);
        tv_public_number = (TextView) findViewById(R.id.tv_public_number);
        tv_like_number = (TextView) findViewById(R.id.tv_like_number);

        AVQuery<User> query = AVObject.getQuery(User.class);
        query.whereEqualTo("objectId", otherUserId);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        String url = list.get(0).getAvatar();
                        if (url != null) {
                            Glide.with(OtherUserActivity.this).load(url).into(iv_other_avatar);
                        }
                        tv_other_nickname.setText(list.get(0).getNickname());
                        tv_other_desc.setText(list.get(0).getDesc());
                        otherUsername = list.get(0).getUsername();

                        try {
                            AVQuery<User> followeeQuery = list.get(0).followeeQuery(User.class);
                            followeeQuery.findInBackground(new FindCallback<User>() {
                                @Override
                                public void done(List<User> list, AVException e) {
                                    if (list != null) {
                                        if (list.size() != 0) {
                                            tv_followee_number.setText(String.valueOf(list.size()));
                                        } else {
                                            tv_followee_number.setText("0");
                                        }
                                    }
                                }
                            });
                        } catch (AVException e1) {
                            e1.printStackTrace();
                        }

                        try {
                            AVQuery<User> followerQuery = list.get(0).followerQuery(User.class);
                            followerQuery.findInBackground(new FindCallback<User>() {
                                @Override
                                public void done(List<User> list, AVException e) {
                                    if (list != null) {
                                        if (list.size() != 0) {
                                            tv_follower_number.setText(String.valueOf(list.size()));
                                        } else {
                                            tv_follower_number.setText("0");
                                        }
                                    }
                                }
                            });
                        } catch (AVException e1) {
                            e1.printStackTrace();
                        }

                        AVQuery<MusicAlbum> queryUser = AVObject.getQuery(MusicAlbum.class);
                        queryUser.whereEqualTo(Config.USERID, otherUserId);
                        AVQuery<MusicAlbum> queryIsPublic = AVObject.getQuery(MusicAlbum.class);
                        queryIsPublic.whereEqualTo(Config.ISPUBLIC, true);
                        AVQuery<MusicAlbum> query = AVQuery.and(Arrays.asList(queryUser, queryIsPublic));
                        query.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                if (list != null) {
                                    if (list.size() != 0) {
                                        tv_public_number.setText(String.valueOf(list.size()));
                                    } else {
                                        tv_public_number.setText("0");
                                    }
                                }
                            }
                        });

                        if (list.get(0).getLikeAlbum() != null) {
                            if (list.get(0).getLikeAlbum().size() != 0) {
                                tv_like_number.setText(String.valueOf(list.get(0).getLikeAlbum().size()));
                            } else {
                                tv_like_number.setText("0");
                            }
                        }
                    }
                }

            }
        });


        ll_followee = (LinearLayout) findViewById(R.id.ll_followee);
        ll_follower = (LinearLayout) findViewById(R.id.ll_follower);
        ll_public_album = (LinearLayout) findViewById(R.id.ll_public_album);
        ll_like_album = (LinearLayout) findViewById(R.id.ll_like_album);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        btn_private_msg = (Button) findViewById(R.id.btn_private_msg);

        //如果跳转到了当前用户的个人中心界面，两个按钮设置为不可见，简介位置下移
        if (otherUserId.equals(mCurrentUser.getObjectId())) {
            btn_follow.setVisibility(View.INVISIBLE);
            btn_private_msg.setVisibility(View.INVISIBLE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 200, 0, 0);
            lp.gravity = Gravity.CENTER;
            tv_other_desc.setLayoutParams(lp);
        }

        //判断当前用户是否关注了当前界面的用户
        //若没有关注可进行关注，若已关注可取消关注
        //同时设定按钮文字
        try {
            AVQuery<User> followeeQuery = mCurrentUser.followeeQuery(User.class);
            followeeQuery.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i).getObjectId());
                            }
                            if (mList.contains(otherUserId)) {
                                btn_follow.setText("已关注");
                                isFollow = true;
                            } else {
                                isFollow = false;
                                btn_follow.setText("关注");
                            }
                        }
                    }
                }
            });
        } catch (AVException e) {
            e.printStackTrace();
        }

        ll_followee.setOnClickListener(this);
        ll_follower.setOnClickListener(this);
        ll_public_album.setOnClickListener(this);
        ll_like_album.setOnClickListener(this);
        btn_follow.setOnClickListener(this);
        btn_private_msg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_followee:
                Intent intent1 = new Intent(this, FolloweeActivity.class);
                intent1.putExtra(USER_ID, otherUserId);
                startActivity(intent1);
                break;
            case R.id.ll_follower:
                Intent intent2 = new Intent(this, FollowerActivity.class);
                intent2.putExtra(USER_ID, otherUserId);
                startActivity(intent2);
                break;
            case R.id.ll_public_album:
                Intent intent3 = new Intent(this, PublicAlbumActivity.class);
                intent3.putExtra(USER_ID, otherUserId);
                startActivity(intent3);
                break;
            case R.id.ll_like_album:
                Intent intent4 = new Intent(this, LikeAlbumActivity.class);
                intent4.putExtra(USER_ID, otherUserId);
                startActivity(intent4);
                break;
            case R.id.btn_follow:
                //如果已关注，点击为取消关注
                if (isFollow) {
                    mCurrentUser.unfollowInBackground(otherUserId, new FollowCallback() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                        }

                        @Override
                        protected void internalDone0(Object o, AVException e) {
                            if (e == null) {
                                isFollow = false;
                                btn_follow.setText("关注");
                                ShowToast.showShortToast(OtherUserActivity.this, "已取消关注该用户。");
                            } else {
                                ShowToast.showShortToast(OtherUserActivity.this, "取关失败！");
                            }
                        }
                    });
                    //如果未关注，点击为关注
                } else {
                    mCurrentUser.followInBackground(otherUserId, new FollowCallback() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                        }

                        @Override
                        protected void internalDone0(Object o, AVException e) {
                            if (e == null) {
                                isFollow = true;
                                btn_follow.setText("已关注");

                                //通知对方关注了对方
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("type", "StatusFollow");
                                data.put(Config.AVATAR, mCurrentUser.getAvatar());
                                data.put(Config.NICKNAME, mCurrentUser.getNickname());
                                data.put(Config.USERID, mCurrentUser.getObjectId());
                                AVStatus status = AVStatus.createStatusWithData(data);
                                AVStatus.sendPrivateStatusInBackgroud(status, otherUserId, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Log.i(TAG, "Send private status finished.");
                                    }
                                });

                                ShowToast.showShortToast(OtherUserActivity.this, "关注成功。");
                            } else {
                                ShowToast.showShortToast(OtherUserActivity.this, "关注失败！");
                            }
                        }
                    });
                }
                break;
            case R.id.btn_private_msg:
/*                LCChatKit.getInstance().open(mCurrentUser.getUsername(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null == e) {
                            //finish();
                            Intent intent = new Intent(OtherUserActivity.this, LCIMConversationActivity.class);
                            intent.putExtra(LCIMConstants.PEER_ID,otherUsername);
                            startActivity(intent);
                        }
                    }
                });*/
                Intent intent = new Intent(this, LCIMConversationActivity.class);
                intent.putExtra(LCIMConstants.PEER_ID, otherUsername);
                startActivity(intent);
                break;
        }
    }

}
