package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.util.List;

/**
 * Created by guohouxiao on 2017/10/14.
 * 可能感兴趣的人
 */

public class InterestingActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_desc;

    private LinearLayout ll_interesting;
    private Button btn_follow;
    private boolean isFollow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting);

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

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        AVQuery<User> query = AVObject.getQuery(User.class);
        query.whereEqualTo("objectId", "59a91aeea8f9d30064bbb33e");
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                String url = list.get(0).getAvatar();
                if (url != null) {
                    Glide.with(InterestingActivity.this).load(url).into(iv_avatar);
                }
                tv_nickname.setText(list.get(0).getNickname());
                tv_desc.setText(list.get(0).getDesc());
            }
        });

        ll_interesting = (LinearLayout) findViewById(R.id.ll_interesting);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        ll_interesting.setOnClickListener(this);
        btn_follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_interesting:
                Intent intent = new Intent(this, OtherUserActivity.class);
                intent.putExtra("other_user_id", "59a91aeea8f9d30064bbb33e");
                startActivity(intent);
                break;
            case R.id.btn_follow:
                if (isFollow) {
                    btn_follow.setText("关注");
                    ShowToast.showShortToast(this, "你已取消关注该用户。");
                    isFollow = false;
                } else {
                    btn_follow.setText("已关注");
                    ShowToast.showShortToast(this, "你已关注该用户。");
                    isFollow = true;
                }
                break;
        }
    }
}
