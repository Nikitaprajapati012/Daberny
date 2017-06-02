package com.example.raviarchi.daberny.Activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.raviarchi.daberny.Activity.Fragment.Notification;
import com.example.raviarchi.daberny.Activity.Fragment.SearchPeople;
import com.example.raviarchi.daberny.Activity.Fragment.SearchRecent;
import com.example.raviarchi.daberny.Activity.Fragment.SearchTag;


public class SearchPager extends FragmentStatePagerAdapter {
    public Fragment fragment;
    int tabCount;

    public SearchPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = null;

        switch (position) {
            case 0:
                fragment = new SearchRecent();
                break;
            case 1:
                fragment = new SearchPeople();
                break;
            case 2:
                fragment = new SearchTag();
                break;

            default:
                return null;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}