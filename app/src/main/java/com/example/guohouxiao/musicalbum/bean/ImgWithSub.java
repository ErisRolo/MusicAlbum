package com.example.guohouxiao.musicalbum.bean;

/**
 * Created by guohouxiao on 2017/9/23.
 * 将字幕添加到图片上构成的实体类
 */

public class ImgWithSub {

    private String img;
    private String sub;

    public ImgWithSub(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

}
