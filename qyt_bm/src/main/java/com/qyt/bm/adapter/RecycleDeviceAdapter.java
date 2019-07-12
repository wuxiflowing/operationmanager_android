package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.model.PondChoiceItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class RecycleDeviceAdapter extends BaseRecyclerAdapter<PondChoiceItem> {

    public RecycleDeviceAdapter(Context mContext, List<PondChoiceItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycledevice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setTag(position);
        final PondChoiceItem model = mData.get(position);
        viewHolder.pondName.setText(model.pondName);
        viewHolder.pondAddress.setText(model.pondAddress);
        viewHolder.operatorName.setText(model.maintainKeeper);
        if (model.deviceList != null && model.deviceList.size() > 0) {
            final ChoiceDeviceAdapter deviceAdapter = new ChoiceDeviceAdapter(mContext, true, model.deviceList);
            viewHolder.deviceList.setAdapter(deviceAdapter);
            viewHolder.deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    model.deviceList.get(position).changeState();
                    deviceAdapter.notifyDataSetChanged();
                }
            });
        }
        viewHolder.choiceOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemOpera("switch", position, model);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pond_name)
        TextView pondName;
        @BindView(R.id.pond_address)
        TextView pondAddress;
        @BindView(R.id.device_list)
        UnScrollListView deviceList;
        @BindView(R.id.operator_name)
        TextViewPlus operatorName;
        @BindView(R.id.choice_operator)
        RelativeLayout choiceOperator;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
