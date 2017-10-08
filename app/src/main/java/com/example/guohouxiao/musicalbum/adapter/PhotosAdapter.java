package com.example.guohouxiao.musicalbum.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.musicalbum.MyApplication;
import com.example.guohouxiao.musicalbum.R;

import java.util.List;

/**
 * Created by guohouxiao on 2017/9/22.
 * 选择图片的Adapter
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_NORMAL = 1;

    private List<String> mList;
    private View mFooterView;

    private OnRecyClickerListener mListener;

    public interface OnRecyClickerListener {
        void click(View view, int position);
    }

    public void setOnRecyclerViewBottom(OnRecyClickerListener onRecyClickerListener) {
        mListener = onRecyClickerListener;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    public PhotosAdapter(List<String> list) {
        mList = list;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            if (itemView == mFooterView) {
                return;
            }

            mImageView = (ImageView) itemView.findViewById(R.id.mImageView);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ViewHolder) {
                Glide.with(MyApplication.getContext())
                        .load(mList.get(position))
                        .override(100, 100)
                        .centerCrop()
                        .into(holder.mImageView);
            }
        } else {
            if (mListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.click(holder.itemView, holder.getLayoutPosition());
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {

        if (mFooterView == null) {
            return mList.size();
        } else {
            return mList.size() + 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }
}
