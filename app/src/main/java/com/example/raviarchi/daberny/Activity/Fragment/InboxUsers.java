package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.raviarchi.daberny.Activity.Adapter.InboxUsersAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class InboxUsers extends Fragment implements View.OnClickListener {
    public Utils utils;
    public UserProfileDetails details;
    public String loginUserId;
    @BindView(R.id.fragment_inboxuser_recycler_inboxuserlist)
    RecyclerView recyclerViewInboxUsers;
    @BindView(R.id.headerview)
    RelativeLayout headerInboxView;
    @BindView(R.id.header_icon)
    ImageView imgback;
    @BindView(R.id.header_iconplus)
    ImageView imgAdd;
    @BindView(R.id.header_title)
    TextView txtTitle;
    private ArrayList<UserProfileDetails> arrayUserList;
    public Toolbar toolBar;
    public RelativeLayout headerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_inboxuser, container, false);
        ButterKnife.bind(this, view);
        init();
        headerView = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        headerView.setVisibility(View.GONE);
        txtTitle.setText("Inbox");
        imgAdd.setVisibility(View.VISIBLE);
        new GetInboxPeopleDetails().execute();
        click();
        return view;
    }

    // TODO: 2/21/2017 initilization
    private void init() {

        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
       loginUserId= Utils.ReadSharePrefrence(getActivity(),Constant.USERID);
    }

    private void click() {
        imgback.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_icon:
                getActivity().onBackPressed();
                break;
            case R.id.header_iconplus:
                Fragment fragment = new SearchChatPeople();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_contain_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }
    }

    private void openInboxPeopleDetailsList() {

        // TODO: 2/28/2017 set following peoplelist
        InboxUsersAdapter adapter = new InboxUsersAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewInboxUsers.setLayoutManager(mLayoutManager);
        recyclerViewInboxUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewInboxUsers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class GetInboxPeopleDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // http://181.224.157.105/~hirepeop/host2/surveys/api/get_chat_msg_inbox/752
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "get_chat_msg_inbox/" + loginUserId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Inbox People details..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject inboxDetailsObj = jsonObject.getJSONObject("inbox_details");
                    JSONArray inboxArray = inboxDetailsObj.getJSONArray("data");
                    for (int i = 0; i < inboxArray.length(); i++) {
                        JSONObject followingObject = inboxArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setUserId(followingObject.getString("sender_id"));
                        details.setUserUserName(followingObject.getString("username"));
                        details.setUserFullName(followingObject.getString("fullname"));
                        details.setUserImage(followingObject.getString("image"));
                        details.setUserMsgPostDate(followingObject.getString("post_date"));
                        details.setOtherUserId(followingObject.getString("recipient_id"));
                        details.setOtherUserFullName(followingObject.getString("r_fullname"));
                        details.setOtherUserName(followingObject.getString("r_username"));
                        details.setOtherUserImage(followingObject.getString("r_image"));
                        details.setUserMsgReceiver(followingObject.getString("recipient_msg"));
                        details.setUserMsgSender(followingObject.getString("sender_msg"));
                        details.setUserMsgPostDate(followingObject.getString("post_date"));
                        details.setUserMsgType(followingObject.getString("type"));
                        arrayUserList.add(details);
                    }
                    if (arrayUserList.size() > 0) {
                        openInboxPeopleDetailsList();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

