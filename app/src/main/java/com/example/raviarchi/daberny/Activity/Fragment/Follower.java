package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.FollowerAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

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

public class Follower extends Fragment implements View.OnClickListener {
    public Utils utils;
    public UserProfileDetails details;
    public String ID;
    public ArrayList<String> arrayInterestList, arrayInterestIdList, arrayInterestEndName, arrayInterestStartName;
    @BindView(R.id.fragment_follower_recycler_followerlist)
    RecyclerView recyclerViewFollower;
    @BindView(R.id.fragment_follower_imgback)
    ImageView imgBack;
    private ArrayList<UserProfileDetails> arrayUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_follower, container, false);
        ButterKnife.bind(this, view);
        init();
        new GetFollowerPeopleDetails().execute();
        click();
        return view;
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        if (getArguments() != null) {
            Gson gson = new Gson();
            String strObj = getArguments().getString("userprofiledetails");
            details = gson.fromJson(strObj, UserProfileDetails.class);
            ID = details.getUserId();
        }

    }

    private void click() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_follower_imgback:
                getActivity().onBackPressed();
                break;
        }
    }

    private void openFollowerPeopleDetailsList() {
        // TODO: 2/28/2017 set follower peoplelist
        FollowerAdapter adapter = new FollowerAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFollower.setLayoutManager(mLayoutManager);
        recyclerViewFollower.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFollower.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class GetFollowerPeopleDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://hire-people.com/host2/surveys/api/followers_list/669
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "followers_list/" + ID);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Follower People details..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray followerArray = dataObject.getJSONArray("followers");
                    for (int i = 0; i < followerArray.length(); i++) {
                        JSONObject followerObject = followerArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        arrayInterestList = new ArrayList<>();
                        details.setUserId(followerObject.getString("id"));
                        details.setUserUserName(followerObject.getString("username"));
                        details.setUserFullName(followerObject.getString("fullname"));
                        details.setUserEmail(followerObject.getString("email"));
                        details.setUserCountryId(followerObject.getString("country_id"));
                        details.setUserImage(followerObject.getString("user_image"));
                        JSONArray ranksArray = followerObject.getJSONArray("ranks");
                        arrayInterestList = new ArrayList<>();
                        arrayInterestStartName = new ArrayList<>();
                        arrayInterestEndName = new ArrayList<>();
                        for (int j = 0; j < ranksArray.length(); j++) {
                            JSONObject ranksObject = ranksArray.getJSONObject(j);
                            arrayInterestList.add(ranksObject.getString("int_name"));
                            arrayInterestStartName.add(ranksObject.getString("start_name"));
                            arrayInterestEndName.add(ranksObject.getString("end_name"));
                        }
                        details.setIntName(arrayInterestList);
                        details.setStartRankName(arrayInterestStartName);
                        details.setEndRankName(arrayInterestEndName);
                        arrayUserList.add(details);
                    }
                    if (arrayUserList.size() > 0) {
                        openFollowerPeopleDetailsList();
                    } else {
                        Toast.makeText(getActivity(), "No Follower People Found, Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}

