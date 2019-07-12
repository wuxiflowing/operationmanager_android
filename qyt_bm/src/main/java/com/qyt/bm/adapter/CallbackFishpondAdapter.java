package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.model.PondChoiceItem;
import com.qyt.bm.model.PondDeviceChoice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class CallbackFishpondAdapter extends BaseSimpleAdapter<PondChoiceItem> {

    public CallbackFishpondAdapter(Context mContext, List<PondChoiceItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_callback_fishpond, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PondChoiceItem model = mData.get(position);
        viewHolder.pondName.setText(model.pondName);
        viewHolder.pondAddress.setText(model.pondAddress);
        viewHolder.operatorName.setText(model.maintainKeeper);
        if (model.deviceList != null && model.deviceList.size() > 0) {
            viewHolder.deviceList.setAdapter(new ChoiceDeviceAdapter(mContext, false, model.deviceList));
        }
        return convertView;
    }

    class ViewHolder {
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
            ButterKnife.bind(this, view);
        }
    }
}
