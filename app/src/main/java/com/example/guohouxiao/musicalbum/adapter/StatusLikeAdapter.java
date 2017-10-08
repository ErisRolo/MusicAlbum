package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.StatusLike;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;

import java.util.List;

/**
 * Created by guohouxiao on 2017/10/5.
 * 喜欢的动态Adapter
 */

public class StatusLikeAdapter extends RecyclerView.Adapter<StatusLikeAdapter.ViewHolder> {

    private static final String OTHER_USER_ID = "other_user_id";
    private static final String ALBUM_ID = "album_id";

    private Context mContext;
    private List<StatusLike> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View statusLikeView;
        TextView tv_user;
        TextView tv_album;

        public ViewHolder(View itemView) {
            super(itemView);
            statusLikeView = itemView;
            tv_user = (TextView) itemView.findViewById(R.id.tv_user);
            tv_album = (TextView) itemView.findViewById(R.id.tv_album);
        }
    }

    public StatusLikeAdapter(Context context, List<StatusLike> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_like_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusLike status = mList.get(position);
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID,status.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusLike status = mList.get(position);
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.putExtra(ALBUM_ID,status.getAlbumId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatusLike status = mList.get(position);
        holder.tv_user.setText(status.getNickname());
        holder.tv_album.setText(status.getAlbumname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
