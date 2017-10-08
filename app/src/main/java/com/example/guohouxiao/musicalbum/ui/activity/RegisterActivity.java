package com.example.guohouxiao.musicalbum.ui.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

/**
 * Created by guohouxiao on 2017/7/15.
 * 注册
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_register_username;
    private EditText et_register_password;
    private EditText et_register_pass;
    private EditText et_register_email;
    private Button btn_registered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        et_register_username = (EditText) findViewById(R.id.et_register_username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_pass = (EditText) findViewById(R.id.et_register_pass);
        et_register_email = (EditText) findViewById(R.id.et_register_email);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                String register_username = et_register_username.getText().toString().trim();
                String register_password = et_register_password.getText().toString().trim();
                String register_pass = et_register_pass.getText().toString().trim();
                String register_email = et_register_email.getText().toString().trim();

                if (!TextUtils.isEmpty(register_username) && !TextUtils.isEmpty(register_password)
                        && !TextUtils.isEmpty(register_pass) && !TextUtils.isEmpty(register_email)) {
                    if (register_password.equals(register_pass)) {

                        final User user = new User();
                        user.setUsername(register_username);
                        user.setPassword(register_password);
                        user.setEmail(register_email);
                        user.setNickname("未命名用户");
                        user.setDesc("这个人很懒，什么都没有留下。");
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    ShowToast.showShortToast(RegisterActivity.this, "注册成功，请前往邮箱激活账号！");
                                    finish();
                                } else {
                                    ShowToast.showShortToast(RegisterActivity.this, e.getMessage());
                                }
                            }
                        });
                    } else {
                        ShowToast.showShortToast(this, "两次输入密码不一致！");
                    }
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }

                break;
        }
    }
}
