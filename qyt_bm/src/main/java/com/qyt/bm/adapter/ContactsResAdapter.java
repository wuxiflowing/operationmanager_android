package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.RoundImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.R;
import com.qyt.bm.model.ContactItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class ContactsResAdapter extends BaseRecyclerAdapter<ContactItem> {

    private RequestOptions requestOptions;

    public ContactsResAdapter(Context mContext, List<ContactItem> mData) {
        super(mContext, mData);
        requestOptions = new RequestOptions().placeholder(R.mipmap.contacts_header).error(R.mipmap.contacts_header);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_contactsres, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setTag(position);
        final ContactItem model = mData.get(position);
        viewHolder.name.setText(model.name);
        viewHolder.mobile.setText(model.farmerAdd);
        if (position < getItemCount() - 1 && mData.get(position + 1).index == model.index) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(model.farmerPic)) {
            Glide.with(mContext).load(model.farmerPic.trim()).apply(requestOptions).into(viewHolder.header);
        } else {
            Glide.with(mContext).load(R.mipmap.contacts_header).into(viewHolder.header);
        }
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contacts_header)
        RoundImageView header;
        @BindView(R.id.contacts_name)
        TextView name;
        @BindView(R.id.contacts_mobile)
        TextView mobile;
        @BindView(R.id.contacts_divider)
        View divider;
        @BindView(R.id.contacts_call)
        LinearLayout call;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
