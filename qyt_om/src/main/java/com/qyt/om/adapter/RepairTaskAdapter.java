package com.qyt.om.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.om.R;
import com.qyt.om.model.InstallTaskModel;
import com.qyt.om.response.InstallTaskItem;
import com.qyt.om.response.RepairTaskItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class RepairTaskAdapter extends BaseRecyclerAdapter<RepairTaskItem> {

    public RepairTaskAdapter(Context mContext, List<RepairTaskItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_repairtask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final RepairTaskItem model = mData.get(position);
        viewHolder.installCustomerName.setText(model.farmerName);
        viewHolder.installCustomerMobile.setText(model.farmerPhone);
        viewHolder.installPondAddress.setText(model.pondAddr);
        List<String> infos = new ArrayList<>();
        infos.add("鱼塘名称：" + model.pondsName);
        if (TextUtils.isEmpty(model.txtMonMembName)) {
            infos.add("养殖管家：" + model.txtHKName);
        } else {
            infos.add("监控人员：" + model.txtMonMembName);
        }
        infos.add("设备型号：" + model.repairEqpKind);
        infos.add("故障描述：" + model.maintenDetail);
        switch (model.tskType) {
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
        viewHolder.repairInfo.setAdapter(new OrderItemAdapter(mContext, infos));
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
        @BindView(R.id.repair_info)
        UnScrollListView repairInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
