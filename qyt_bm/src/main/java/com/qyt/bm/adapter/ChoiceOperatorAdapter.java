package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.RoundImageView;
import com.bumptech.glide.Glide;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.OperatorItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class ChoiceOperatorAdapter extends BaseRecyclerAdapter<OperatorItem> {

    private String taskDesc;

    public ChoiceOperatorAdapter(Context mContext, String taskDesc, List<OperatorItem> mData) {
        super(mContext, mData);
        this.taskDesc = taskDesc;
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_choiceoperator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setTag(position);
        final OperatorItem model = mData.get(position);
        viewHolder.operatorRegion.setText(model.region);
        viewHolder.operatorName.setText(model.memName);
        viewHolder.operatorDevice.setText(taskDesc + model.taskCount);
        viewHolder.operatorTask.setText("当前任务数量：" + model.totalTaskCount);
        try {
            Glide.with(mContext).load(model.picture.trim()).apply(Constants.REQUEST_OPTIONS).into(viewHolder.operatorHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (position > 0) {
            if (mData.get(position - 1).region.equals(model.region)) {
                viewHolder.operatorRegion.setVisibility(View.GONE);
            } else {
                viewHolder.operatorRegion.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.operatorRegion.setVisibility(View.VISIBLE);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.operator_header)
        RoundImageView operatorHeader;
        @BindView(R.id.operator_name)
        TextView operatorName;
        @BindView(R.id.operator_device)
        TextView operatorDevice;
        @BindView(R.id.operator_task)
        TextView operatorTask;
        @BindView(R.id.operator_region)
        TextView operatorRegion;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
