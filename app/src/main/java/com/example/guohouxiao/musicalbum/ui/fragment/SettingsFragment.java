package com.example.guohouxiao.musicalbum.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.ui.activity.LocationActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/9/22.
 * 设置相册具体信息
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private static final int REQUEST_LOCATION = 730;
    private static final int REQUEST_CODE = 731;
    private static final int REQUSET_MUSIC = 732;

    private ImageView iv_choose_cover;
    private ImageView iv_modify_albumname;
    private TextView tv_album_name;
    private Spinner mSpinner;
    private TextView tv_choose_location;
    private Switch mSwitch;
    private TextView tv_permission;

    private ArrayAdapter<String> mAdapter;
    private List<String> modeList = new ArrayList<>();
    private String selectedMode;

    private AlertDialog mDialog;
    private EditText et_albumname;

    private String coverPath;

    private TextView tv_choose_music;
    private String musicPath;

    private String locationName;
    private LatLonPoint locationCoordinate;

    private boolean isPublic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        findView(view);

        initDialog();

        return view;
    }

    private void findView(View view) {

        iv_choose_cover = (ImageView) view.findViewById(R.id.iv_choose_cover);
        iv_modify_albumname = (ImageView) view.findViewById(R.id.iv_modify_albumname);
        tv_album_name = (TextView) view.findViewById(R.id.tv_album_name);
        mSpinner = (Spinner) view.findViewById(R.id.mSpinner);
        tv_choose_location = (TextView) view.findViewById(R.id.tv_choose_location);
        mSwitch = (Switch) view.findViewById(R.id.mSwitch);
        tv_permission = (TextView) view.findViewById(R.id.tv_permission);
        tv_choose_music = (TextView) view.findViewById(R.id.tv_choose_music);

        iv_choose_cover.setOnClickListener(this);
        iv_modify_albumname.setOnClickListener(this);
        tv_choose_location.setOnClickListener(this);
        tv_choose_music.setOnClickListener(this);

        modeList.add("默认主题");
        modeList.add("诗和远方");
        modeList.add("风景如画");
        mAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, modeList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        tv_permission.setText("仅自己可见");
        isPublic = mSwitch.isChecked();
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_permission.setText("所有人可见");
                    isPublic = isChecked;
                } else {
                    tv_permission.setText("仅自己可见");
                    isPublic = isChecked;
                }
            }
        });

    }

    private void initDialog() {
        View dialogAlbumName = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_albumname, null);
        et_albumname = (EditText) dialogAlbumName.findViewById(R.id.et_albumname);
        mDialog = new AlertDialog.Builder(getContext())
                .setView(dialogAlbumName)
                .setTitle("设置相册名称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_album_name.setText(et_albumname.getText().toString());
                    }
                }).setNegativeButton("取消", null)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_choose_cover:
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(20)
                        .gridExpectedSize(200)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE);
                break;
            case R.id.iv_modify_albumname:
                mDialog.show();
                break;
            case R.id.tv_choose_location:
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
                break;
            case R.id.tv_choose_music:
                Intent intentMusic = new Intent(Intent.ACTION_GET_CONTENT);
                intentMusic.setType("audio/*");
                intentMusic.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentMusic, REQUSET_MUSIC);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Glide.with(getContext()).load(Matisse.obtainResult(data).get(0)).override(200, 200).centerCrop().into(iv_choose_cover);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor imageCursor = getActivity().managedQuery(Matisse.obtainResult(data).get(0), proj, null, null, null);
            int index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imageCursor.moveToFirst();
            coverPath = imageCursor.getString(index);

        }

        if (resultCode == REQUEST_LOCATION) {
            locationName = data.getStringExtra("locationName");
            locationCoordinate = data.getParcelableExtra("LatLonPoint");
            tv_choose_location.setText(locationName);
        }

        if (requestCode == REQUSET_MUSIC) {
            if (resultCode == Activity.RESULT_OK) {
                String[] proj = {MediaStore.Audio.Media.DATA};
                Cursor audioCursor = getActivity().managedQuery(data.getData(), proj, null, null, null);
                int index = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                audioCursor.moveToFirst();
                musicPath = audioCursor.getString(index);
                System.out.println(TAG + musicPath);
                tv_choose_music.setText(new File(musicPath).getName());
            }
        }

    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            selectedMode = String.valueOf(arg2 + 1);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    public String getSelectedMode() {
        return selectedMode;
    }

    public String getAlbumsName() {
        return tv_album_name.getText().toString();
    }

    public String getLocationName() {
        return locationName;
    }

    public LatLonPoint getLocationCoordinate() {
        return locationCoordinate;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public String getMusicPath() {
        return musicPath;
    }

}
