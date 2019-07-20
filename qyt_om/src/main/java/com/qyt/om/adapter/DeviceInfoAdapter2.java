package com.qyt.om.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.qyt.om.R;
import com.qyt.om.activity.device.FishpondInfoAdapter2;
import com.qyt.om.comm.Constants;
import com.qyt.om.response.ChildDeviceListBean;
import com.qyt.om.response.DeviceControlInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceInfoAdapter2 extends BaseRecyclerAdapter<ChildDeviceListBean> {

    public DeviceInfoAdapter2(Context mContext, List<ChildDeviceListBean> mData) {
        super(mContext, mData);
    }

    public void setOperaListener(ListItemOperaListener operaListener) {
        this.operaListener = operaListener;
    }

    private ListItemOperaListener operaListener;

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_fishpond_info_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final ChildDeviceListBean bean = mData.get(position);
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
        if (bean != null) {
            if (Constants.DEVICE_TYPE_KD326.equals(bean.type)) {
                viewHolder.tvControl3.setVisibility(View.GONE);
                viewHolder.tvControl4.setVisibility(View.GONE);

                viewHolder.ivControl3.setVisibility(View.GONE);
                viewHolder.ivControl4.setVisibility(View.GONE);

            } else if (Constants.DEVICE_TYPE_QY601.equals(bean.type)) {
                viewHolder.tvControl3.setVisibility(View.VISIBLE);
                viewHolder.tvControl4.setVisibility(View.VISIBLE);

                viewHolder.ivControl3.setVisibility(View.VISIBLE);
                viewHolder.ivControl4.setVisibility(View.VISIBLE);
                if (bean.deviceControlInfoBeanList != null) {
                    viewHolder.tvControl3.setChecked(bean.deviceControlInfoBeanList.size() > 2 && bean.deviceControlInfoBeanList.get(2).open == 1);
                    viewHolder.tvControl4.setChecked(bean.deviceControlInfoBeanList.size() > 3 && bean.deviceControlInfoBeanList.get(3).open == 1);
                } else {
                    viewHolder.tvControl3.setChecked(false);
                    viewHolder.tvControl4.setChecked(false);
                }
            }
            if (bean.deviceControlInfoBeanList != null) {

                viewHolder.tvControl1.setChecked(bean.deviceControlInfoBeanList.size() > 0 && bean.deviceControlInfoBeanList.get(1).open == 1);
                viewHolder.tvControl2.setChecked(bean.deviceControlInfoBeanList.size() > 1 && bean.deviceControlInfoBeanList.get(1).open == 1);
            } else {
                viewHolder.tvControl1.setChecked(false);
                viewHolder.tvControl2.setChecked(false);

            }
            viewHolder.tvItemValue.setText(!TextUtils.isEmpty(bean.oxy) ? bean.oxy : "--");
            viewHolder.tvTemperatureValue.setText(!TextUtils.isEmpty(bean.temp) ? bean.temp + "℃" : "--℃");
            viewHolder.tvPhValue.setText((TextUtils.isEmpty(bean.ph) || bean.ph.contains("-")) ? "--" : bean.ph);

        } else {
            viewHolder.tvControl3.setVisibility(View.VISIBLE);
            viewHolder.tvControl4.setVisibility(View.VISIBLE);

            viewHolder.ivControl3.setVisibility(View.VISIBLE);
            viewHolder.ivControl4.setVisibility(View.VISIBLE);

            viewHolder.tvControl1.setChecked(false);
            viewHolder.tvControl2.setChecked(false);
            viewHolder.tvControl3.setChecked(false);
            viewHolder.tvControl4.setChecked(false);

            viewHolder.tvItemValue.setText("--");
            viewHolder.tvTemperatureValue.setText("--℃");
            viewHolder.tvPhValue.setText("--");

        }
        settingControlMode(bean, viewHolder);

        viewHolder.tvDeviceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operaListener != null) {
                    operaListener.onItemOpera("device", position, bean != null ? bean.type + "-" + bean.identifier : "");
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

    }


    private void settingControlMode(ChildDeviceListBean bean, ViewHolder viewHolder) {
        if (bean == null || bean.deviceControlInfoBeanList == null) {
            //设备自动手动/模式
            viewHolder.ivControl1.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl2.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl3.setBackgroundResource(R.mipmap.icon_auto);
            viewHolder.ivControl4.setBackgroundResource(R.mipmap.icon_auto);
            return;
        }
        for (DeviceControlInfoBean deviceControlInfoBean : bean.deviceControlInfoBeanList) {
            if (deviceControlInfoBean.controlId == 0) {
                viewHolder.ivControl1.setBackgroundResource(deviceControlInfoBean.auto == 1 ? R.mipmap.icon_auto : R.mipmap.icon_manual);
            }
            if (deviceControlInfoBean.controlId == 1) {
                viewHolder.ivControl2.setBackgroundResource(deviceControlInfoBean.auto == 1 ? R.mipmap.icon_auto : R.mipmap.icon_manual);
            }
            if (deviceControlInfoBean.controlId == 2) {
                viewHolder.ivControl3.setBackgroundResource(deviceControlInfoBean.auto == 1 ? R.mipmap.icon_auto : R.mipmap.icon_manual);
            }
            if (deviceControlInfoBean.controlId == 3) {
                viewHolder.ivControl4.setBackgroundResource(deviceControlInfoBean.auto == 1 ? R.mipmap.icon_auto : R.mipmap.icon_manual);

            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
