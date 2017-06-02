package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Adapter.NotificationAdapter;
import com.example.raviarchi.daberny.Activity.Adapter.SearchTagAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class Notification extends Fragment {
    public Toolbar toolBar;
    public TextView txtTitle;
    public Utils utils;
    public String userId;
    public ArrayList<UserProfileDetails> arrayUserList;
    @BindView(R.id.fragment_recycler_noficationlist)
    RecyclerView recyclerView;
    public NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this,view);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.notification);
        init();
        return view;
    }

    // TODO: 5/22/2017 show the list of notification
    private void init() {
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        utils = new Utils(getActivity());
        new GetNotificationList(userId).execute();
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetNotificationList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String user_id;

        public GetNotificationList(String userid) {
            this.user_id = userid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList=new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/user_notification/752
            String response = utils.getResponseofGet(Constant.QUESTION_BASE_URL + "user_notification/" + user_id);
            Log.d("RESPONSE", "Notification List..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray dateArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dateArray.length(); i++) {
                        JSONObject notifyObject = dateArray.getJSONObject(i);
                        UserProfileDetails details = new UserProfileDetails();
                        details.setQueNotificationId(notifyObject.getString("id"));
                        details.setQueNotificationType(notifyObject.getString("type"));
                        details.setQueNotificationStatus(notifyObject.getString("notifications_status"));
                        String contentId = notifyObject.getString("content_id");
                        if (!notifyObject.getString("type").equalsIgnoreCase("follow")){
                            details.setQueId(contentId);
                        }
                        JSONObject userObject = notifyObject.getJSONObject("user_detail");
                        details.setUserUserName(userObject.getString("username"));
                        details.setUserId(userObject.getString("user_id"));
                        arrayUserList.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (arrayUserList.size() > 0) {
                openNotificationList();
               }
        }
    }

    // TODO: 5/22/2017 set the lotification in list.
    private void openNotificationList() {
        adapter = new NotificationAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
