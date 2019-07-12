package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.AreaItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class ChoiceAreaAdapter extends BaseSimpleAdapter<AreaItem> {

    public ChoiceAreaAdapter(Context mContext, List<AreaItem> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_choicearea, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AreaItem model = mData.get(position);
        viewHolder.areaName.setText(model.name);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.area_name)
        TextView areaName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
