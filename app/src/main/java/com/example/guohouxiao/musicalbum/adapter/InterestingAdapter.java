package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.activity.OtherUserActivity;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.utils.ShowToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/10/31.
 * 可能感兴趣的人Adapter
 */

public class InterestingAdapter extends RecyclerView.Adapter<InterestingAdapter.ViewHolder> {

    private static final String TAG = "InterestingAdapter";
    private static final String OTHER_USER_ID = "other_user_id";

    private Context mContext;
    private List<User> mList;

    boolean[] isFollow = new boolean[10000];
    User mCurrentUser = User.getCurrentUser(User.class);

    static class ViewHolder extends RecyclerView.ViewHolder {

        View interestingView;

        LinearLayout ll_interesting;
        CircleImageView iv_avatar;
        TextView tv_nickname;
        TextView tv_desc;
        Button btn_follow;

        public ViewHolder(View itemView) {
            super(itemView);

            interestingView = itemView;

            ll_interesting = (LinearLayout) itemView.findViewById(R.id.ll_interesting);
            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_nickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            btn_follow = (Button) itemView.findViewById(R.id.btn_follow);
        }
    }

    public InterestingAdapter(Context context, List<User> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interesting_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.ll_interesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                User user = mList.get(position);
                String userId = user.getObjectId();
                //跳转所选用户的个人中心界面
                Intent intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra(OTHER_USER_ID, userId);
                mContext.startActivity(intent);
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                User user = mList.get(position);
                final String userId = user.getObjectId();

                //如果已关注，点击为取消关注
                if (isFollow[holder.getAdapterPosition()]) {
                    mCurrentUser.unfollowInBackground(userId, new FollowCallback() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                        }

                        @Override
                        protected void internalDone0(Object o, AVException e) {
                            if (e == null) {
                                isFollow[holder.getAdapterPosition()] = false;
                                holder.btn_follow.setText("关注");
                                ShowToast.showShortToast(mContext, "已取消关注该用户。");
                            } else {
                                ShowToast.showShortToast(mContext, "取关失败！");
                            }
                        }
                    });
                    //如果未关注，点击为关注
                } else {
                    mCurrentUser.followInBackground(userId, new FollowCallback() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                        }

                        @Override
                        protected void internalDone0(Object o, AVException e) {
                            if (e == null) {
                                isFollow[holder.getAdapterPosition()] = true;
                                holder.btn_follow.setText("已关注");

                                //通知对方关注了对方
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("type", "StatusFollow");
                                data.put(Config.AVATAR, mCurrentUser.getAvatar());
                                data.put(Config.NICKNAME, mCurrentUser.getNickname());
                                data.put(Config.USERID, mCurrentUser.getObjectId());
                                AVStatus status = AVStatus.createStatusWithData(data);
                                AVStatus.sendPrivateStatusInBackgroud(status, userId, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Log.i(TAG, "Send private status finished.");
                                    }
                                });

                                ShowToast.showShortToast(mContext, "关注成功。");
                            } else {
                                ShowToast.showShortToast(mContext, "关注失败！");
                            }
                        }
                    });
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mList.get(position);
        String url = user.getAvatar();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_avatar);
        }
        holder.tv_nickname.setText(user.getNickname());
        holder.tv_desc.setText(user.getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
