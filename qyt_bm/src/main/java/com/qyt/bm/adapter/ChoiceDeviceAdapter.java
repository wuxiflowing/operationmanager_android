package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.PondDeviceChoice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class ChoiceDeviceAdapter extends BaseSimpleAdapter<PondDeviceChoice> {

    private boolean isChoice;

    public ChoiceDeviceAdapter(Context mContext, boolean isChoice, List<PondDeviceChoice> mData) {
        super(mContext, mData);
        this.isChoice = isChoice;
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_ponddevicechoice, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PondDeviceChoice model = mData.get(position);
        viewHolder.device_identify.setText(model.identifier);
        viewHolder.deviceType.setText(model.kind);
        if (isChoice) {
            viewHolder.deviceCheck.setVisibility(View.VISIBLE);
            viewHolder.deviceCheck.setChecked(model.isChoice);
        } else {
            viewHolder.deviceCheck.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.device_check)
        CheckBox deviceCheck;
        @BindView(R.id.device_identify)
        TextView device_identify;
        @BindView(R.id.device_type)
        TextView deviceType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
