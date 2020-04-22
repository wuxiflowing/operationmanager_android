package com.qyt.bm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollGridView;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.DeviceConfigInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceCellAdapter extends BaseSimpleAdapter<DeviceConfigInfo> {

    private ListItemOperaListener listItemOperaListener;

    public void setListItemOperaListener(ListItemOperaListener listItemOperaListener) {
        this.listItemOperaListener = listItemOperaListener;
    }

    public DeviceCellAdapter(Context mContext, List<DeviceConfigInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_devicecell, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DeviceConfigInfo bean = mData.get(position);
        viewHolder.deviceId.setText("设备ID：" + (bean != null ? bean.identifier : "--"));
        viewHolder.deviceType.setText("型号：" + (bean != null ? bean.type : "--"));
        if (bean != null && "0".equals(bean.workStatus)) {
            viewHolder.deviceState.setTextColor(Color.GREEN);
        } else {
            viewHolder.deviceState.setTextColor(Color.RED);
        }
        if (bean == null) {
            viewHolder.deviceState.setText("--");
        } else {
            String status = Constants.showDeviceStatus(mContext, bean.workStatus);
            viewHolder.deviceState.setText(status);
        }
//        viewHolder.deviceState.setText("" + (bean != null ? Constants.DEVICE_STATE.get(bean.workStatus) : "--"));
        List<InfoMap> infoMaps = new ArrayList<>();
        infoMaps.add(new InfoMap("溶氧值", (bean != null ? bean.dissolvedOxygen : "--") + "mg/L"));
        infoMaps.add(new InfoMap("温度", (bean != null ? bean.temperature : "--") + "℃"));
        if (bean != null && !TextUtils.isEmpty(bean.ph)) {
            try {
                float ph = Float.parseFloat(bean.ph);
                if (ph < 0) {
                    infoMaps.add(new InfoMap("PH值", "--"));
                } else {
                    infoMaps.add(new InfoMap("PH值", bean.ph + ""));
                }
            } catch (NumberFormatException e) {
                infoMaps.add(new InfoMap("PH值", "--"));
            }
        } else {
            infoMaps.add(new InfoMap("PH值", "--"));
        }
        if (bean != null && bean.aeratorControlList != null && bean.aeratorControlList.size() >= 2) {
            infoMaps.add(new InfoMap("控制1状态", (bean != null && bean.aeratorControlList.get(0).open ? "开" : "关")));
            infoMaps.add(new InfoMap("控制2状态", (bean != null && bean.aeratorControlList.get(0).open ? "开" : "关")));
        } else {
            infoMaps.add(new InfoMap("控制1状态", "关"));
            infoMaps.add(new InfoMap("控制2状态", "关"));
        }
        infoMaps.add(new InfoMap("控制方式", (bean != null && bean.automatic ? "自动" : "手动")));
        viewHolder.deviceGrid.setAdapter(new DeviceStateAdapter(mContext, infoMaps));
        viewHolder.clickDeviceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItemOperaListener != null) {
                    listItemOperaListener.onItemOpera("detail", position, bean.identifier);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.device_id)
        TextView deviceId;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_state)
        TextView deviceState;
        @BindView(R.id.click_device_detail)
        TextViewPlus clickDeviceDetail;
        @BindView(R.id.device_grid)
        UnScrollGridView deviceGrid;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
