package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.SearchAlbumAdapter;
import com.example.guohouxiao.musicalbum.adapter.SearchUserAdapter;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/10/3.
 * 搜索结果
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private static final String GO_PLACE = "goplace";
    private static final String GO_LATITUDE = "golatitude";
    private static final String GO_LONGITUDE = "golongitude";
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private GeocodeSearch geocoderSearch;

    private static final String SEARCH_RESULT = "searchresult";
    private String search;

    private Toolbar mToolbar;

    private LinearLayout ll_user;
    private LinearLayout ll_album;
    private LinearLayout ll_place;

    private RecyclerView rv_user;
    private TextView tv_nosearch_user;
    private LinearLayoutManager userLayoutManager;
    private SearchUserAdapter mUserAdapter;
    private List<User> mUserList = new ArrayList<>();

    private RecyclerView rv_album;
    private TextView tv_nosearch_album;
    private LinearLayoutManager albumLayoutManager;
    private SearchAlbumAdapter mAlbumAdapter;
    private List<MusicAlbum> mAlbumList = new ArrayList<>();

    private MapView mapView;
    private AMap mAMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        search = getIntent().getStringExtra(SEARCH_RESULT);

        initView();
        initMap(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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

        rv_user = (RecyclerView) findViewById(R.id.rv_user);
        tv_nosearch_user = (TextView) findViewById(R.id.tv_nosearch_user);
        userLayoutManager = new LinearLayoutManager(this);
        userLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_user.setLayoutManager(userLayoutManager);
        setUserAdapter();

        rv_album = (RecyclerView) findViewById(R.id.rv_album);
        tv_nosearch_album = (TextView) findViewById(R.id.tv_nosearch_album);
        albumLayoutManager = new LinearLayoutManager(this);
        albumLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_album.setLayoutManager(albumLayoutManager);
        setAlbumAdapter();

        ll_user = (LinearLayout) findViewById(R.id.ll_user);
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_place = (LinearLayout) findViewById(R.id.ll_place);

        ll_user.setOnClickListener(this);
        ll_album.setOnClickListener(this);
        ll_place.setOnClickListener(this);

    }


    private void initMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mAMap = mapView.getMap();
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        GeocodeQuery query = new GeocodeQuery(search,"");
        geocoderSearch.getFromLocationNameAsyn(query);

    }

    private void setUserAdapter() {
        AVQuery<User> query = AVObject.getQuery(User.class);
        query.whereContains(Config.NICKNAME, search);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        mUserList.add(list.get(i));
                        mUserAdapter = new SearchUserAdapter(SearchResultActivity.this, mUserList);
                        rv_user.setAdapter(mUserAdapter);
                    }
                } else {
                    tv_nosearch_user.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setAlbumAdapter() {
        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        query.whereContains(Config.ALBUMNAME, search);
        query.whereEqualTo(Config.ISPUBLIC, true);
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        mAlbumList.add(list.get(i));
                        mAlbumAdapter = new SearchAlbumAdapter(SearchResultActivity.this, mAlbumList);
                        rv_album.setAdapter(mAlbumAdapter);
                    }
                } else {
                    tv_nosearch_album.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user:
                Intent userIntent = new Intent(this, SearchUserActivity.class);
                userIntent.putExtra(SEARCH_RESULT, search);
                startActivity(userIntent);
                break;
            case R.id.ll_album:
                Intent albumIntent = new Intent(this, SearchAlbumActivity.class);
                albumIntent.putExtra(SEARCH_RESULT, search);
                startActivity(albumIntent);
                break;
            case R.id.ll_place:
                Intent intent = new Intent(this, GoActivity.class);
                intent.putExtra(GO_PLACE, search);
                intent.putExtra(GO_LATITUDE, latitude);
                intent.putExtra(GO_LONGITUDE, longitude);
                startActivity(intent);
                break;
        }
    }

    /**
     * 逆地理编码回调
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";
                ShowToast.showShortToast(SearchResultActivity.this, addressName);
            }
        }

    }

    /**
     * 地理编码查询回调
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                latitude = address.getLatLonPoint().getLatitude();
                longitude = address.getLatLonPoint().getLongitude();
                latLng = new LatLng(latitude, longitude);
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F));
                mAMap.moveCamera(CameraUpdateFactory.changeTilt(180.0F));
                mAMap.getUiSettings().setZoomControlsEnabled(false);
            } else {
                ShowToast.showShortToast(SearchResultActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else if (rCode == 27) {
            ShowToast.showShortToast(SearchResultActivity.this, "搜索失败,请检查网络连接！");
        } else if (rCode == 32) {
            ShowToast.showShortToast(SearchResultActivity.this, "key验证无效！");
        } else {
            ShowToast.showShortToast(SearchResultActivity.this, "未知错误，请稍后重试!错误码为:" + rCode);
        }
    }
}
