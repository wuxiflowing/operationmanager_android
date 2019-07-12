package com.qyt.om.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.om.activity.fragment.RepairTaskFragment;
import com.qyt.om.activity.fragment.RetrieveTaskFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class RetrievePagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"待接单", "进行中", "已完成"};

    public RetrievePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return RetrieveTaskFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
