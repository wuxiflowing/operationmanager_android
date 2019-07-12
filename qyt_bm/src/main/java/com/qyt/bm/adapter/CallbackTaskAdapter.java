package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.response.RecycleItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class CallbackTaskAdapter extends BaseRecyclerAdapter<RecycleItem> {

    public CallbackTaskAdapter(Context mContext, List<RecycleItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_callbacktask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final RecycleItem model = mData.get(position);
        viewHolder.installCustomerName.setText(model.farmerName);
        viewHolder.installCustomerMobile.setText(model.farmerPhone);
        viewHolder.installPondAddress.setText("鱼塘地址：" + model.pondAddr);
        List<String> infos = new ArrayList<>();
        infos.add("鱼塘名称：" + model.pondsName);
        infos.add("设备ID：" + model.deviceID);
        if ("check".equals(model.tskType)) {

        }
        infos.add("运维管家：" + model.matnerMembName);
        switch (model.tskType) {
            case "check":
                viewHolder.installState.setText("待审核");
                infos.add("派单时间：" + model.calDT);
                break;
            case "prepare":
                viewHolder.installState.setText("待接单");
                infos.add("派单时间：" + model.calDT);
                break;
            case "ing":
                viewHolder.installState.setText("进行中");
                infos.add("接单时间：" + model.calOT);
                break;
            case "finish":
                viewHolder.installState.setText("已完成");
                infos.add("完成时间：" + model.calCT);
                break;
        }
        viewHolder.recycleInfo.setAdapter(new OrderItemAdapter(mContext, infos));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.install_customer_mobile:
                        if (recyclerViewItemClickListener != null) {
                            recyclerViewItemClickListener.onItemOpera("call", position, model);
                        }
                        break;
                    case R.id.install_pond_address:
                        if (recyclerViewItemClickListener != null) {
                            recyclerViewItemClickListener.onItemOpera("navi", position, model);
                        }
                        break;
                }
            }
        };
        viewHolder.installCustomerMobile.setOnClickListener(onClickListener);
        viewHolder.installPondAddress.setOnClickListener(onClickListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.install_customer_name)
        TextView installCustomerName;
        @BindView(R.id.install_customer_mobile)
        TextViewPlus installCustomerMobile;
        @BindView(R.id.install_state)
        TextViewPlus installState;
        @BindView(R.id.install_pond_address)
        TextViewPlus installPondAddress;
        @BindView(R.id.recycle_info)
        UnScrollListView recycleInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
