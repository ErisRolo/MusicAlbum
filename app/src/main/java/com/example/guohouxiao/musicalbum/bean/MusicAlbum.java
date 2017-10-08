package com.example.guohouxiao.musicalbum.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/5.
 * 子类化音乐相册
 */

@AVClassName("MusicAlbum")
public class MusicAlbum extends AVObject {

    public static final Creator CREATOR = AVObjectCreator.instance;

    public MusicAlbum() {

    }

    public String getUserId() {
        return getString(Config.USERID);
    }

    public void setUserId(String userId) {
        put(Config.USERID, userId);
    }

    public String getAlbumname() {
        return getString(Config.ALBUMNAME);
    }

    public void setAlbumname(String name) {
        put(Config.ALBUMNAME, name);
    }

    public String getCover() {
        return getString(Config.COVER);
    }

    public void setCover(String cover) {
        put(Config.COVER, cover);
    }

    public String getPlace() {
        return getString(Config.PLACE);
    }

    public void setPlace(String place) {
        put(Config.PLACE, place);
    }

    public AVGeoPoint getWhereCreated() {
        return getAVGeoPoint(Config.WHERECREATED);
    }

    public void setWhereCreated(AVGeoPoint point) {
        put(Config.WHERECREATED, point);
    }

    public String getPlaymode() {
        return getString(Config.PLAYMODE);
    }

    public void setPlaymode(String playmode) {
        put(Config.PLAYMODE, playmode);
    }

    public String getMusic() {
        return getString(Config.MUSIC);
    }

    public void setMusic(String music) {
        put(Config.MUSIC, music);
    }

    public List<String> getPhotos() {
        return getList(Config.PHOTOS);
    }

    public void setPhotos(List<String> photos) {
        put(Config.PHOTOS, photos);
    }

    public List<String> getSubtitles() {
        return getList(Config.SUBTITLES);
    }

    public void setSubtitles(List<String> subtitles) {
        put(Config.SUBTITLES, subtitles);
    }

    public boolean getIsPublic() {
        return getBoolean(Config.ISPUBLIC);
    }

    public void setIsPublic(boolean isPublic) {
        put(Config.ISPUBLIC, isPublic);
    }

    public String getLikenumber() {
        return getString(Config.LIKENUMBER);
    }

    public void setLikenumber(String likenumber) {
        put(Config.LIKENUMBER, likenumber);
    }

}
