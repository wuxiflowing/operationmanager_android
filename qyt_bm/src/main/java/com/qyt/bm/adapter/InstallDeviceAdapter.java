package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.qyt.bm.R;
import com.qyt.bm.model.DeviceChoiceModel;
import com.qyt.bm.response.EquipmentItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class InstallDeviceAdapter extends BaseSimpleAdapter<EquipmentItem> {

    public void setListItemOperaListener(ListItemOperaListener listItemOperaListener) {
        this.listItemOperaListener = listItemOperaListener;
    }

    private ListItemOperaListener listItemOperaListener;

    public InstallDeviceAdapter(Context mContext, List<EquipmentItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_installdevice2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EquipmentItem model = mData.get(position);
        viewHolder.pondName.setText(model.ITEM2);
        viewHolder.deviceIdentifier.setText(model.ITEM1);

        viewHolder.deviceType.setText(model.ITEM4);
        viewHolder.deviceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItemOperaListener != null) {
                    //返回设备类型、设备编号
                    listItemOperaListener.onItemOpera(model.ITEM4, position, model.ITEM1);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.pond_name)
        TextView pondName;
        @BindView(R.id.device_identifier)
        TextView deviceIdentifier;
        @BindView(R.id.device_type)
        TextView deviceType;
        @BindView(R.id.device_detail)
        Button deviceDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
