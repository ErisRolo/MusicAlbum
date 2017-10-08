package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/5.
 * 首页Item的adapter
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context mContext;
    private List<MusicAlbum> mAlbumList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View homeView;

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            homeView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.mImageView);

        }

    }

    public HomeAdapter(Context context, List<MusicAlbum> albumList) {
        mContext = context;
        mAlbumList = albumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicAlbum musicAlbum = mAlbumList.get(position);
        String url = musicAlbum.getCover();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }
}
