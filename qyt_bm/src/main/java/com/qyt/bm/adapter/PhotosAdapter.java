package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangqu.lib.base.BaseRecyclerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.R;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class PhotosAdapter extends BaseRecyclerAdapter<String> {

    private RequestOptions requestOptions;

    public PhotosAdapter(Context context, ArrayList<String> images) {
        super(context, images);
        requestOptions = new RequestOptions()
                .placeholder(R.mipmap.default_file_picture)
                .error(R.mipmap.default_file_picture);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String istrurl = mData.get(position).trim();
        if (null == holder || null == istrurl || istrurl.equals("")) {
            return;
        }
        Glide.with(mContext).load(istrurl)
                .apply(requestOptions)
                .into(viewHolder.proImage);
    }

    @Override
    public void clearGlideView(RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder != null) {
            Glide.with(mContext).clear(viewHolder.proImage);
        }
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
