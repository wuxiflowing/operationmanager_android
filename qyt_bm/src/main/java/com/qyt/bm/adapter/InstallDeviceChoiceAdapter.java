package com.qyt.bm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.bm.R;
import com.qyt.bm.model.DeviceChoiceModel;
import com.qyt.bm.response.PactDeviceInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class InstallDeviceChoiceAdapter extends BaseSimpleAdapter<PactDeviceInfo> {

    public void setOnItemCountChange(OnItemCountChange onItemCountChange) {
        this.onItemCountChange = onItemCountChange;
    }

    private OnItemCountChange onItemCountChange;

    public InstallDeviceChoiceAdapter(Context mContext, List<PactDeviceInfo> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device_choice, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PactDeviceInfo model = mData.get(position);
        if (model.count < 0) {
            model.count = model.contractDeviceNum;
        }
        viewHolder.deviceCheck.setText(model.contractDeviceType);
        viewHolder.deviceCount.setText(model.count + "");
        viewHolder.deviceCheck.setChecked(model.selected);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.device_plus:
                        if (model.count >= model.contractDeviceNum) {
                            Toast.makeText(mContext, "已是最大安装数量", Toast.LENGTH_SHORT).show();
                            viewHolder.devicePlus.setEnabled(false);
                            return;
                        }
                        model.count++;
                        viewHolder.deviceCount.setText(model.count + "");
                        if (onItemCountChange != null && model.selected) {
                            onItemCountChange.onCountChange(1);
                        }
                        break;
                    case R.id.device_reduce:
                        viewHolder.devicePlus.setEnabled(true);
                        if (model.count > 1) {
                            model.count--;
                            viewHolder.deviceCount.setText(model.count + "");
                            if (onItemCountChange != null && model.selected) {
                                onItemCountChange.onCountChange(-1);
                            }
                        }
                        break;
                }
            }
        };
        viewHolder.deviceReduce.setOnClickListener(onClickListener);
        viewHolder.devicePlus.setOnClickListener(onClickListener);
        viewHolder.deviceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    model.selected = isChecked;
                    if (onItemCountChange != null) {
                        if (isChecked)
                            onItemCountChange.onCountChange(model.count);
                        else
                            onItemCountChange.onCountChange(-model.count);
                    }
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.device_check)
        CheckBox deviceCheck;
        @BindView(R.id.device_reduce)
        TextView deviceReduce;
        @BindView(R.id.device_count)
        EditText deviceCount;
        @BindView(R.id.device_plus)
        TextView devicePlus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemCountChange {
        void onCountChange(int change);
    }
}
