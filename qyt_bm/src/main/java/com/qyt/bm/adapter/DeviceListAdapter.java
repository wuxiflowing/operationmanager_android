package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.DeviceChoiceModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceListAdapter extends BaseSimpleAdapter<DeviceChoiceModel> {

    public DeviceListAdapter(Context mContext, List<DeviceChoiceModel> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_devicelist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DeviceChoiceModel model = mData.get(position);
        viewHolder.deviceName.setText(model.deviceTypeName);
        viewHolder.deviceCount.setText(model.deviceTypeId + "套");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_count)
        TextView deviceCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
