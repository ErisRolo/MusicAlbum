package com.example.guohouxiao.musicalbum.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseFragment;
import com.example.guohouxiao.musicalbum.bean.MusicAlbum;
import com.example.guohouxiao.musicalbum.ui.activity.AddActivity;
import com.example.guohouxiao.musicalbum.ui.activity.AlbumActivity;
import com.example.guohouxiao.musicalbum.ui.activity.SearchActivity;
import com.example.guohouxiao.musicalbum.utils.Config;
import com.example.guohouxiao.musicalbum.widget.FadeTransitionImageView;
import com.example.guohouxiao.musicalbum.widget.PileLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guohouxiao on 2017/7/18.
 * 首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private static final int ADD_RESULT_CODE = 10000;

    private static final String ALBUM_ID = "album_id";
    private String albumId;

    private LinearLayout ll_search;

    private PileLayout mPileLayout;
    private List<MusicAlbum> mList = new ArrayList<>();
    private PileLayout.Adapter mAdapter;

    private AlertDialog mDialog;

    private TextView tv_album_name;
    private TextView tv_place;
    private ImageView iv_add;
    private ImageView iv_remove;
    private FadeTransitionImageView bottomImageView;
    //private int lastDisplay = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        findView(view);

        initDialog();

        return view;
    }

    private void findView(View view) {

        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);

        mPileLayout = (PileLayout) view.findViewById(R.id.mPileLayout);
        mAdapter = new PileLayout.Adapter() {
            @Override
            public int getLayoutId() {
                return R.layout.home_item;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder == null) {
                    holder = new ViewHolder();
                    holder.imageView = (ImageView) view.findViewById(R.id.imageView);
                    view.setTag(holder);
                }
                Glide.with(getActivity()).load(mList.get(position).getCover()).into(holder.imageView);
            }

            @Override
            public void displaying(int position) {
                albumId = mList.get(position).getObjectId();
                tv_album_name.setText(mList.get(position).getAlbumname());
                tv_place.setText(mList.get(position).getPlace());
                bottomImageView.firstInit("http://ac-xc4xbn7q.clouddn.com/" + mList.get(position).getPhotos().get(0));
  /*              if (lastDisplay < 0) {
                    bottomImageView.firstInit("http://ac-xc4xbn7q.clouddn.com/" + mList.get(position).getPhotos().get(0));
                    lastDisplay = 0;
                } else if (lastDisplay != position) {
                    bottomImageView.saveNextPosition(position, "http://ac-xc4xbn7q.clouddn.com/" + mList.get(position).getPhotos().get(0));
                    lastDisplay = position;
                }*/
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AlbumActivity.class);
                intent.putExtra(ALBUM_ID, mList.get(position).getObjectId());
                startActivity(intent);
            }
        };

        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
        query.whereEqualTo(Config.USERID, mCurrentUser.getObjectId());
        query.findInBackground(new FindCallback<MusicAlbum>() {
            @Override
            public void done(List<MusicAlbum> list, AVException e) {
                if (list != null) {
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mList.add(list.get(i));
                        }

                        mPileLayout.setAdapter(mAdapter);

                    }
                }
            }
        });

        tv_album_name = (TextView) view.findViewById(R.id.tv_album_name);
        tv_place = (TextView) view.findViewById(R.id.tv_place);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_remove = (ImageView) view.findViewById(R.id.iv_remove);
        bottomImageView = (FadeTransitionImageView) view.findViewById(R.id.bottomImageView);

        iv_add.setOnClickListener(this);
        iv_remove.setOnClickListener(this);

    }

    private void initDialog() {
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_remove_album, null);
        mDialog = new AlertDialog.Builder(getContext())
                .setView(dialog)
                .setTitle("是否删除相册")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
                        query.whereEqualTo("objectId", albumId);
                        query.findInBackground(new FindCallback<MusicAlbum>() {
                            @Override
                            public void done(List<MusicAlbum> list, AVException e) {
                                list.get(0).deleteInBackground();
                                mList.remove(list.get(0));
                                mPileLayout.notifyDataSetChanged();
                                tv_album_name.setText("");
                                tv_place.setText("");
                                bottomImageView.firstInit("");
                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.iv_add:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(intent, ADD_RESULT_CODE);
                break;
            case R.id.iv_remove:
                mDialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_RESULT_CODE) {
            mList.clear();
            AVQuery<MusicAlbum> query = AVObject.getQuery(MusicAlbum.class);
            query.whereEqualTo(Config.USERID, mCurrentUser.getObjectId());
            query.findInBackground(new FindCallback<MusicAlbum>() {
                @Override
                public void done(List<MusicAlbum> list, AVException e) {
                    if (list != null) {
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i));
                            }

                            mAdapter = new PileLayout.Adapter() {
                                @Override
                                public int getLayoutId() {
                                    return R.layout.home_item;
                                }

                                @Override
                                public int getItemCount() {
                                    return mList.size();
                                }
                            };
                            mPileLayout.notifyDataSetChanged();
                            bottomImageView.firstInit("");
                        }
                    }
                }
            });
        }
    }

    class ViewHolder {
        ImageView imageView;
    }


}
