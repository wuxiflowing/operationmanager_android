package com.qyt.bm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollGridView;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.DeviceConfigInfo;
import com.qyt.bm.response.FishPondInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class FishpondInfoAdapter extends BaseSimpleAdapter<FishPondInfo> {

    public void setOperaListener(ListItemOperaListener operaListener) {
        this.operaListener = operaListener;
    }

    private ListItemOperaListener operaListener;

    public FishpondInfoAdapter(Context mContext, List<FishPondInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fishpondinfo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FishPondInfo model = mData.get(position);
        viewHolder.fishpondName.setText(model.name);
        if (model.childDeviceList != null && model.childDeviceList.size() > 0) {
            DeviceCellAdapter deviceCellAdapter = new DeviceCellAdapter(mContext, model.childDeviceList);
            viewHolder.deviceList.setAdapter(deviceCellAdapter);
            deviceCellAdapter.setListItemOperaListener(new ListItemOperaListener() {
                @Override
                public void onItemOpera(String tag, int position, Object value) {
                    if (operaListener != null) {
                        operaListener.onItemOpera("device", position, value);
                    }
                }
            });
        } else {
            viewHolder.deviceList.setAdapter(null);
        }
        viewHolder.fishpondExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.fishpondArrow.isChecked()) {
                    viewHolder.fishpondArrow.setChecked(false);
                    viewHolder.fishpondInfo.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.fishpondArrow.setChecked(true);
                    viewHolder.fishpondInfo.setVisibility(View.GONE);
                }
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operaListener != null) {
                    switch (v.getId()) {
                        case R.id.click_fishpond_detail:
                            operaListener.onItemOpera("fishpond", position, mData.get(position));
                            break;
                    }
                }
            }
        };
        viewHolder.fishpondDetail.setOnClickListener(onClickListener);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.fishpond_arrow)
        CheckBox fishpondArrow;
        @BindView(R.id.fishpond_expand)
        RelativeLayout fishpondExpand;
        @BindView(R.id.fishpond_info)
        LinearLayout fishpondInfo;
        @BindView(R.id.click_fishpond_detail)
        TextViewPlus fishpondDetail;
        @BindView(R.id.fishpond_name)
        TextView fishpondName;
        @BindView(R.id.device_list)
        UnScrollListView deviceList;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
