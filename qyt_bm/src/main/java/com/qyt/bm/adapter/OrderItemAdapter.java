package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.DeviceChoiceModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class OrderItemAdapter extends BaseSimpleAdapter<String> {

    public OrderItemAdapter(Context mContext, List<String> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_orderitem, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderItem.setText(mData.get(position));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.order_item)
        TextView orderItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
