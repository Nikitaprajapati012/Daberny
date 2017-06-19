package com.example.raviarchi.daberny.Activity.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Adapter.AddFriendsPager;
import com.example.raviarchi.daberny.R;


/*** Created by Ravi archi on 1/10/2017.
 */

public class AddFriends extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public TextView txtTitle;
    public ImageView imgBack;
    public RelativeLayout layoutHeader;
    public RelativeLayout headerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_addfriends, container, false);
        findById(view);
        layoutHeader = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        layoutHeader.setVisibility(View.GONE);
        tabLayout.addTab(tabLayout.newTab().setText("Facebook"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // TODO: 3/22/2017 set view pager
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        AddFriendsPager adapter = new AddFriendsPager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        // TODO: 3/22/2017  Adding adapter to pager
        viewPager.setAdapter(adapter);
        // TODO: 3/22/2017  Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        return view;
    }

    private void findById(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        headerView = (RelativeLayout) view.findViewById(R.id.headerview);
        txtTitle = (TextView)view.findViewById(R.id.header_title);
        imgBack = (ImageView)view.findViewById(R.id.header_icon);
        txtTitle.setText(R.string.discoverpeople);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.header_icon:
            getActivity().onBackPressed();
            break;
    }
    }
}
