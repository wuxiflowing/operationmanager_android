package com.qyt.bm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.widget.TextViewPlus;
import com.qyt.bm.R;
import com.qyt.bm.model.SignBillItem;
import com.qyt.bm.request.PactSubmit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class SignBillAdapter extends BaseSimpleAdapter<PactSubmit> {

    private signBillOperaListener signBillOperaListener;

    public void setSignBillOperaListener(SignBillAdapter.signBillOperaListener signBillOperaListener) {
        this.signBillOperaListener = signBillOperaListener;
    }

    public SignBillAdapter(Context mContext, List<PactSubmit> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_signbill, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PactSubmit model = mData.get(position);
        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getId()) {
                    case R.id.signbill_ispay:
                        switch (checkedId) {
                            case R.id.ispay_false:
                                viewHolder.payInfo.setVisibility(View.GONE);
                                viewHolder.payFile.setVisibility(View.GONE);
                                break;
                            case R.id.ispay_true:
                                viewHolder.payInfo.setVisibility(View.VISIBLE);
                                viewHolder.payFile.setVisibility(View.VISIBLE);
                                break;
                        }
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signbill_del:
                        if (signBillOperaListener != null)
                            signBillOperaListener.onBillDelete(position);
                        break;
                    case R.id.signbill_date:
                        if (signBillOperaListener != null)
                            signBillOperaListener.onBillPickDate(position);
                        break;
                }
            }
        };
        viewHolder.signbillDel.setOnClickListener(onClickListener);
        viewHolder.signbillDate.setOnClickListener(onClickListener);
        viewHolder.signbillIspay.setOnCheckedChangeListener(onCheckedChangeListener);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.signbill_del)
        TextView signbillDel;
        @BindView(R.id.signbill_type)
        RadioGroup signbillType;
        @BindView(R.id.signbill_date)
        TextViewPlus signbillDate;
        @BindView(R.id.signbill_name)
        TextView signbillName;
        @BindView(R.id.signbill_amount)
        EditText signbillAmount;
        @BindView(R.id.signbill_ispay)
        RadioGroup signbillIspay;
        @BindView(R.id.signbill_paytype)
        RadioGroup signbillPaytype;
        @BindView(R.id.signbill_paysum)
        EditText signbillPaysum;
        @BindView(R.id.pay_info)
        LinearLayout payInfo;
        @BindView(R.id.pact_pictures)
        RecyclerView pactPictures;
        @BindView(R.id.pay_pictures)
        RecyclerView payPictures;
        @BindView(R.id.pay_file)
        LinearLayout payFile;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface signBillOperaListener {
        void onBillDelete(int position);

        void onBillPickDate(int position);
    }
}
