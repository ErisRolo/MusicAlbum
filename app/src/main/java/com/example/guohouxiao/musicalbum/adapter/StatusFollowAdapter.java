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
import com.example.guohouxiao.musicalbum.bean.StatusFollow;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/10/5.
 * 关注的动态Adapter
 */

public class StatusFollowAdapter extends RecyclerView.Adapter<StatusFollowAdapter.ViewHolder> {

    private static final String OTHER_USER_ID = "other_user_id";

    private Context mContext;
    private List<StatusFollow> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View statusFollowView;

        LinearLayout ll_follow;
        CircleImageView iv_avatar;
        TextView tv_user;

        public ViewHolder(View itemView) {
            super(itemView);

            statusFollowView = itemView;
            ll_follow = (LinearLayout) itemView.findViewById(R.id.ll_follow);
            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_user = (TextView) itemView.findViewById(R.id.tv_user);
        }
    }

    public StatusFollowAdapter(Context context, List<StatusFollow> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_follow_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.ll_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                StatusFollow status = mList.get(position);
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID, status.getUserId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatusFollow status = mList.get(position);
        String avatar = status.getAvatar();
        if (avatar != null) {
            Glide.with(mContext).load(avatar).into(holder.iv_avatar);
        }
        holder.tv_user.setText(status.getNickname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
