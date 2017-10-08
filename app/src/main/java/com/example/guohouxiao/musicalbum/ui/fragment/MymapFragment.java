package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.AMapCluster.ClusterOverlay;
import com.example.guohouxiao.musicalbum.AMapCluster.IClusterItem;
import com.example.guohouxiao.musicalbum.AMapCluster.IconRes;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/4.
 * 我的（走过的）
 */

public class MymapFragment extends BaseFragment implements AMap.OnMarkerClickListener, AMap.OnCameraChangeListener {

    private static final String TAG = "MymapFragment";
    private static final String ALBUM_ID = "album_id";

    private TextureMapView mMapView;
    private AMap mAMap;
    private Context mContext;
    private ClusterOverlay mClusterOverlay;

    List<LatLng> pointList = new ArrayList<>();//标记位置，用于动态调整地图显示整屏Marker
    List<IClusterItem> clusterItems = new ArrayList<>();//实现点聚合效果


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mClusterOverlay != null) {
            mClusterOverlay.destroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymap, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mMapView = (TextureMapView) getView().findViewById(R.id.mMapView);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(4.0F));
        }
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnCameraChangeListener(this);
        addMarkersToMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        AVGeoPoint point = new AVGeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        query.whereEqualTo("whereCreated", point);
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                String album_id = list.get(0).getObjectId();
                Intent intent = new Intent(getActivity(), AlbumActivity.class);
                intent.putExtra(ALBUM_ID, album_id);
                startActivity(intent);
            }
        });
        return false;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mClusterOverlay.onCameraChangeFinish(cameraPosition);
    }

    private void addMarkersToMap() {
        clusterItems = new ArrayList<IClusterItem>();
        pointList = new ArrayList<LatLng>();
        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        query.whereEqualTo("userId", mCurrentUser.getObjectId());
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                for (int i = 0; i < list.size(); i++) {
                    double lat = list.get(i).getWhereCreated().getLatitude();
                    double lng = list.get(i).getWhereCreated().getLongitude();
                    LatLng latLng = new LatLng(lat, lng);
                    clusterItems.add(new ImgData(latLng, list.get(i).getCover()));
                    pointList.add(latLng);
                }
                Log.e(TAG, "list" + clusterItems);
                mClusterOverlay = new ClusterOverlay(mContext, mAMap, dp2px(80), clusterItems, null);
                //动态调整地图显示整屏Marker
                if (pointList != null && pointList.size() > 0) {
                    if (mAMap == null) {
                        return;
                    }
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    for (int i = 0; i < pointList.size(); i++) {
                        LatLng latLng = pointList.get(i);
                        builder.include(latLng);
                    }
                    LatLngBounds bounds = builder.build();
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }

            }
        });
    }

    static class ImgData implements IClusterItem {

        LatLng latLng;
        String imgStr;

        public ImgData(LatLng latLng, String imgStr) {
            this.latLng = latLng;
            this.imgStr = imgStr;
        }

        @Override
        public LatLng getPosition() {
            return latLng;
        }

        @Override
        public IconRes getIconRes() {
            return new IconRes(imgStr);
        }

        @Override
        public String toString() {
            return "ImgData{" +
                    "latLng=" + latLng +
                    ", imgStr='" + imgStr + '\'' +
                    '}';
        }

    }

    //根据手机的分辨率从dp的单位转成为px(像素)
    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
