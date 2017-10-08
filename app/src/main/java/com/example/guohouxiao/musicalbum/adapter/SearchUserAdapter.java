package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/10/3.
 * 搜索用户结果Adapter
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUserList;


    static class ViewHolder extends RecyclerView.ViewHolder {

        View userView;

        CircleImageView iv_search_photo;
        TextView tv_search_name;

        public ViewHolder(View itemView) {
            super(itemView);

            userView = itemView;

            iv_search_photo = (CircleImageView) itemView.findViewById(R.id.iv_search_photo);
            tv_search_name = (TextView) itemView.findViewById(R.id.tv_search_name);
        }
    }

    public SearchUserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
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
        User user = mUserList.get(position);
        String url = user.getAvatar();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_search_photo);
        }
        holder.tv_search_name.setText(user.getNickname());

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
