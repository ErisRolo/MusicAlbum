package com.example.guohouxiao.musicalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.bean.User;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guohouxiao on 2017/9/21.
 * 相册适配器
 * 用于列表展示
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static final String ALBUM_ID = "album_id";

    private Context mContext;
    private List<MusicAlbum> mAlbumList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View albumView;

        CircleImageView iv_album_cover;
        TextView tv_album_name;
        TextView tv_album_author;
        TextView tv_album_place;


        public ViewHolder(View itemView) {
            super(itemView);

            albumView = itemView;

            iv_album_cover = (CircleImageView) itemView.findViewById(R.id.iv_album_cover);
            tv_album_name = (TextView) itemView.findViewById(R.id.tv_album_name);
            tv_album_author = (TextView) itemView.findViewById(R.id.tv_album_author);
            tv_album_place = (TextView) itemView.findViewById(R.id.tv_album_place);
        }
    }

    public AlbumAdapter(Context context, List<MusicAlbum> albumList) {
        mContext = context;
        mAlbumList = albumList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.albumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MusicAlbum album = mAlbumList.get(position);
                String album_id = album.getObjectId();
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.putExtra(ALBUM_ID, album_id);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MusicAlbum album = mAlbumList.get(position);
        String url = album.getCover();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_album_cover);
        }
        holder.tv_album_name.setText(album.getAlbumname());
        holder.tv_album_place.setText(album.getPlace());
        AVQuery<User> query = AVObject.getQuery(User.class);
        query.whereEqualTo("objectId", album.getUserId());
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        holder.tv_album_author.setText(list.get(0).getNickname());
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }
}
