package com.qyt.om.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.photos.util.ImageSelect;
import com.bumptech.glide.Glide;
import com.qyt.om.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class PhotosChoiceAdapter extends BaseRecyclerAdapter<String> {

    public PhotosChoiceAdapter(Context context, ArrayList<String> images) {
        super(context, images);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photoschoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position < getItemCount() - 1) {
            Glide.with(mContext).load(mData.get(position)).into(viewHolder.photosPic);
            viewHolder.photosDel.setVisibility(View.VISIBLE);
        } else {
            Glide.with(mContext).load(R.mipmap.btn_star_addstar).into(viewHolder.photosPic);
            viewHolder.photosDel.setVisibility(View.GONE);
        }
        viewHolder.photosDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
                ImageSelect.mSelectedImage.remove(position);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photos_pic)
        ImageView photosPic;
        @BindView(R.id.photos_del)
        ImageView photosDel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
