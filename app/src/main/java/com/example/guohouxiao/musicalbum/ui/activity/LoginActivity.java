package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.guohouxiao.musicalbum.MainActivity;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.L;
import com.example.guohouxiao.musicalbum.utils.ShowToast;
import com.example.guohouxiao.musicalbum.widget.CustomDialog;

import cn.leancloud.chatkit.LCChatKit;


/**
 * Created by guohouxiao on 2017/7/15.
 * 登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_username;
    private EditText et_login_password;
    private TextView tv_forget;
    private Button btn_login;
    private Button btn_register;

    private CustomDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        //初始化dialog
        mDialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        mDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.btn_login:
                String login_username = et_login_username.getText().toString().trim();
                String login_password = et_login_password.getText().toString().trim();
                if (!TextUtils.isEmpty(login_username) && !TextUtils.isEmpty(login_password)) {

                    mDialog.show();

                    User.logInInBackground(login_username, login_password, new LogInCallback<User>() {
                        @Override
                        public void done(User avUser, AVException e) {
                            mDialog.dismiss();

                            if (e == null) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                ShowToast.showShortToast(LoginActivity.this, e.getMessage());
                            }
                        }
                    }, User.class);

                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
