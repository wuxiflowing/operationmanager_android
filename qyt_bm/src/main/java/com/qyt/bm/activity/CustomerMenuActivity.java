package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bangqu.lib.widget.UnScrollGridView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.WorkMenuAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.WorkMenuModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class CustomerMenuActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    //待办事项菜单
    @BindView(R.id.customer_operas)
    UnScrollGridView customerOperas;
    private List<WorkMenuModel> operaModels = new ArrayList<>();
    @BindArray(R.array.customer_opera_array)
    String[] operasName;
    int[] operasRes = new int[]{R.mipmap.device_install, R.mipmap.device_repair, R.mipmap.device_retrieve, R.mipmap.customer_agreement
            , R.mipmap.customer_feed, R.mipmap.customer_tackpay, R.mipmap.customer_bill, R.mipmap.employee_routing};
    //传感器业务菜单
    @BindView(R.id.customer_records)
    UnScrollGridView customerRecords;
    private List<WorkMenuModel> recordModels = new ArrayList<>();
    @BindArray(R.array.customer_record_array)
    String[] recordsName;
    int[] recordsRes = new int[]{R.mipmap.device_install, R.mipmap.customer_tackpay, R.mipmap.customer_bill, R.mipmap.record_settlement
            , R.mipmap.device_repair, R.mipmap.device_retrieve, R.mipmap.device_stick, R.mipmap.employee_routing
            , R.mipmap.record_order, R.mipmap.record_pact};

    private ContactItem farmer;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customermenu);
        farmer = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("操作记录");
        for (int i = 0; i < operasName.length && i < operasRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = operasName[i];
            model.resource = operasRes[i];
            if (i < 4)
                model.isDone = true;
            operaModels.add(model);
        }
        customerOperas.setAdapter(new WorkMenuAdapter(this, operaModels));
        for (int i = 0; i < recordsName.length && i < recordsRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = recordsName[i];
            model.resource = recordsRes[i];
            recordModels.add(model);
        }
        customerRecords.setAdapter(new WorkMenuAdapter(this, recordModels));
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkMenuAdapter adapter = (WorkMenuAdapter) parent.getAdapter();
                String name = adapter.getItem(position).name;
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(Constants.INTENT_OBJECT, farmer);
                switch (name) {
                    case "设备安装":
                        goToActivity(InstallTaskCreateActivity.class, bundle1);
                        break;
                    case "故障报修":
                        goToActivity(RepairTaskCreateActivity.class, bundle1);
                        break;
                    case "签/续约":
                        goToActivity(CustomerSignCreateActivity.class, bundle1);
                        break;
                    case "合同管理":
//                        goToActivity(CustomerPactActivity.class);
                        break;
                    case "我的订单":
//                        goToActivity(CustomerOrderActivity.class);
                        break;
                }
            }
        };
        customerOperas.setOnItemClickListener(onItemClickListener);
        customerRecords.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void requestData() {
        super.requestData();

    }

}
