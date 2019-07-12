package com.qyt.om.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.qyt.om.R;
import com.qyt.om.adapter.PondListAdapter;
import com.qyt.om.model.FishPondInfo;
import com.qyt.om.utils.SharedPref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 * 鱼塘设备绑定
 */
public class FishPondDialog extends Dialog {

    private ListView sentryList;
    private DialogConfirmListener onConfrimClickedListener;
    private PondListAdapter sentryListAdapter;
    private EditText etFishpond;
    private Context mContext;
    private List<FishPondInfo> mDatas = new ArrayList<>();
    private HashSet<String> pondNames;
    private SharedPref sharedPref;

    public FishPondDialog(Context context, final String farmerName, List<FishPondInfo> mData, DialogConfirmListener listener) {
        super(context, R.style.menu_dialog_style);
        this.mContext = context;
        onConfrimClickedListener = listener;
        setContentView(R.layout.dialog_fishpondlist);
        Window window = getWindow();
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (AppUtils.getDisplayMetrics(context).widthPixels * 0.8); //
        window.setAttributes(p);
        window.setGravity(Gravity.CENTER);
        etFishpond = findViewById(R.id.fishpond_name);
        boolean has = false;
        for (FishPondInfo pond : mData) {
            if (pond.name.equals(farmerName)) {
                has = true;
                break;
            }
        }
        if (!TextUtils.isEmpty(farmerName) && !has)
            etFishpond.setText(farmerName);
        sharedPref = new SharedPref(mContext);
//        pondNames = sharedPref.getJsonInfo(farmerName, new TypeToken<HashSet<String>>() {
//        }.getType());
//        if (pondNames != null && pondNames.size() > 0) {
//            for (String pondname : pondNames) {
//                FishPondInfo pond = new FishPondInfo();
//                pond.name = pondname;
//                mDatas.add(pond);
//            }
//        }
        this.mDatas.addAll(mData);
        sentryList = findViewById(R.id.sentry_list);
        sentryListAdapter = new PondListAdapter(mContext, mDatas);
        sentryList.setAdapter(sentryListAdapter);
        sentryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onConfrimClickedListener != null) {
                    onConfrimClickedListener.onDialogConfirm(true, mDatas.get(position));
                }
                dismiss();
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == com.bangqu.lib.R.id.edittext_ok) {
                    if (onConfrimClickedListener != null) {
                        if (TextUtils.isEmpty(etFishpond.getText().toString())) {
                            Toast.makeText(mContext, "请填写鱼塘名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        String pn = etFishpond.getText().toString();
//                        if (pondNames == null) {
//                            pondNames = new HashSet<>();
//                        }
//                        pondNames.add(pn);
//                        sharedPref.setJsonInfo(farmerName, pondNames);
                        onConfrimClickedListener.onDialogConfirm(false, etFishpond.getText().toString());
                    }
                    AppUtils.hideSoftInput(mContext, etFishpond);
                    dismiss();
                } else if (i == com.bangqu.lib.R.id.edittext_cancel) {
                    AppUtils.hideSoftInput(mContext, etFishpond);
                    dismiss();
                }
            }
        };
        findViewById(com.bangqu.lib.R.id.edittext_ok).setOnClickListener(onClickListener);
        findViewById(com.bangqu.lib.R.id.edittext_cancel).setOnClickListener(onClickListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        setHeight();
    }

    private void setHeight() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.6)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.6);
        }
        window.setAttributes(attributes);
    }

}
