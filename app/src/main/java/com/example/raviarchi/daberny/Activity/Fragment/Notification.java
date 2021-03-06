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
import android.widget.RelativeLayout;
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
    public UserProfileDetails details;
    public RelativeLayout layoutHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this,view);
        layoutHeader = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        layoutHeader.setVisibility(View.VISIBLE);
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
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "user_notification/" + user_id);
            Log.d("RESPONSE", "Notification List..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject notifyObj = jsonObject.getJSONObject("notification_detail");
                    JSONObject userObject = notifyObj.getJSONObject("Login_user_detail");
                    JSONArray dataArray = notifyObj.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject notifyObjectdetails = dataArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setQueNotificationId(notifyObjectdetails.getString("id"));
                        details.setQueNotificationType(notifyObjectdetails.getString("type"));
                        details.setUserId(notifyObjectdetails.getString("user_id"));
                        details.setQueNotificationStatus(notifyObjectdetails.getString("notifications_status"));
                        details.setQueImage(notifyObjectdetails.getString("image"));
                        details.setQuePostDate(notifyObjectdetails.getString("post_date"));
                        String contentId = notifyObjectdetails.getString("content_id");
                        details.setContentId(contentId);
                        if (!notifyObjectdetails.getString("type").equalsIgnoreCase("follow")){
                            details.setQueId(contentId);
                        }
                        JSONObject userObjectdetails = notifyObjectdetails.getJSONObject("user_detail");
                        details.setUserUserName(userObjectdetails.getString("username"));
                        details.setUserFullName(userObjectdetails.getString("fullname"));
                        details.setUserId(userObjectdetails.getString("user_id"));
                        details.setUserImage(userObjectdetails.getString("user_image"));
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
        utils.setAdapterForList(recyclerView,adapter);
    }
}
