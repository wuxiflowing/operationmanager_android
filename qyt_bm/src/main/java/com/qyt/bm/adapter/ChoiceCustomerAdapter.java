package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.RoundImageView;
import com.bumptech.glide.Glide;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.ContactItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class ChoiceCustomerAdapter extends BaseRecyclerAdapter<ContactItem> {

    public ChoiceCustomerAdapter(Context mContext, List<ContactItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_choicecustomer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setTag(position);
        final ContactItem model = mData.get(position);
        viewHolder.name.setText(model.name);
        viewHolder.mobile.setText(model.farmerAdd);
        viewHolder.index.setText(String.valueOf(model.index).toUpperCase());
        if (position > 0 && mData.get(position - 1).index == model.index) {
            viewHolder.index.setVisibility(View.GONE);
        } else {
            viewHolder.index.setVisibility(View.VISIBLE);
        }
        if (position < getItemCount() - 1 && mData.get(position + 1).index == model.index) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }
        try {
            Glide.with(mContext).load(model.farmerPic).apply(Constants.REQUEST_OPTIONS).into(viewHolder.header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contacts_header)
        RoundImageView header;
        @BindView(R.id.contacts_name)
        TextView name;
        @BindView(R.id.contacts_mobile)
        TextView mobile;
        @BindView(R.id.contacts_index)
        TextView index;
        @BindView(R.id.contacts_divider)
        View divider;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
