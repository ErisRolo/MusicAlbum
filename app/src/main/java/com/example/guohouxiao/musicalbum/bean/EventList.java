package com.example.guohouxiao.musicalbum.bean;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/22.
 * 实体化EventList
 * 用于添加字幕
 */

public class EventList {

    private List<String> mList;

    public EventList(List<String> list) {
        mList = list;
    }

    public List<String> getSelected() {
        return mList;
    }
}
