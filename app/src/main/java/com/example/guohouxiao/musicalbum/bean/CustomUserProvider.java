package com.example.guohouxiao.musicalbum.bean;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.utils.L;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by guohouxiao on 2017/9/11.
 * 实现即时通讯的用户接口
 */

public class CustomUserProvider implements LCChatProfileProvider {
    private static final String TAG = "CustomUserProvider";

    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

    @Override
    public void fetchProfiles(final List<String> userIdList, final LCChatProfilesCallBack callBack) {
        AVQuery<User> query = AVObject.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
                if (e != null) {
                    Log.i(TAG, "fetchProfiles:" + e);
                } else {
                    if (list != null && !list.isEmpty()) {
                        for (User user : list) {
                            for (String userId : userIdList) {
                                if (userId.equals(user.getUsername())) {
                                    Log.i(TAG, "userId:" + user.getUsername() +
                                            "nickname:" + user.getNickname() +
                                            "avatar:" + user.getAvatar());
                                    LCChatKitUser kitUser = new LCChatKitUser(
                                            user.getUsername(),
                                            user.getNickname(),
                                            user.getAvatar());
                                    userList.add(kitUser);
                                }
                            }
                        }
                    }
                }
                callBack.done(userList, e);
            }
        });
    }
}
