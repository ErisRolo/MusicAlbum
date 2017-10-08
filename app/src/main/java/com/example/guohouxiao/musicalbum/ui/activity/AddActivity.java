package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.fragment.PhotosFragment;
import com.example.guohouxiao.musicalbum.ui.fragment.SettingsFragment;
import com.example.guohouxiao.musicalbum.ui.fragment.SubtitlesFragment;
import com.example.guohouxiao.musicalbum.utils.CompressHelper;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.utils.Crop;
import com.example.guohouxiao.musicalbum.utils.Presenter;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guohouxiao on 2017/7/19.
 * 新建相册
 */

public class AddActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddActivity";
    public static final int UPDATE_TEXT = 1;

    private static final int ADD_RESULT_CODE = 10000;

    private Toolbar mToolbar;
    private ImageView iv_finish;

    private ProgressBar mProgressBar;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<BaseFragment> mList;
    private PhotosFragment mPhotosFragment;
    private SubtitlesFragment mSubtitlesFragment;
    private SettingsFragment mSettingsFragment;
    private MyAdapter mAdapter;
    private String[] titles = {"照片", "字幕", "设置"};

    private List<String> mSelected;
    private List<String> filepath;
    private List<File> loadFile;

    private AVFile file;
    private AVFile musicFile;
    private List<AVFile> mFileList = new ArrayList<>();

    private int count = 0;
    private List<String> mSubtitles;
    private List<String> photosUrl = new ArrayList<>();

    private String musicKey;
    private String playMode;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    setResult(ADD_RESULT_CODE);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mPhotosFragment = new PhotosFragment();
        mSubtitlesFragment = new SubtitlesFragment();
        mSettingsFragment = new SettingsFragment();

        initView();
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

        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);

        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mList = new ArrayList<>();
        mList.add(mPhotosFragment);
        mList.add(mSubtitlesFragment);
        mList.add(mSettingsFragment);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                if (mSettingsFragment.getMusicPath() != null) {
                    addFinish();
                } else {
                    ShowToast.showShortToast(this, "忘记选择音乐了！");
                }
                break;
        }
    }

    //添加完成后执行
    private void addFinish() {
        mProgressBar.setVisibility(View.VISIBLE);

        playMode = mSettingsFragment.getSelectedMode();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mSelected = mPhotosFragment.getList();
                Crop crop = new Crop(mSelected);
                switch (playMode) {
                    case "1":
                        filepath = crop.cropmr();
                        break;
                    case "2":
                        filepath = crop.cropshyf();
                        break;
                    case "3":
                        filepath = crop.cropfjrh();
                        break;
                    case "4":

                        break;
                    case "5":

                        break;
                    case "6":

                        break;
                    default:
                        break;
                }

                loadFile = new ArrayList<File>();
                for (int i = 0; i < filepath.size(); i++) {
                    loadFile.add(CompressHelper.getDefault(AddActivity.this).compressToFile(new File(filepath.get(i))));
                }
                upLoad(loadFile);
            }
        }).start();
    }

    //上传文件
    private void upLoad(final List<File> mFiles) {
        for (int i = 0; i < mFiles.size(); i++) {
            try {
                file = AVFile.withFile(mFiles.get(i).getName(), mFiles.get(i));
                mFileList.add(file);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        createAlbum(mFiles);
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //创建相册
    public synchronized void createAlbum(List<File> mFiles) {

        count++;

        if (count == mFiles.size()) {
            mSubtitles = Presenter.getInstance().getContentText();
            for (int i = 0; i < mFileList.size(); i++) {
                photosUrl.add(mFileList.get(i).getUrl().split("m/")[1]);
            }
            if (mSettingsFragment.getMusicPath() != null) {
                try {
                    musicFile = AVFile.withFile(new File(mSettingsFragment.getMusicPath()).getName(),
                            new File(mSettingsFragment.getMusicPath()));
                    musicFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            musicKey = musicFile.getUrl().split("m/")[1];
                            final MusicAlbum musicAlbum = new MusicAlbum();
                            musicAlbum.setUserId(mCurrentUser.getObjectId());
                            musicAlbum.setAlbumname(mSettingsFragment.getAlbumsName());
                            musicAlbum.setPlace(mSettingsFragment.getLocationName());
                            AVGeoPoint point = new AVGeoPoint(mSettingsFragment.getLocationCoordinate().getLatitude()
                                    , mSettingsFragment.getLocationCoordinate().getLongitude());
                            musicAlbum.setWhereCreated(point);
                            musicAlbum.setPlaymode(playMode);
                            musicAlbum.setMusic(musicKey);
                            musicAlbum.setPhotos(photosUrl);
                            musicAlbum.setSubtitles(mSubtitles);
                            musicAlbum.setIsPublic(mSettingsFragment.getIsPublic());
                            musicAlbum.setLikenumber("0");
                            if (mSettingsFragment.getCoverPath() != null) {
                                File file = new File(mSettingsFragment.getCoverPath());
                                try {
                                    final AVFile coverFile = AVFile.withFile(file.getName(), file);
                                    coverFile.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            musicAlbum.setCover(coverFile.getUrl());
                                            musicAlbum.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e == null) {
                                                        Message message = new Message();
                                                        message.what = UPDATE_TEXT;
                                                        handler.sendMessage(message);

                                                        //如果相册是公开的
                                                        if (mSettingsFragment.getIsPublic()) {
                                                            try {
                                                                AVQuery<User> followerQuery = mCurrentUser.followerQuery(User.class);
                                                                followerQuery.findInBackground(new FindCallback<User>() {
                                                                    @Override
                                                                    public void done(List<User> list, AVException e) {
                                                                        //如果粉丝数不为0
                                                                        if (list != null) {
                                                                            if (list.size() != 0) {
                                                                                //给粉丝发送状态
                                                                                Map<String, Object> data = new HashMap<String, Object>();
                                                                                data.put("type", "StatusAdd");
                                                                                if (mCurrentUser.getAvatar() == null) {
                                                                                    data.put(Config.AVATAR, "");
                                                                                } else {
                                                                                    data.put(Config.AVATAR, mCurrentUser.getAvatar());
                                                                                }
                                                                                data.put(Config.USERID, mCurrentUser.getObjectId());
                                                                                data.put(Config.NICKNAME, mCurrentUser.getNickname());
                                                                                data.put(Config.PLACE, mSettingsFragment.getLocationName());
                                                                                data.put(Config.COVER, coverFile.getUrl());
                                                                                data.put(Config.ALBUMNAME, mSettingsFragment.getAlbumsName());
                                                                                data.put("album_id", musicAlbum.getObjectId());
                                                                                AVGeoPoint point = new AVGeoPoint(mSettingsFragment.getLocationCoordinate().getLatitude()
                                                                                        , mSettingsFragment.getLocationCoordinate().getLongitude());
                                                                                data.put(Config.WHERECREATED, point);
                                                                                AVStatus status = AVStatus.createStatusWithData(data);
                                                                                AVStatus.sendStatusToFollowersInBackgroud(status, new SaveCallback() {
                                                                                    @Override
                                                                                    public void done(AVException e) {
                                                                                        Log.i(TAG, "Send status finished.");
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            } catch (AVException e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 730) {
            mSettingsFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 731) {
            mSettingsFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 732) {
            mSettingsFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 733) {
            mPhotosFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
