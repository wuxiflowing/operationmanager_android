package com.qyt.om.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseRecyclerAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.om.R;
import com.qyt.om.comm.Constants;
import com.qyt.om.model.InfoMap;
import com.qyt.om.response.PondDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceInfoAdapter extends BaseRecyclerAdapter<PondDeviceInfo> {

    public DeviceInfoAdapter(Context mContext, List<PondDeviceInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_deviceinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final PondDeviceInfo model = mData.get(position);
        viewHolder.deviceFishpond.setText(model.name);
        if (model.childDeviceList != null) {
            DeviceCellAdapter deviceCellAdapter = new DeviceCellAdapter(mContext, model.childDeviceList);
            viewHolder.deviceList.setAdapter(deviceCellAdapter);
            deviceCellAdapter.setListItemOperaListener(new ListItemOperaListener() {
                @Override
                public void onItemOpera(String tag, int position, Object value) {
                    if (recyclerViewItemClickListener != null) {
                        recyclerViewItemClickListener.onItemOpera((String) value, position, model);
                    }
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_fishpond)
        TextView deviceFishpond;
        @BindView(R.id.device_list)
        UnScrollListView deviceList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
