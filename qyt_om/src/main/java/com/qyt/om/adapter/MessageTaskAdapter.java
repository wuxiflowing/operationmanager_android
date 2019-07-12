package com.qyt.om.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.qyt.om.R;
import com.qyt.om.response.TaskMessageItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class MessageTaskAdapter extends BaseRecyclerAdapter<TaskMessageItem> {

    public MessageTaskAdapter(Context mContext, List<TaskMessageItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_messagetask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TaskMessageItem model = mData.get(position);
        viewHolder.messageKey.setText(model.keyword);
        viewHolder.messageTime.setText(model.createDate);
        viewHolder.messageContent.setText(model.msgContent);
        if ("N".equals(model.isRead)) {
            viewHolder.flagRead.setImageResource(R.mipmap.flag_noread);
        } else {
            viewHolder.flagRead.setImageResource(R.mipmap.flag_isread);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_key)
        TextView messageKey;
        @BindView(R.id.message_time)
        TextView messageTime;
        @BindView(R.id.message_content)
        TextView messageContent;
        @BindView(R.id.message_detail)
        TextViewPlus messageDetail;
        @BindView(R.id.flag_read)
        ImageView flagRead;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
