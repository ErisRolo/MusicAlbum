package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.MainActivity;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.FileUtils;
import com.example.guohouxiao.musicalbum.utils.L;
import com.example.guohouxiao.musicalbum.utils.ShowToast;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/8/19.
 * 编辑个人资料
 */

public class MyUserActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private CircleImageView iv_avatar;//修改头像
    private ImageView iv_modify_nickname;//修改昵称
    private ImageView iv_modify_desc;//修改简介

    private LinearLayout ll_exit_user;//退出登录

    private TextView tv_nickname;//昵称
    private TextView tv_desc;//简介

    private AlertDialog dialog_nickname;//修改昵称对话框
    private AlertDialog dialog_desc;//修改简介对话框
    private EditText et_nickname;
    private EditText et_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user);

        initView();
        initDialog();
    }

    private void initDialog() {
        View dialogNickname = LayoutInflater.from(this).inflate(R.layout.dialog_modify_nickname, null);
        et_nickname = (EditText) dialogNickname.findViewById(R.id.et_nickname);
        dialog_nickname = new AlertDialog.Builder(this)
                .setView(dialogNickname)
                .setTitle("修改昵称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
/*                        AVUser.getCurrentUser().put("pickname", et_nickname.getText().toString());
                        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tv_nickname.setText(et_nickname.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this,"修改成功");
                            }
                        });*/

                        mCurrentUser.setNickname(et_nickname.getText().toString());
                        mCurrentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tv_nickname.setText(et_nickname.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this, "修改成功");
                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();


        View dialogDesc = LayoutInflater.from(this).inflate(R.layout.dialog_modify_desc, null);
        et_desc = (EditText) dialogDesc.findViewById(R.id.et_desc);
        dialog_desc = new AlertDialog.Builder(this)
                .setView(dialogDesc)
                .setTitle("修改简介")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
/*                        AVUser.getCurrentUser().put("desc",et_desc.getText().toString());
                        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tv_desc.setText(et_desc.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this,"修改成功");
                            }
                        });*/

                        mCurrentUser.setDesc(et_desc.getText().toString());
                        mCurrentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tv_desc.setText(et_desc.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this, "修改成功");
                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();

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

        iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        iv_modify_nickname = (ImageView) findViewById(R.id.iv_modify_nickname);
        iv_modify_desc = (ImageView) findViewById(R.id.iv_modify_desc);
        ll_exit_user = (LinearLayout) findViewById(R.id.ll_exit_user);

        iv_avatar.setOnClickListener(this);
        iv_modify_nickname.setOnClickListener(this);
        iv_modify_desc.setOnClickListener(this);
        ll_exit_user.setOnClickListener(this);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        if (mCurrentUser != null) {

            String url = mCurrentUser.getAvatar();
            if (url != null) {
                Glide.with(this).load(url).into(iv_avatar);
            }

            tv_nickname.setText(mCurrentUser.getNickname());
            tv_desc.setText(mCurrentUser.getDesc());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .theme(R.style.Matisse_Dracula)
                        .imageEngine(new GlideEngine())
                        .countable(false)
                        .maxSelectable(1)
                        .forResult(0);
                break;
            case R.id.iv_modify_nickname:
                dialog_nickname.show();
                break;
            case R.id.iv_modify_desc:
                dialog_desc.show();
                break;
            case R.id.ll_exit_user:
/*                AVUser.logOut();
                AVUser currentUser = AVUser.getCurrentUser();*/
                User.logOut();
                startActivity(new Intent(MyUserActivity.this, LoginActivity.class));
                MyUserActivity.this.finish();
                MainActivity.instance.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(iv_avatar);

                String path = FileUtils.getFilePahtFromUri(this, uri);

                L.i(path);

                if (path != null) {
                    try {
                        final AVFile file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
/*                                    AVUser.getCurrentUser().put("avatar", file.getUrl());
                                    AVUser.getCurrentUser().saveInBackground();*/
                                    mCurrentUser.setAvatar(file.getUrl());
                                    mCurrentUser.saveInBackground();
                                } else {
                                    Snackbar.make(iv_avatar, "上传头像失败", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}
