package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.RoundImageView;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.MessageModel;
import com.qyt.bm.response.MessageItem;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class MessageAdapter extends BaseRecyclerAdapter<MessageItem> {

    public MessageAdapter(Context mContext, List<MessageItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MessageItem model = mData.get(position);
        viewHolder.text.setText(model.keyword);
        viewHolder.time.setText(model.createDate);
        if (Constants.TASK_ICONS.keySet().contains(model.messageType)) {
            viewHolder.icon.setImageResource(Constants.TASK_ICONS.get(model.messageType));
        }
        if (Constants.TASK_TITLES.keySet().contains(model.messageType)) {
            viewHolder.type.setText(Constants.TASK_TITLES.get(model.messageType));
        }
        if (model.count > 0) {
            if ("sys".equals(model.messageType)) {
                viewHolder.notice.setVisibility(View.VISIBLE);
                viewHolder.count.setVisibility(View.GONE);
            }else {
                viewHolder.count.setText(model.count + "");
                viewHolder.count.setVisibility(View.VISIBLE);
                viewHolder.notice.setVisibility(View.GONE);
            }
        } else {
            viewHolder.count.setVisibility(View.GONE);
            viewHolder.notice.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_icon)
        RoundImageView icon;
        @BindView(R.id.message_type)
        TextView type;
        @BindView(R.id.message_time)
        TextView time;
        @BindView(R.id.message_text)
        TextView text;
        @BindView(R.id.message_count)
        TextView count;
        @BindView(R.id.message_notice)
        TextView notice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
