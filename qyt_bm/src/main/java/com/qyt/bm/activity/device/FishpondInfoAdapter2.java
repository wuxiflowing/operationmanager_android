package com.qyt.bm.activity.device;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.response.ChildDeviceListBean;
import com.qyt.bm.response.DeviceControlInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: sunzhibin
 * @date: 2019-07-12 13:07
 * @description:
 * @e-mail:
 */
public class FishpondInfoAdapter2 extends BaseSimpleAdapter<ChildDeviceListBean> {


    public FishpondInfoAdapter2(Context mContext, List<ChildDeviceListBean> mData) {
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
        final ChildDeviceListBean bean = mData.get(position);
        viewHolder.tvFishpondName.setText(bean != null ? bean.pondName : "--");
        viewHolder.tvDeviceType.setText(bean != null ? bean.type : "--");

        if (bean != null) {
            switch (bean.workStatus) {
                case "-1": //数据解析异常(-1)
                    //fixme
                    //viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
                    break;
                case "0": //正常(0)
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_normal);
                    break;
                case "3": //不在线告警(3)
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
                    break;
                case "1": //告警限1(1)
                case "2": //告警限2(2)
                case "5": //设备告警(5)
                case "10": //断点告警(10)
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_warning);
                    break;
                default:
                    //fixme 多状态 "."分割
                    viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_ponds_offline);
                    break;
            }
        } else {
            viewHolder.tvDeviceStatus.setBackgroundResource(R.mipmap.icon_normal);
        }
        if (bean != null) {
            if (bean.deviceControlInfoBeanList != null) {
                for (DeviceControlInfoBean controlInfoBean : bean.deviceControlInfoBeanList) {

                    if (controlInfoBean.controlId == 0) {
                        if (!TextUtils.isEmpty(controlInfoBean.open)) {
                            viewHolder.tvControl1.setChecked("1".equals(controlInfoBean.open));
                            viewHolder.tvControl1.setVisibility(View.VISIBLE);
                            viewHolder.ivControl1.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvControl1.setVisibility(View.GONE);
                            viewHolder.ivControl1.setVisibility(View.GONE);
                        }
                    }
                    if (controlInfoBean.controlId == 1) {
                        if (!TextUtils.isEmpty(controlInfoBean.open)) {
                            viewHolder.tvControl2.setChecked("1".equals(controlInfoBean.open));
                            viewHolder.tvControl2.setVisibility(View.VISIBLE);
                            viewHolder.ivControl2.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvControl2.setVisibility(View.GONE);
                            viewHolder.ivControl2.setVisibility(View.GONE);
                        }

                    }

                    if (controlInfoBean.controlId == 2) {
                        if (!TextUtils.isEmpty(controlInfoBean.open)) {
                            viewHolder.tvControl3.setChecked("1".equals(controlInfoBean.open));
                            viewHolder.tvControl3.setVisibility(View.VISIBLE);
                            viewHolder.ivControl3.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvControl3.setVisibility(View.GONE);
                            viewHolder.ivControl3.setVisibility(View.GONE);
                        }
                    }

                    if (controlInfoBean.controlId == 3) {
                        if (!TextUtils.isEmpty(controlInfoBean.open)) {
                            viewHolder.tvControl4.setChecked("1".equals(controlInfoBean.open));
                            viewHolder.tvControl4.setVisibility(View.VISIBLE);
                            viewHolder.ivControl4.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvControl4.setVisibility(View.GONE);
                            viewHolder.ivControl4.setVisibility(View.GONE);
                        }
                    }
                }

            } else {
                viewHolder.tvControl1.setChecked(false);
                viewHolder.tvControl2.setChecked(false);
                viewHolder.tvControl3.setChecked(false);
                viewHolder.tvControl4.setChecked(false);

            }
            if (bean.workStatus.contains("3")) {
                viewHolder.tvItemValue.setText("--");
                viewHolder.tvTemperatureValue.setText("--");
                viewHolder.tvPhValue.setText("--");
            } else {
                viewHolder.tvItemValue.setText(!TextUtils.isEmpty(bean.oxy) ? bean.oxy : "--");
                viewHolder.tvTemperatureValue.setText(!TextUtils.isEmpty(bean.temp) ? bean.temp + "℃" : "--℃");
                viewHolder.tvPhValue.setText((TextUtils.isEmpty(bean.ph) || bean.ph.contains("-")) ? "--" : bean.ph);
            }
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
        return convertView;
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

