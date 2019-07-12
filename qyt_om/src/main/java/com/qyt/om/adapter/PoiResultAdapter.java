package com.qyt.om.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.baidu.mapapi.search.core.PoiInfo;
import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.om.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class PoiResultAdapter extends BaseSimpleAdapter<PoiInfo> {

    private int checkItem = 0;

    public PoiInfo getCheckPoi() {
        return mData.get(checkItem);
    }

    public PoiResultAdapter(Context mContext, List<PoiInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_poiresult, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PoiInfo model = mData.get(position);
//        if (TextUtils.isEmpty(model.name)) {
//            viewHolder.poiName.setText(model.address);
//        } else {
//            viewHolder.poiName.setText(model.address + model.name);
//        }
        viewHolder.poiName.setText(model.address + model.name);
        if (checkItem == position) {
            viewHolder.poiName.setChecked(true);
        } else {
            viewHolder.poiName.setChecked(false);
        }
        viewHolder.poiName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed() && isChecked) {
                    checkItem = position;
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.poi_name)
        RadioButton poiName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
