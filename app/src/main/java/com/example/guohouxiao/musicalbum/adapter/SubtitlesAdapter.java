package com.example.guohouxiao.musicalbum.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.MyApplication;
import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.bean.ImgWithSub;
import com.example.guohouxiao.musicalbum.utils.L;
import com.example.guohouxiao.musicalbum.utils.Presenter;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/23.
 * 添加字幕的Adapter
 */

public class SubtitlesAdapter extends RecyclerView.Adapter<SubtitlesAdapter.ViewHolder> {

    private Presenter mPresenter;
    private List<ImgWithSub> mImgWithSubs;

    public SubtitlesAdapter(List<ImgWithSub> imgWithSubs) {
        mImgWithSubs = imgWithSubs;
        mPresenter = Presenter.getInstance();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        EditText mEditText;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.mImageView);
            mEditText = (EditText) itemView.findViewById(R.id.mEditText);
        }

        public String getContentText() {
            return mEditText.getText().toString();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtitle_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mPresenter.register(holder, mImgWithSubs.get(position));

        ImgWithSub imgWithSub = mImgWithSubs.get(position);

        Glide.with(MyApplication.getContext())
                .load(imgWithSub.getImg())
                .override(100, 100)
                .centerCrop()
                .into(holder.mImageView);

        L.i("onBindViewHolder:" + position);

        holder.mEditText.setText(mImgWithSubs.get(position).getSub());

        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                L.i("输入过程中执行该方法，" + "文字变化" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.modifyTextContent(holder, s.toString());
                L.i("输入过程中执行该方法，" + position + "----------" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                L.i("输入过程中执行该方法，" + "输入结束");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImgWithSubs.size();
    }


}
