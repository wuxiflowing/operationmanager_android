package com.qyt.bm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ChoiceCustomerAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.response.CustomerItem;
import com.qyt.bm.widget.LetterNavigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChoiceCustomerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView contactsList;
    @BindView(R.id.letter_navi)
    LetterNavigation letterNavi;
    @BindView(R.id.letter_notice)
    TextView letterNotice;
    @BindView(R.id.slip_loading_view)
    LoadingView loadingView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.phone_search)
    EditText switchcityInput;
    @BindView(R.id.phone_search_cancel)
    ImageView cancelSearch;

    private List<ContactItem> contactItemList = new ArrayList<>();
    private ChoiceCustomerAdapter contactsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HashMap<String, Integer> listIndex = new HashMap<>();
    private boolean move;
    private int mIndex;
    Handler handler = new Handler();
    private String fromFlag;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_choicecustomer);
        fromFlag = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        if (fromFlag != null && "sign".equals(fromFlag)) {
            BmApplication.getInstance().addToSteamActivity(this);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("选择养殖户");
        linearLayoutManager = new LinearLayoutManager(this);
        contactsAdapter = new ChoiceCustomerAdapter(this, contactItemList);
        contactsList.setLayoutManager(linearLayoutManager);
        contactsList.setAdapter(contactsAdapter);
    }

    @OnClick({R.id.phone_search_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phone_search_cancel:
                cancelSearch.setVisibility(View.GONE);
                switchcityInput.setText("");
                requestData();
                break;
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                requestData();
            }

            @Override
            public void onLoadingMore() {

            }
        });
        letterNavi.setOnLetterChangeListener(new LetterNavigation.OnLetterChangeListener() {
            @Override
            public void letterChanged(String letter) {
                showLetterNotice(letter);
                if (listIndex.keySet().contains(letter)) {
                    moveToPosition(listIndex.get(letter));
                } else {
                    moveToPosition(contactItemList.size() - 1);
                }
            }
        });
        contactsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fir = linearLayoutManager.findFirstVisibleItemPosition();
                letterNavi.setLetterFocused(String.valueOf(contactItemList.get(fir).index));
                showLetterNotice(String.valueOf(contactItemList.get(fir).index));
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < contactsList.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = contactsList.getChildAt(n).getTop();
                        //最后的移动
                        contactsList.scrollBy(0, top);
                    }
                }
            }
        });
        contactsAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<ContactItem>() {
            @Override
            public void onItemClick(int position, ContactItem value) {
                if (fromFlag != null) {
                    switch (fromFlag) {
                        case "sign":
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constants.INTENT_OBJECT, value);
                            goToActivity(CustomerSignCreateActivity.class, bundle);
                            break;
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_OBJECT, value);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onItemOpera(String tag, int position, ContactItem value) {

            }
        });
        switchcityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideSoftInput(mContext, switchcityInput);
                    requestData();
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
    }

    @Override
    protected void requestData() {
        super.requestData();
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        String key = "";
        if (!TextUtils.isEmpty(switchcityInput.getText())) {
            key = switchcityInput.getText().toString().trim();
        }
        getData(HttpConfig.CUSTOMER_LIST.replace("{loginid}", loginId) + "/contract?name=" + key, new ResponseCallBack<ArrayList<CustomerItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<CustomerItem> d, String msg) {
                slipLoadLayout.onLoadingComplete(true);
                contactItemList.clear();
                if (d != null && d.size() > 0) {
                    for (CustomerItem item : d) {
                        ContactItem contactItem = new ContactItem();
                        contactItem.farmerPic = item.farmerPic;
                        contactItem.name = item.farmerName;
                        contactItem.mobile = item.farmerTel;
                        contactItem.setPinyin(item.farmerName.trim());
                        contactItem.farmerId = item.farmerId;
                        contactItem.farmerAdd = item.farmerRegion + item.farmerAdd;
                        contactItemList.add(contactItem);
                    }
                    Collections.sort(contactItemList);
                    dealListIndex();
                    contactsAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    showToast(msg);
                    contactsAdapter.notifyDataSetChanged();
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

    private void moveToPosition(int index) {
        mIndex = index;
        //获取当前recycleView屏幕可见的第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (index <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            contactsList.scrollToPosition(index);
        } else if (index <= lastItem) {
            //当要置顶的项已经在屏幕上显示时，计算它离屏幕原点的距离
            int top = contactsList.getChildAt(index - firstItem).getTop();
            contactsList.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            contactsList.scrollToPosition(index);
            //记录当前需要在RecyclerView滚动监听里面继续第二次滚动
            move = true;
        }
    }

    private void showLetterNotice(String letter) {
        letterNotice.setText(letter);
        letterNotice.setVisibility(View.VISIBLE);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                letterNotice.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void dealListIndex() {
        String words[] = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int i = 0;
        String iw = words[0];
        for (int j = 0; j < contactItemList.size(); j++) {
            String id = String.valueOf(contactItemList.get(j).index).toUpperCase();
            if (iw.equals(id)) {
                if (!listIndex.keySet().contains(words[i])) {
                    listIndex.put(words[i], j);
                }
            } else {
                i++;
                for (; i < words.length; i++) {
                    iw = words[i];
                    if (iw.equals(id)) {
                        if (!listIndex.keySet().contains(words[i])) {
                            listIndex.put(words[i], j);
                        }
                        break;
                    } else {
                        listIndex.put(words[i], j);
                    }
                }
            }
        }
    }

}
