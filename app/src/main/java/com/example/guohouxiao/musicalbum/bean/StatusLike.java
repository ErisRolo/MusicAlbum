package com.example.guohouxiao.musicalbum.bean;

/**
 * Created by guohouxiao on 2017/10/5.
 * 喜欢的动态实体类
 */

public class StatusLike {

    private String userId;//用户ID
    private String nickname;//用户昵称
    private String albumId;//相册ID
    private String albumname;//相册名称

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }
}
