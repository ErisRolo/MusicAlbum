package com.example.guohouxiao.musicalbum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.adapter.SubtitlesAdapter;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.EventList;
import com.example.guohouxiao.musicalbum.bean.ImgWithSub;
import com.example.guohouxiao.musicalbum.utils.Presenter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by guohouxiao on 2017/9/22.
 * 添加字幕
 */

public class SubtitlesFragment extends BaseFragment {

    private static final String TAG = "SubtitlesFragment";

    private RecyclerView rv_subtitles;
    private LinearLayoutManager mLayoutManager;
    private SubtitlesAdapter mAdapter;
    private List<ImgWithSub> mImgWithSubs;
    private List<String> mSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subtitles, container, false);

        EventBus.getDefault().register(this);

        findView(view);

        return view;
    }

    private void findView(View view) {

        rv_subtitles = (RecyclerView) view.findViewById(R.id.rv_subtitles);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv_subtitles.setLayoutManager(mLayoutManager);

    }

    public List<String> getContentText() {
        return Presenter.getInstance().getContentText();
    }

    @Subscribe(sticky = true)
    public void onEventMainThread(EventList eventList) {

        mSelected = eventList.getSelected();
        mImgWithSubs = new ArrayList<>();
        for (int i = 0; i < mSelected.size(); i++) {
            Log.i(TAG, mSelected.get(i));
            ImgWithSub imgWithSub = new ImgWithSub(mSelected.get(i));
            mImgWithSubs.add(imgWithSub);
        }
        mAdapter = new SubtitlesAdapter(mImgWithSubs);
        rv_subtitles.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
