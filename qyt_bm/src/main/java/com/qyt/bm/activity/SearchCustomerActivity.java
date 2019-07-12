package com.qyt.bm.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.DividerItemDecoration;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ContactsAdapter;
import com.qyt.bm.adapter.ContactsResAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.response.CustomerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchCustomerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView contactsList;
    @BindView(R.id.slip_loading_view)
    LoadingView loadingView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.switchcity_input)
    EditText switchcityInput;
    @BindView(R.id.cancel_search)
    ImageView cancelSearch;

    private List<ContactItem> contactItemList = new ArrayList<>();
    private ContactsResAdapter contactsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String httpUrl;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_searchcustomer);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("养殖户");
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        boolean isContract = getIntent().getBooleanExtra(Constants.INTENT_OBJECT, false);
        if (isContract)
            httpUrl = HttpConfig.CUSTOMER_LIST.replace("{loginid}", loginId) + "/contract";
        else
            httpUrl = HttpConfig.CUSTOMER_LIST.replace("{loginid}", loginId) + "/general";
        linearLayoutManager = new LinearLayoutManager(this);
        contactsAdapter = new ContactsResAdapter(this, contactItemList);
        contactsList.setLayoutManager(linearLayoutManager);
        contactsList.setAdapter(contactsAdapter);
        contactsList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, 0.5f, Color.parseColor("#DDDDDD")));
        loadingView.setLoadingState(LoadingView.NO_DATA);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        switchcityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideSoftInput(mContext, getCurrentFocus());
                    searchData(v.getText().toString());
                }
                return false;
            }
        });
        switchcityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cancelSearch.setVisibility(View.VISIBLE);
                } else {
                    cancelSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                requestData();
            }

            @Override
            public void onLoadingMore() {

            }
        });
        contactsAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<ContactItem>() {
            @Override
            public void onItemClick(int position, ContactItem value) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, value);
                goToActivity(CustomerHomeActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, ContactItem value) {

            }
        });
    }

    protected void searchData(String name) {
        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名关键字");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(httpUrl + "?" + getParamsToUrl(params), new ResponseCallBack<ArrayList<CustomerItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<CustomerItem> d, String msg) {
                slipLoadLayout.onLoadingComplete(true);
                contactItemList.clear();
                if (d != null && d.size() > 0) {
                    for (CustomerItem item : d) {
                        ContactItem contactItem = new ContactItem();
                        contactItem.name = item.farmerName;
                        contactItem.mobile = item.farmerTel;
                        contactItem.setPinyin(item.farmerName.trim());
                        contactItem.farmerId = item.farmerId;
                        contactItem.farmerAdd = item.farmerRegion + item.farmerAdd;
                        contactItem.farmerPic = item.farmerPic;
                        contactItemList.add(contactItem);
                    }
                    contactsAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    showToast(msg);
                    loadingView.setLoadingState(LoadingView.NO_DATA);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                contactItemList.clear();
                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                contactItemList.clear();
                contactsAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.cancel_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_search:
                cancelSearch.setVisibility(View.GONE);
                switchcityInput.setText("");
                loadingView.setLoadingState(LoadingView.NO_DATA);
                contactItemList.clear();
                contactsAdapter.notifyDataSetChanged();
                break;
        }
    }

}
