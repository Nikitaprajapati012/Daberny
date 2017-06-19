package com.example.raviarchi.daberny.Activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.raviarchi.daberny.Activity.Fragment.FriendsContact;
import com.example.raviarchi.daberny.Activity.Fragment.FriendsFacebook;
import com.example.raviarchi.daberny.Activity.Fragment.FriendsTwitter;


public class AddFriendsPager extends FragmentStatePagerAdapter {
    public Fragment fragment;
    int tabCount;

    public AddFriendsPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = null;

        switch (position) {
            case 0:
                fragment = new FriendsFacebook();
                break;
            case 1:
                fragment = new FriendsContact();
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