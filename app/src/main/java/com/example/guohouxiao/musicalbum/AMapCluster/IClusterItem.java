package com.example.guohouxiao.musicalbum.AMapCluster;


import com.amap.api.maps.model.LatLng;

/**
 * Created by guohouxiao on 2017/9/14.
 * 聚合点的元素
 */

public interface IClusterItem {

    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
    public LatLng getPosition();

    public IconRes getIconRes();
}
