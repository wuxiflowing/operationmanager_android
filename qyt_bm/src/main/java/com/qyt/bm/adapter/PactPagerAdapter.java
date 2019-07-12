package com.qyt.bm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.bm.activity.fragment.CustomerPactFragment;
import com.qyt.bm.activity.fragment.InstallTaskFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class PactPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"服务合同", "押金合同", "预付款"};

    public PactPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return CustomerPactFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
