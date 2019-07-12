package com.qyt.bm.activity.device;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.qyt.bm.R;
import com.qyt.bm.response.DeviceConfigInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: sunzhibin
 * @date: 2019-07-12 13:07
 * @description:
 * @e-mail:
 */
public class FishpondInfoAdapter2 extends BaseSimpleAdapter<DeviceConfigInfo> {


    public FishpondInfoAdapter2(Context mContext, List<DeviceConfigInfo> mData) {
        super(mContext, mData);
    }

    public void setOperaListener(ListItemOperaListener operaListener) {
        this.operaListener = operaListener;
    }

    private ListItemOperaListener operaListener;

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fishpond_info_new, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DeviceConfigInfo bean = mData.get(position);
        viewHolder.tvFishpondName.setText(bean != null ? bean.pondName : "--");
        viewHolder.tvDeviceType.setText(bean != null ? bean.type : "--");

        if (bean != null) {
            switch (bean.workStatus) {
                case -1:
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
                    break;
                case 0:
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_normal);
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_warning);
                    break;
                default:
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
                    break;
            }
        } else {
            viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
        }
        viewHolder.tvItemValue.setText(bean != null ? bean.dissolvedOxygen : "--");
        viewHolder.tvTemperatureValue.setText(bean != null ? bean.temperature : "--℃");
        viewHolder.tvPhValue.setText(bean != null ? bean.ph : "--");

        if (bean != null && bean.aeratorControlList != null && bean.aeratorControlList.size() > 0) {
            viewHolder.tvControl1.setVisibility(View.VISIBLE);
            viewHolder.tvControl2.setVisibility(bean.aeratorControlList.size() > 1 ? View.VISIBLE : View.GONE);

            viewHolder.tvControl3.setVisibility(bean.aeratorControlList.size() > 2 ? View.VISIBLE : View.GONE);
            viewHolder.tvControl4.setVisibility(bean.aeratorControlList.size() > 3 ? View.VISIBLE : View.GONE);


            viewHolder.ivControl1.setVisibility(View.VISIBLE);
            viewHolder.ivControl2.setVisibility(viewHolder.tvControl2.getVisibility());

            viewHolder.ivControl3.setVisibility(viewHolder.tvControl3.getVisibility());
            viewHolder.ivControl4.setVisibility(viewHolder.tvControl4.getVisibility());


            viewHolder.tvControl1.setChecked(bean.aeratorControlList.get(0).open);
            viewHolder.tvControl2.setChecked(bean.aeratorControlList.size() > 1 && bean.aeratorControlList.get(1).open);

            viewHolder.tvControl3.setChecked(bean.aeratorControlList.size() > 2 && bean.aeratorControlList.get(2).open);
            viewHolder.tvControl4.setChecked(bean.aeratorControlList.size() > 3 && bean.aeratorControlList.get(3).open);


            viewHolder.ivControl1.setBackgroundResource(bean.automatic ? R.mipmap.icon_auto : R.mipmap.icon_manual);
            viewHolder.ivControl2.setBackgroundResource(bean.automatic ? R.mipmap.icon_auto : R.mipmap.icon_manual);

            viewHolder.ivControl3.setBackgroundResource(bean.automatic ? R.mipmap.icon_auto : R.mipmap.icon_manual);
            viewHolder.ivControl4.setBackgroundResource(bean.automatic ? R.mipmap.icon_auto : R.mipmap.icon_manual);

        } else {
            //设备开关状态
            viewHolder.tvControl1.setVisibility(View.VISIBLE);
            viewHolder.tvControl2.setVisibility(View.VISIBLE);
            //fixme
            viewHolder.tvControl3.setVisibility(View.VISIBLE);
            viewHolder.tvControl4.setVisibility(View.VISIBLE);

            viewHolder.ivControl1.setVisibility(View.VISIBLE);
            viewHolder.ivControl2.setVisibility(View.VISIBLE);
            viewHolder.ivControl3.setVisibility(View.VISIBLE);
            viewHolder.ivControl4.setVisibility(View.VISIBLE);

            viewHolder.tvControl1.setChecked(false);
            viewHolder.tvControl2.setChecked(false);
            viewHolder.tvControl3.setChecked(false);
            viewHolder.tvControl4.setChecked(false);

            //设备自动手动/模式
            viewHolder.ivControl1.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl2.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl3.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl4.setBackgroundResource(R.mipmap.icon_auto);
        }

        viewHolder.tvDeviceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operaListener != null) {
                    operaListener.onItemOpera("device", position, bean.identifier);
                }
            }
        });
        viewHolder.tvFishpondDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operaListener != null) {
                    operaListener.onItemOpera("fishpond", position, bean.pondId);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_value)
        TextView tvItemValue;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_fishpond_name)
        TextView tvFishpondName;
        @BindView(R.id.tv_device_type)
        TextView tvDeviceType;
        @BindView(R.id.tv_device_status)
        TextView tvDeviceStatus;
        @BindView(R.id.tv_control_1)
        CheckedTextView tvControl1;
        @BindView(R.id.tv_control_2)
        CheckedTextView tvControl2;
        @BindView(R.id.tv_control_3)
        CheckedTextView tvControl3;
        @BindView(R.id.tv_control_4)
        CheckedTextView tvControl4;
        @BindView(R.id.iv_control_1)
        ImageView ivControl1;
        @BindView(R.id.iv_control_2)
        ImageView ivControl2;
        @BindView(R.id.iv_control_3)
        ImageView ivControl3;
        @BindView(R.id.iv_control_4)
        ImageView ivControl4;
        @BindView(R.id.tv_temperature_value)
        TextView tvTemperatureValue;
        @BindView(R.id.tv_ph_value)
        TextView tvPhValue;
        @BindView(R.id.tv_device_detail)
        TextView tvDeviceDetail;
        @BindView(R.id.tv_fishpond_detail)
        TextView tvFishpondDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
