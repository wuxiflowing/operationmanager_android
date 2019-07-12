package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.InfoMap;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
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
