package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.StatusBookmark;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;

import java.util.List;

/**
 * Created by guohouxiao on 2017/10/5.
 * 收藏的动态Adapter
 */

public class StatusBookmarkAdapter extends RecyclerView.Adapter<StatusBookmarkAdapter.ViewHolder> {

    private static final String OTHER_USER_ID = "other_user_id";
    private static final String ALBUM_ID = "album_id";

    private Context mContext;
    private List<StatusBookmark> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View statusBookmarkView;
        TextView tv_user;
        TextView tv_album;

        public ViewHolder(View itemView) {
            super(itemView);

            statusBookmarkView = itemView;
            tv_user = (TextView) itemView.findViewById(R.id.tv_user);
            tv_album = (TextView) itemView.findViewById(R.id.tv_album);
        }
    }

    public StatusBookmarkAdapter(Context context, List<StatusBookmark> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_bookmark_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusBookmark status = mList.get(position);
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID,status.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusBookmark status = mList.get(position);
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.putExtra(ALBUM_ID,status.getAlbumId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatusBookmark status = mList.get(position);
        holder.tv_user.setText(status.getNickname());
        holder.tv_album.setText(status.getAlbumname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
