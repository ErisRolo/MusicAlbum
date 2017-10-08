package com.example.guohouxiao.musicalbum.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.List;

/**
 * Created by guohouxiao on 2017/8/30.
 * 子类化User
 */

@AVClassName("User")
public class User extends AVUser {

/*    private String avatar;//头像
    private String nickname;//昵称
    private String desc;//简介*/

    public String getAvatar() {
        return this.getString(Config.AVATAR);
    }

    public void setAvatar(String avatar) {
        this.put(Config.AVATAR, avatar);
    }

    public String getNickname() {
        return this.getString(Config.NICKNAME);
    }

    public void setNickname(String nickname) {
        this.put(Config.NICKNAME, nickname);
    }

    public String getDesc() {
        return this.getString(Config.DESC);
    }

    public void setDesc(String desc) {
        this.put(Config.DESC, desc);
    }

    public List<String> getBookmarkAlbum() {
        return this.getList(Config.BOOKMARKALBUM);
    }

    public void setBookmarkAlbum(List<String> bookmarkAlbum) {
        this.put(Config.BOOKMARKALBUM, bookmarkAlbum);
    }

    public List<String> getLikeAlbum() {
        return this.getList(Config.LIKEALBUM);
    }

    public void setLikeAlbum(List<String> likeAlbum) {
        this.put(Config.LIKEALBUM, likeAlbum);
    }


}
