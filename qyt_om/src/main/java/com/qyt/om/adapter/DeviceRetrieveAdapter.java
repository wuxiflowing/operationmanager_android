package com.qyt.om.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.qyt.om.R;
import com.qyt.om.model.DeviceChoiceModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class DeviceRetrieveAdapter extends BaseSimpleAdapter<DeviceChoiceModel> {

    public void setListItemOperaListener(ListItemOperaListener listItemOperaListener) {
        this.listItemOperaListener = listItemOperaListener;
    }

    private ListItemOperaListener listItemOperaListener;

    public DeviceRetrieveAdapter(Context mContext, List<DeviceChoiceModel> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_deviceretrieve, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DeviceChoiceModel model = mData.get(position);
        viewHolder.deviceName.setText(model.device_model);
        viewHolder.deviceType.setText(model.device_id);
        if (model.selected) {
            viewHolder.deviceUnbind.setText("已解绑");
            viewHolder.deviceUnbind.setEnabled(false);
        } else {
            viewHolder.deviceUnbind.setText("解绑设备");
            viewHolder.deviceUnbind.setEnabled(true);
        }
        viewHolder.deviceUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItemOperaListener != null) {
                    listItemOperaListener.onItemOpera("unbind", position, model);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_unbind)
        Button deviceUnbind;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
