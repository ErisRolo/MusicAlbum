package com.example.guohouxiao.musicalbum.bean;

/**
 * Created by guohouxiao on 2017/10/5.
 * 关注的动态实体类
 */

public class StatusFollow {

    private String avatar;//用户头像
    private String nickname;//用户昵称
    private String userId;//用户ID

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
