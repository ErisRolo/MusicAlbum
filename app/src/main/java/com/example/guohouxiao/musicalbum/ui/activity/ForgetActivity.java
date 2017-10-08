package com.example.guohouxiao.musicalbum.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.ShowToast;


/**
 * Created by guohouxiao on 2017/7/17.
 * 忘记密码
 */

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_forget_email;
    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

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

        et_forget_email = (EditText) findViewById(R.id.et_forget_email);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String email = et_forget_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
/*                    User.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ShowToast.showShortToast(ForgetActivity.this,"邮件已发送，请查收并设定新密码！");
                                finish();
                            } else {
                                ShowToast.showShortToast(ForgetActivity.this,"邮件发送失败！");
                            }
                        }
                    });*/

                    User.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                ShowToast.showShortToast(ForgetActivity.this,"邮件已发送，请查收并设定新密码！");
                                finish();
                            } else {
                                ShowToast.showShortToast(ForgetActivity.this,e.getMessage());
                            }
                        }
                    });
                } else {
                    ShowToast.showShortToast(ForgetActivity.this,"输入框不能为空！");
                }
                break;
        }
    }
}
