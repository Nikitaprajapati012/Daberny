package com.example.raviarchi.daberny.Activity.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi archi on 1/10/2017.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, MultiSelectSpinner.MultiSpinnerListener {
    private static final String TAG = "AskQuestion";
    public Utils utils;
    public UserProfileDetails details;
    @BindView(R.id.activity_setting_txtblockeduser)
    TextView txtBlockedUser;
    /*@BindView(R.id.multi_spinner)
    MultiSelectSpinner multiSpinner;*/
    private ArrayList<String> arrayfollowingUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
        click();
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        /*utils = new Utils(SettingActivity.this);
        arrayfollowingUserList = new ArrayList<>();
        arrayfollowingUserList.add("niki");
        arrayfollowingUserList.add("rujul");
        arrayfollowingUserList.add("pooja");
        multiSpinner.setItems(arrayfollowingUserList, getString(R.string.follow),this);*/
    }

    //public void onItemsSelected(boolean[] selected);
    private void click() {
        txtBlockedUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_setting_txtblockeduser:
                break;
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}


