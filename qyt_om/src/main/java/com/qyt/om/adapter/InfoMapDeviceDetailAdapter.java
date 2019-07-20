package com.qyt.om.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by sunzhibin
 */
public class InfoMapDeviceDetailAdapter extends BaseSimpleAdapter<InfoMap> {

    public InfoMapDeviceDetailAdapter(Context mContext, List<InfoMap> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_infomap14, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InfoMap model = mData.get(position);
        viewHolder.label.setText(model.label);
        viewHolder.value.setText(model.value);
        if (mContext.getString(R.string.connection_mode_select).equals(model.label)) {
            if (mContext.getString(R.string.text_normal).equals(model.value)) {
                viewHolder.value.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                viewHolder.value.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.infomap_label)
        TextView label;
        @BindView(R.id.infomap_value)
        TextView value;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
