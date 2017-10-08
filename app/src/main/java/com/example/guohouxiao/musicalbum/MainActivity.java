package com.example.guohouxiao.musicalbum;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.ui.fragment.FindFragment;
import com.example.guohouxiao.musicalbum.ui.fragment.HomeFragment;
import com.example.guohouxiao.musicalbum.ui.fragment.MyFragment;
import com.example.guohouxiao.musicalbum.ui.fragment.NotificationFragment;
import com.example.guohouxiao.musicalbum.utils.BottomNavigationViewHelper;
import com.example.guohouxiao.musicalbum.utils.FragmentUtils;
import com.example.guohouxiao.musicalbum.utils.L;

import cn.leancloud.chatkit.LCChatKit;
import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by guohouxiao on 2017/7/15.
 * 主界面
 */

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottom_layout;
    private FragmentManager fm;

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private NotificationFragment mNotificationFragment;
    private MyFragment mMyFragment;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        PermissionGen.with(this)
                .addRequestCode(10000)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CHANGE_CONFIGURATION)
                .request();

        //聊天用户登录
        LCChatKit.getInstance().open(mCurrentUser.getUsername(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    L.i("聊天用户已登入。");
                }
            }
        });

        fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mHomeFragment = new HomeFragment();
            FragmentUtils.addFragment(fm, R.id.fragment_container, mHomeFragment);
            initView();
        } else {
            initView();
        }

    }

    private void initView() {

        bottom_layout = (BottomNavigationView) findViewById(R.id.bottom_layout);
        BottomNavigationViewHelper.disableShiftMode(bottom_layout);
        bottom_layout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        mHomeFragment = new HomeFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mHomeFragment);
                        break;
                    case R.id.find:
                        mFindFragment = new FindFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mFindFragment);
                        break;
                    case R.id.notification:
                        mNotificationFragment = new NotificationFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mNotificationFragment);
                        break;
                    case R.id.my:
                        mMyFragment = new MyFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mMyFragment);
                        break;
                }
                return true;
            }
        });

    }


}
