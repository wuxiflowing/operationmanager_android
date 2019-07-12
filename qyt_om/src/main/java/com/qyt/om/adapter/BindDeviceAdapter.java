package com.qyt.om.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.widget.UnScrollGridView;
import com.qyt.om.R;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.model.InfoMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class BindDeviceAdapter extends BaseSimpleAdapter<BindDeviceInfo> {

    public void setListItemOperaListener(ListItemOperaListener listItemOperaListener) {
        this.listItemOperaListener = listItemOperaListener;
    }

    private ListItemOperaListener listItemOperaListener;

    public BindDeviceAdapter(Context mContext, List<BindDeviceInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_binddevice, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BindDeviceInfo model = mData.get(position);
        viewHolder.deviceFishpond.setText(model.pondName);
        viewHolder.deviceId.setText("设备ID：" + model.deviceIdentifier);
        viewHolder.deviceType.setText("型号：" + model.deviceModel);
        viewHolder.deviceState.setText(model.state);
        List<InfoMap> infoMaps = new ArrayList<>();
        infoMaps.add(new InfoMap("溶氧值", model.oxyValue));
        infoMaps.add(new InfoMap("温度", model.temperature));
        if (model != null && !TextUtils.isEmpty(model.ph)) {
            try {
                float ph = Float.parseFloat(model.ph);
                if (ph < 0) {
                    infoMaps.add(new InfoMap("PH值", "--"));
                } else {
                    infoMaps.add(new InfoMap("PH值", model.ph + ""));
                }
            } catch (NumberFormatException e) {
                infoMaps.add(new InfoMap("PH值", "--"));
            }
        } else {
            infoMaps.add(new InfoMap("PH值", "--"));
        }
        infoMaps.add(new InfoMap("控制1状态", model.control1));
        infoMaps.add(new InfoMap("控制2状态", model.control2));
        infoMaps.add(new InfoMap("控制方式", model.automatic));
        viewHolder.deviceGrid.setAdapter(new DeviceStateAdapter(mContext, infoMaps));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_del:
                        if (listItemOperaListener != null) {
                            listItemOperaListener.onItemOpera("del", position, mData.get(position));
                        }
                        break;
                    case R.id.item_config:
                        if (listItemOperaListener != null) {
                            listItemOperaListener.onItemOpera("config", position, mData.get(position));
                        }
                        break;
                }
            }
        };
        viewHolder.del.setOnClickListener(onClickListener);
        viewHolder.config.setOnClickListener(onClickListener);
        return convertView;
    }


    class ViewHolder {

        @BindView(R.id.device_fishpond)
        TextView deviceFishpond;
        @BindView(R.id.device_id)
        TextView deviceId;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_state)
        TextView deviceState;
        @BindView(R.id.item_del)
        TextView del;
        @BindView(R.id.item_config)
        TextView config;
        @BindView(R.id.device_grid)
        UnScrollGridView deviceGrid;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
