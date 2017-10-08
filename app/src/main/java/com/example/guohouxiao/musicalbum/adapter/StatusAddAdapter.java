package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.StatusAdd;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.GoActivity;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/10/5.
 * 新的动态Adaptet
 */

public class StatusAddAdapter extends RecyclerView.Adapter<StatusAddAdapter.ViewHolder> {

    private static final String OTHER_USER_ID = "other_user_id";

    private static final String GO_PLACE = "goplace";
    private static final String GO_LATITUDE = "golatitude";
    private static final String GO_LONGITUDE = "golongitude";

    private static final String ALBUM_ID = "album_id";

    private Context mContext;
    private List<StatusAdd> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View statusAddView;

        CircleImageView iv_avatar;
        TextView tv_album_author;
        TextView tv_album_place;
        LinearLayout ll_album;
        CircleImageView iv_album_cover;
        TextView tv_album_name;

        public ViewHolder(View itemView) {
            super(itemView);

            statusAddView = itemView;

            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_album_author = (TextView) itemView.findViewById(R.id.tv_album_author);
            tv_album_place = (TextView) itemView.findViewById(R.id.tv_album_place);
            ll_album = (LinearLayout) itemView.findViewById(R.id.ll_album);
            iv_album_cover = (CircleImageView) itemView.findViewById(R.id.iv_album_cover);
            tv_album_name = (TextView) itemView.findViewById(R.id.tv_album_name);
        }

    }

    public StatusAddAdapter(Context context, List<StatusAdd> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_add_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.tv_album_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusAdd status = mList.get(position);
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID, status.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.tv_album_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusAdd status = mList.get(position);
                Intent intent = new Intent(mContext, GoActivity.class);
                intent.putExtra(GO_PLACE, status.getPlace());
                intent.putExtra(GO_LATITUDE, status.getPoint().getLatitude());
                intent.putExtra(GO_LONGITUDE, status.getPoint().getLongitude());
                mContext.startActivity(intent);
            }
        });
        holder.ll_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusAdd status = mList.get(position);
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.putExtra(ALBUM_ID, status.getAlbumId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatusAdd status = mList.get(position);
        String avatar = status.getAvatar();
        if (avatar != null) {
            Glide.with(mContext).load(avatar).into(holder.iv_avatar);
        }
        holder.tv_album_author.setText(status.getNickname());
        holder.tv_album_place.setText(status.getPlace());
        String cover = status.getCover();
        if (cover != null) {
            Glide.with(mContext).load(cover).into(holder.iv_album_cover);
        }
        holder.tv_album_name.setText(status.getAlbumname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
