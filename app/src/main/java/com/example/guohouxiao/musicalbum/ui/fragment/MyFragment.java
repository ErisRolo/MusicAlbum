package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.activity.BookmarkAlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.FolloweeActivity;
import com.example.guohouxiao.musicalbum.ui.activity.FollowerActivity;
import com.example.guohouxiao.musicalbum.ui.activity.LikeAlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.MyUserActivity;
import com.example.guohouxiao.musicalbum.ui.activity.PrivateAlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.PublicAlbumActivity;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/7/18.
 * 个人中心
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_profile;//编辑个人资料
    private LinearLayout ll_followee;//关注者
    private LinearLayout ll_follower;//粉丝
    private LinearLayout ll_public_album;//公开相册
    private LinearLayout ll_private_album;//私密相册
    private LinearLayout ll_like_album;//喜欢的相册
    private LinearLayout ll_bookmark_album;//收藏的相册

    private CircleImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_desc;

    private TextView tv_followee_number;//关注数
    private TextView tv_follower_number;//粉丝数

    private TextView tv_public_number;//公开相册数
    private TextView tv_private_number;//私有相册数
    private TextView tv_like_number;//喜欢的相册数
    private TextView tv_bookmark_number;//收藏的相册数

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        findView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshView();

    }

    private void findView(View view) {
        ll_profile = (LinearLayout) view.findViewById(R.id.ll_profile);
        ll_followee = (LinearLayout) view.findViewById(R.id.ll_followee);
        ll_follower = (LinearLayout) view.findViewById(R.id.ll_follower);
        ll_public_album = (LinearLayout) view.findViewById(R.id.ll_public_album);
        ll_private_album = (LinearLayout) view.findViewById(R.id.ll_private_album);
        ll_like_album = (LinearLayout) view.findViewById(R.id.ll_like_album);
        ll_bookmark_album = (LinearLayout) view.findViewById(R.id.ll_bookmark_album);

        ll_profile.setOnClickListener(this);
        ll_follower.setOnClickListener(this);
        ll_followee.setOnClickListener(this);
        ll_public_album.setOnClickListener(this);
        ll_private_album.setOnClickListener(this);
        ll_like_album.setOnClickListener(this);
        ll_bookmark_album.setOnClickListener(this);

        iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);

        tv_followee_number = (TextView) view.findViewById(R.id.tv_followee_number);
        tv_follower_number = (TextView) view.findViewById(R.id.tv_follower_number);

        tv_public_number = (TextView) view.findViewById(R.id.tv_public_number);
        tv_private_number = (TextView) view.findViewById(R.id.tv_private_number);
        tv_like_number = (TextView) view.findViewById(R.id.tv_like_number);
        tv_bookmark_number = (TextView) view.findViewById(R.id.tv_bookmark_number);

        refreshView();

    }

    private void refreshView() {
        if (mCurrentUser != null) {

            String url = mCurrentUser.getAvatar();
            if (url != null) {
                Glide.with(this).load(url).into(iv_avatar);
            }
            tv_nickname.setText(mCurrentUser.getNickname());
            tv_desc.setText(mCurrentUser.getDesc());

            try {
                AVQuery<User> followeeQuery = mCurrentUser.followeeQuery(User.class);
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
            } catch (AVException e) {
                e.printStackTrace();
            }
            try {
                AVQuery<User> followerQuery = mCurrentUser.followerQuery(User.class);
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
            } catch (AVException e) {
                e.printStackTrace();
            }

            AVQuery<MusicAlbum> queryUser = AVObject.getQuery(MusicAlbum.class);
            queryUser.whereEqualTo(Config.USERID, mCurrentUser.getObjectId());
            AVQuery<MusicAlbum> queryIsPublic = AVObject.getQuery(MusicAlbum.class);
            queryIsPublic.whereEqualTo(Config.ISPUBLIC, true);
            AVQuery<MusicAlbum> queryPublic = AVQuery.and(Arrays.asList(queryUser, queryIsPublic));
            queryPublic.findInBackground(new FindCallback<MusicAlbum>() {
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
            AVQuery<MusicAlbum> queryIsPrivate = AVObject.getQuery(MusicAlbum.class);
            queryIsPrivate.whereEqualTo(Config.ISPUBLIC, false);
            AVQuery<MusicAlbum> queryPrivate = AVQuery.and(Arrays.asList(queryUser, queryIsPrivate));
            queryPrivate.findInBackground(new FindCallback<MusicAlbum>() {
                @Override
                public void done(List<MusicAlbum> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            tv_private_number.setText(String.valueOf(list.size()));
                        } else {
                            tv_private_number.setText("0");
                        }
                    }
                }
            });

            if (mCurrentUser.getLikeAlbum() != null) {
                if (mCurrentUser.getLikeAlbum().size() != 0) {
                    tv_like_number.setText(String.valueOf(mCurrentUser.getLikeAlbum().size()));
                }
            }
            if (mCurrentUser.getBookmarkAlbum() != null) {
                if (mCurrentUser.getBookmarkAlbum().size() != 0) {
                    tv_bookmark_number.setText(String.valueOf(mCurrentUser.getBookmarkAlbum().size()));
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_profile:
                startActivity(new Intent(getActivity(), MyUserActivity.class));
                break;
            case R.id.ll_followee:
                startActivity(new Intent(getActivity(), FolloweeActivity.class));
                break;
            case R.id.ll_follower:
                startActivity(new Intent(getActivity(), FollowerActivity.class));
                break;
            case R.id.ll_public_album:
                startActivity(new Intent(getActivity(), PublicAlbumActivity.class));
                break;
            case R.id.ll_private_album:
                startActivity(new Intent(getActivity(), PrivateAlbumActivity.class));
                break;
            case R.id.ll_like_album:
                startActivity(new Intent(getActivity(), LikeAlbumActivity.class));
                break;
            case R.id.ll_bookmark_album:
                startActivity(new Intent(getActivity(), BookmarkAlbumActivity.class));
                break;
        }
    }
}
