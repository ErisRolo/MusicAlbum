package com.example.guohouxiao.musicalbum.utils;

import com.example.guohouxiao.musicalbum.adapter.SubtitlesAdapter;
import com.example.guohouxiao.musicalbum.bean.ImgWithSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guohouxiao on 2017/9/23.
 * 用于绑定图片和字幕
 */

public class Presenter {

    private Presenter() {
    }

    public static Presenter getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final Presenter sInstance = new Presenter();
    }

    HashMap<SubtitlesAdapter.ViewHolder, ImgWithSub> mMap = new LinkedHashMap<>();
    HashMap<ImgWithSub, SubtitlesAdapter.ViewHolder> mReverseMap = new LinkedHashMap<>();

    public void register(SubtitlesAdapter.ViewHolder viewHolder, ImgWithSub imgWithSub) {
        mMap.put(viewHolder, imgWithSub);
        mReverseMap.put(imgWithSub, viewHolder);
    }

    public void modifyTextContent(SubtitlesAdapter.ViewHolder viewHolder, String sub) {
        ImgWithSub imgWithSub = mMap.get(viewHolder);
        if (null != imgWithSub) {
            imgWithSub.setSub(sub);
        }
    }

    public List<String> getContentText() {
        List<String> list = new ArrayList<>();
        Iterator<Map.Entry<SubtitlesAdapter.ViewHolder, ImgWithSub>> iterator = mMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SubtitlesAdapter.ViewHolder, ImgWithSub> entry = iterator.next();
            list.add(entry.getKey().getContentText());
        }
        return list;
    }

}
