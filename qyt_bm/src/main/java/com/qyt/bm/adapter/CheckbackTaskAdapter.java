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
import com.qyt.bm.response.CheckRecycle;
import com.qyt.bm.response.RecycleItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class CheckbackTaskAdapter extends BaseRecyclerAdapter<CheckRecycle> {

    public CheckbackTaskAdapter(Context mContext, List<CheckRecycle> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_checkbacktask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final CheckRecycle model = mData.get(position);
        viewHolder.installCustomerName.setText(model.txtFarmerName);
        viewHolder.installCustomerMobile.setText(model.txtFarmerPhone);
        List<String> infos = new ArrayList<>();
        switch (model.tskType) {
            case "check":
                viewHolder.installState.setText("待我审核");
                infos.add("发起人：" + model.txtCSMembName);
                infos.add("审核人：" + model.txtHK);
                break;
            case "sign":
                viewHolder.installState.setText("待审核");
                infos.add("发起人：" + model.txtHK);
                infos.add("审核人：" + model.txtCenMagName);
                break;
        }
        infos.add("发起时间：" + model.txtStartDate);
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
                }
            }
        };
        viewHolder.installCustomerMobile.setOnClickListener(onClickListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.install_customer_name)
        TextView installCustomerName;
        @BindView(R.id.install_customer_mobile)
        TextViewPlus installCustomerMobile;
        @BindView(R.id.install_state)
        TextViewPlus installState;
        @BindView(R.id.recycle_info)
        UnScrollListView recycleInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
