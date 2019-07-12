package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.model.InstallTaskModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class CustomerOrderAdapter extends BaseRecyclerAdapter<InstallTaskModel> {

    public CustomerOrderAdapter(Context mContext, List<InstallTaskModel> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_customerorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        InstallTaskModel model = mData.get(position);
        viewHolder.orderCode.setText("订单编号：21484891");
        viewHolder.orderState.setText("待支付");
        viewHolder.orderRealpay.setText("实付款：¥588");
        ArrayList<String> orderItems = new ArrayList<>();
        orderItems.add("养殖户名称：");
        orderItems.add("合同名称：");
        orderItems.add("合同金额：");
        orderItems.add("签约时间：");
        viewHolder.orderInfo.setAdapter(new OrderItemAdapter(mContext, orderItems));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_code)
        TextView orderCode;
        @BindView(R.id.order_state)
        TextView orderState;
        @BindView(R.id.order_info)
        UnScrollListView orderInfo;
        @BindView(R.id.order_realpay)
        TextView orderRealpay;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
