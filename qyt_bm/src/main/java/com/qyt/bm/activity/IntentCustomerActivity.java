package com.qyt.bm.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ContactsAdapter;
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

public class IntentCustomerActivity extends BaseActivity {

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

    private List<ContactItem> contactItemList = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HashMap<String, Integer> listIndex = new HashMap<>();
    private boolean move;
    private int mIndex;
    private Handler handler = new Handler();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_intentcustomer);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("养殖户");
        linearLayoutManager = new LinearLayoutManager(this);
        contactsAdapter = new ContactsAdapter(this, contactItemList);
        contactsList.setLayoutManager(linearLayoutManager);
        contactsList.setAdapter(contactsAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                getCustomerData();
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
            public void onItemOpera(String tag, int position, final ContactItem value) {
                new ConfirmDialog(mContext, "拨打电话", value.mobile, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, value.mobile);
                        }
                    }
                }).show();
            }
        });
        letterNavi.setOnLetterChangeListener(new LetterNavigation.OnLetterChangeListener() {
            @Override
            public void letterChanged(String letter) {
                showLetterNotice(letter);
                if (listIndex.keySet().contains(letter)) {
                    if (listIndex.get(letter) > 0) {
                        moveToPosition(listIndex.get(letter));
                    }
                } else {
                    if (contactItemList.size() > 0) {
                        moveToPosition(contactItemList.size() - 1);
                    }
                }
            }
        });
        contactsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fir = linearLayoutManager.findFirstVisibleItemPosition();
                if (fir < 0) return;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomerData();
    }

    protected void getCustomerData() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.CUSTOMER_LIST.replace("{loginid}", loginId) + "/general?name=", new ResponseCallBack<ArrayList<CustomerItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<CustomerItem> d, String msg) {
                slipLoadLayout.onLoadingComplete(true);
                contactItemList.clear();
                if (d != null && d.size() > 0) {
                    for (CustomerItem item : d) {
                        ContactItem contactItem = new ContactItem();
                        contactItem.name = item.farmerName;
                        contactItem.mobile = item.farmerTel;
                        contactItem.setPinyin(item.farmerName);
                        contactItem.farmerId = item.farmerId;
                        contactItem.farmerAdd = item.farmerRegion + item.farmerAdd;
                        contactItem.farmerPic = item.farmerPic;
                        contactItemList.add(contactItem);
                    }
                    Collections.sort(contactItemList);
                    dealListIndex();
                    contactsAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    contactsAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.NO_DATA);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                contactItemList.clear();
                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                contactItemList.clear();
                contactsAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.customer_create, R.id.customer_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.customer_create:
                goToActivity(CustomerCreateActivity.class);
                break;
            case R.id.customer_search:
                goToActivity(SearchCustomerActivity.class);
                break;
        }
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
