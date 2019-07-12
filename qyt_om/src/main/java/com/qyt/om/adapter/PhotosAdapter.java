package com.qyt.om.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.om.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class PhotosAdapter extends BaseRecyclerAdapter<String> {

    public PhotosAdapter(Context context, ArrayList<String> images) {
        super(context, images);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext).load(mData.get(position).trim())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.default_file_picture)
                        .error(R.mipmap.default_file_picture))
                .into(viewHolder.proImage);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_photo)
        ImageView proImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnPhotoClickListener {
        void onPhotoClick();
    }
}
