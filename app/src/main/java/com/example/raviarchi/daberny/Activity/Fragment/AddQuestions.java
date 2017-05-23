package com.example.raviarchi.daberny.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Activity.AskQuestionActivity;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class AddQuestions extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 0;
    @BindView(R.id.fragment_add_btnadd)
    TextView btnAdd;
    public Toolbar toolBar;
    public TextView txtTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, view);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.question);
        click();
        return view;
    }

    private void click() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_add_btnadd:
                Intent iask = new Intent(getActivity(), AskQuestionActivity.class);
                iask.putExtra("id", Utils.ReadSharePrefrence(getActivity(), Constant.USERID));
                startActivity(iask);
                break;
        }
    }
}
