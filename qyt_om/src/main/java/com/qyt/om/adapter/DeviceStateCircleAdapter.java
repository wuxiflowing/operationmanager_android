package com.qyt.om.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.om.R;
import com.qyt.om.model.InfoMap;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceStateCircleAdapter extends BaseSimpleAdapter<InfoMap> {

    public DeviceStateCircleAdapter(Context mContext, List<InfoMap> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_devicestatecircle, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InfoMap model = mData.get(position);
        viewHolder.stateLabel.setText(model.label);
        viewHolder.stateValue.setText(model.value);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.state_value)
        TextView stateValue;
        @BindView(R.id.state_label)
        TextView stateLabel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
