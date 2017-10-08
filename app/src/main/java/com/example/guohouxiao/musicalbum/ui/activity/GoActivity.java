package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.Config;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/18.
 * 去那里
 */

public class GoActivity extends BaseActivity implements AMap.InfoWindowAdapter, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.OnMapClickListener {

    private static final String GO_PLACE = "goplace";
    private static final String GO_LATITUDE = "golatitude";
    private static final String GO_LONGITUDE = "golongitude";
    private String go_place;
    private double latitude;
    private double longitude;
    private LatLng latLng;

    private static final String ALBUM_ID = "album_id";

    private Toolbar mToolbar;
    private TextView tv_go_place;

    private MapView mMapView;
    private AMap mAMap;
    private Marker mMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        initData();
        initView();
        initMap(savedInstanceState);
        setUpMap();
        addMarkersToMap();

    }

    private void initData() {
        go_place = getIntent().getStringExtra(GO_PLACE);
        latitude = getIntent().getDoubleExtra(GO_LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(GO_LONGITUDE, 0);
        latLng = new LatLng(latitude, longitude);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_go_place = (TextView) findViewById(R.id.tv_go_place);
        tv_go_place.setText(go_place);
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mMapView);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F));
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(180.0F));
        mAMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void setUpMap() {
        mAMap.setOnMarkerClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setOnMapClickListener(this);
    }

    private void addMarkersToMap() {
        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        AVGeoPoint point = new AVGeoPoint(latitude, longitude);
        query.limit(10);
        query.whereEqualTo(Config.ISPUBLIC, true);
        query.whereNear(Config.WHERECREATED, point);
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(final List<MusicAlbum> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //添加Marker
                            final MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(list.get(i).getWhereCreated().getLatitude()
                                    , list.get(i).getWhereCreated().getLongitude());
                            markerOptions.position(latLng);
                            markerOptions.title(list.get(i).getAlbumname());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker)));
                            AVQuery<User> queryUser = AVObject.getQuery(User.class);
                            queryUser.whereEqualTo("objectId", list.get(i).getUserId());
                            queryUser.findInBackground(new FindCallback<User>() {
                                @Override
                                public void done(List<User> listUser, AVException e) {
                                    if (listUser != null) {
                                        if (listUser.size() != 0) {
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
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarker = marker;
        mMarker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.infowindow_item, null);
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
                String userId = list.get(0).getObjectId();
                AVQuery<MusicAlbum> queryAlbum = AVObject.getQuery(MusicAlbum.class);
                queryAlbum.whereEqualTo(Config.ALBUMNAME, album_name);
                queryAlbum.whereEqualTo(Config.USERID, userId);
                queryAlbum.findInBackground(new FindCallback<MusicAlbum>() {
                    @Override
                    public void done(List<MusicAlbum> list, AVException e) {
                        String album_id = list.get(0).getObjectId();
                        Intent intent = new Intent(GoActivity.this, AlbumActivity.class);
                        intent.putExtra(ALBUM_ID, album_id);
                        startActivity(intent);
                    }
                });
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
