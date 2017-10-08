package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/10/4.
 * 搜索相册结果Adapter
 */

public class SearchAlbumAdapter extends RecyclerView.Adapter<SearchAlbumAdapter.ViewHolder> {

    private Context mContext;
    private List<MusicAlbum> mAlbumList;


    static class ViewHolder extends RecyclerView.ViewHolder {

        View albumView;

        CircleImageView iv_search_photo;
        TextView tv_search_name;

        public ViewHolder(View itemView) {
            super(itemView);

            albumView = itemView;

            iv_search_photo = (CircleImageView) itemView.findViewById(R.id.iv_search_photo);
            tv_search_name = (TextView) itemView.findViewById(R.id.tv_search_name);
        }
    }

    public SearchAlbumAdapter(Context context, List<MusicAlbum> albumList) {
        mContext = context;
        mAlbumList = albumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicAlbum album = mAlbumList.get(position);
        String url = album.getCover();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_search_photo);
        }
        holder.tv_search_name.setText(album.getAlbumname());
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }
}
