package com.example.guohouxiao.musicalbum.bean;

import com.avos.avoscloud.AVGeoPoint;

/**
 * Created by guohouxiao on 2017/10/5.
 * 新的动态的实体类
 */

public class StatusAdd {

    private String avatar;//作者头像
    private String userId;//作者ID
    private String nickname;//作者昵称
    private String place;//新建的地点
    private String cover;//相册封面
    private String albumname;//相册名称
    private String albumId;//相册ID
    private AVGeoPoint point;//新建的经纬度

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public AVGeoPoint getPoint() {
        return point;
    }

    public void setPoint(AVGeoPoint point) {
        this.point = point;
    }
}
