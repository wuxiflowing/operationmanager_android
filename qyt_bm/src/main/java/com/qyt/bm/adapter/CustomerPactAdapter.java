package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.InstallTaskModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class CustomerPactAdapter extends BaseRecyclerAdapter<InstallTaskModel> {

    public CustomerPactAdapter(Context mContext, List<InstallTaskModel> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_customerpact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        InstallTaskModel model = mData.get(position);
        viewHolder.pactId.setText("合同ID：216418491");
        viewHolder.pactName.setText("合同名称：2018年押金合同");
        viewHolder.pactSum.setText("合同金额：¥2333");
        viewHolder.pactDate.setText("签约时间：" + model.pact_begin);
        viewHolder.pactBetween.setText("合同时间：" + model.pact_begin + "-" + model.pact_end);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pact_id)
        TextView pactId;
        @BindView(R.id.pact_name)
        TextView pactName;
        @BindView(R.id.pact_sum)
        TextView pactSum;
        @BindView(R.id.pact_date)
        TextView pactDate;
        @BindView(R.id.pact_between)
        TextView pactBetween;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
