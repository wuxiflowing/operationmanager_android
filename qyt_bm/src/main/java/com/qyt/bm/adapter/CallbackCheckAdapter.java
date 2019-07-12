package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.model.PondChoiceItem;
import com.qyt.bm.response.RecycleTaskData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class CallbackCheckAdapter extends BaseSimpleAdapter<RecycleTaskData.TabPondsBean> {

    public CallbackCheckAdapter(Context mContext, List<RecycleTaskData.TabPondsBean> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_callback_check, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RecycleTaskData.TabPondsBean model = mData.get(position);
        viewHolder.pondName.setText("鱼塘名称：" + model.ITEM1);
        viewHolder.pondAddress.setText("鱼塘地址：" + model.ITEM2);
        viewHolder.deviceId.setText("设备ID：" + model.ITEM10);
        viewHolder.operatorName.setText("鱼塘联系电话：" + model.ITEM3);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.pond_name)
        TextView pondName;
        @BindView(R.id.pond_address)
        TextView pondAddress;
        @BindView(R.id.device_id)
        TextView deviceId;
        @BindView(R.id.operator_name)
        TextView operatorName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
