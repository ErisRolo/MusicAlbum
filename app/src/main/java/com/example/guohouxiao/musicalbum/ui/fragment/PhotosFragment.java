package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.PhotosAdapter;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.EventList;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by guohouxiao on 2017/9/22.
 * 选择图片
 */

public class PhotosFragment extends BaseFragment {

    private static final String TAG = "PhotosFragment";

    private static final int REQUEST_CODE = 733;

    private RecyclerView rv_photos;
    private PhotosAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private List<Uri> mUriList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        findView(view);

        return view;
    }

    private void findView(View view) {

        rv_photos = (RecyclerView) view.findViewById(R.id.rv_photos);
        rv_photos.setLayoutManager(new GridLayoutManager(getContext(), 4));

        mAdapter = new PhotosAdapter(mList);
        mAdapter.setOnRecyclerViewBottom(new PhotosAdapter.OnRecyClickerListener() {
            @Override
            public void click(View view, int position) {
                Matisse.from(getActivity())
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(20)
                        .gridExpectedSize(200)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE);
            }
        });
        rv_photos.setAdapter(mAdapter);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer, rv_photos, false);
        mAdapter.setFooterView(footerView);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            mUriList = Matisse.obtainResult(data);
            for (int i = 0; i < mUriList.size(); i++) {
                String[] mediaData = {MediaStore.Images.Media.DATA};
                Cursor imageCursor = getActivity().managedQuery(mUriList.get(i), mediaData, null, null, null);
                int index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imageCursor.moveToFirst();
                mList.add(imageCursor.getString(index));
            }
        }

        mAdapter.notifyDataSetChanged();
        EventBus.getDefault().postSticky(new EventList(mList));

    }

    public List<String> getList() {
        return mList;
    }


}
