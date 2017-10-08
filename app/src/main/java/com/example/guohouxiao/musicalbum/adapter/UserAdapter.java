package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;
import com.example.guohouxiao.musicalbum.utils.L;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/9/1.
 * User适配器
 * 用于关注/粉丝界面
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String OTHER_USER_ID = "other_user_id";

    private Context mContext;
    private List<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View followView;

        CircleImageView iv_avatar;
        TextView tv_nickname;
        TextView tv_desc;

        public ViewHolder(View itemView) {
            super(itemView);

            followView = itemView;

            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_nickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }

    public UserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.followView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                User user = mUserList.get(position);
                String userId = user.getObjectId();
                //跳转所选用户的个人中心界面
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID,userId);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUserList.get(position);
        String url = user.getAvatar();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_avatar);
        }
        holder.tv_nickname.setText(user.getNickname());
        holder.tv_desc.setText(user.getDesc());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
