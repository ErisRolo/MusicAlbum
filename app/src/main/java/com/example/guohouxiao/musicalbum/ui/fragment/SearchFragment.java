package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/8.
 * 探索（去看看）
 */

public class SearchFragment extends BaseFragment implements AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, AMap.OnMapClickListener {

    private static final String TAG = "SearchFragment";

    private static final String ALBUM_ID = "album_id";

    private TextureMapView mMapView;
    private AMap mAMap;

    private MyLocationStyle mMyLocationStyle;
    private Marker mMarker;

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
        View view = inflater.inflate(R.layout.fragment_search, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView = (TextureMapView) getView().findViewById(R.id.mMapView);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F));
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(180.0F));
        mAMap.getUiSettings().setZoomControlsEnabled(false);

        mMyLocationStyle = new MyLocationStyle();
        mMyLocationStyle.interval(3000);//设置连续定位模式下的定位间隔
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        mAMap.setMyLocationStyle(mMyLocationStyle);
        mAMap.setMyLocationEnabled(true);

        mAMap.setOnMyLocationChangeListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setOnMapClickListener(this);
    }

    //获取距离当前位置最近的10个相册
    @Override
    public void onMyLocationChange(Location location) {
        AVQuery<MusicAlbum> queryAlbum = AVObject.getQuery(MusicAlbum.class);
        AVGeoPoint point = new AVGeoPoint(location.getLatitude(), location.getLongitude());
        queryAlbum.limit(10);
        queryAlbum.whereEqualTo(Config.ISPUBLIC, true);
        queryAlbum.whereNear(Config.WHERECREATED, point);
        queryAlbum.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> listAlbum, AVException e) {
                if (listAlbum != null) {
                    if (listAlbum.size() != 0) {
                        for (int i = 0; i < listAlbum.size(); i++) {
                            //添加Marker
                            final MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(listAlbum.get(i).getWhereCreated().getLatitude()
                                    , listAlbum.get(i).getWhereCreated().getLongitude());
                            markerOptions.position(latLng);
                            markerOptions.title(listAlbum.get(i).getAlbumname());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker)));
                            AVQuery<User> queryUser = AVObject.getQuery(User.class);
                            queryUser.whereEqualTo("objectId", listAlbum.get(i).getUserId());
                            queryUser.findInBackground(new FindCallback<User>() {
                                @Override
                                public void done(List<User> listUser, AVException e) {
                                    markerOptions.snippet(listUser.get(0).getNickname());
                                    markerOptions.draggable(false);
                                    markerOptions.setFlat(false);
                                    mMarker = mAMap.addMarker(markerOptions);
                                    //添加动画效果
                                    Animation animation = new ScaleAnimation(0f, 1.0f, 0f, 1.0f);
                                    animation.setDuration(1000);
                                    animation.setInterpolator(new BounceInterpolator());
                                    mMarker.setAnimation(animation);
                                    mMarker.startAnimation();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarker = marker;
        mMarker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater(getArguments()).inflate(R.layout.infowindow_item, null);
        TextView tv_album_name = (TextView) infoWindow.findViewById(R.id.tv_album_name);
        tv_album_name.setText(marker.getTitle());
        TextView tv_album_author = (TextView) infoWindow.findViewById(R.id.tv_album_author);
        tv_album_author.setText(marker.getSnippet());
        return infoWindow;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final String album_name = marker.getTitle();
        String album_author = marker.getSnippet();
        AVQuery<User> queryUser = AVObject.getQuery(User.class);
        queryUser.whereEqualTo(Config.NICKNAME, album_author);
        queryUser.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        String userId = list.get(0).getObjectId();
                        AVQuery<MusicAlbum> queryAlbum = AVObject.getQuery(MusicAlbum.class);
                        queryAlbum.whereEqualTo(Config.ALBUMNAME, album_name);
                        queryAlbum.whereEqualTo(Config.USERID, userId);
                        queryAlbum.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                if (list != null) {
                                    if (list.size() != 0) {
                                        String album_id = list.get(0).getObjectId();
                                        Intent intent = new Intent(getContext(), AlbumActivity.class);
                                        intent.putExtra(ALBUM_ID, album_id);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMarker != null) {
            mMarker.hideInfoWindow();
        }
    }
}
