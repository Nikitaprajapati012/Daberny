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

import com.example.raviarchi.daberny.Activity.Adapter.FollowingAdapter;
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

public class Following extends Fragment implements View.OnClickListener {
    public Utils utils;
    public UserProfileDetails details;
    public String ID;
    public ArrayList<String> arrayInterestList, arrayInterestIdList, arrayInterestEndName, arrayInterestStartName;
    @BindView(R.id.fragment_following_recycler_followinglist)
    RecyclerView recyclerViewFollowing;
    @BindView(R.id.fragment_following_imgback)
    ImageView imgback;
    private ArrayList<UserProfileDetails> arrayUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        ButterKnife.bind(this, view);
        init();
        new GetFollowingPeopleDetails().execute();
        click();
        return view;
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();

        if (getArguments() != null) {
            Gson gson = new Gson();
            String strObj = getArguments().getString("userprofiledetails");
            details = gson.fromJson(strObj, UserProfileDetails.class);
            ID = details.getUserId();
        }
    }

    private void click() {
        imgback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_following_imgback:
                getActivity().onBackPressed();
                break;
        }
    }

    private void openFollowingPeopleDetailsList() {

        // TODO: 2/28/2017 set following peoplelist
        FollowingAdapter adapter = new FollowingAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFollowing.setLayoutManager(mLayoutManager);
        recyclerViewFollowing.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFollowing.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class GetFollowingPeopleDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayInterestList = new ArrayList<>();
            arrayInterestIdList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // http://181.224.157.105/~hirepeop/host2/surveys/api//following_list/669
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "following_list/" + ID);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Following People details..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray followingArray = jsonObject.getJSONArray("following");
                    for (int i = 0; i < followingArray.length(); i++) {
                        JSONObject followingObject = followingArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setUserId(followingObject.getString("id"));
                        details.setUserUserName(followingObject.getString("username"));
                        details.setUserFullName(followingObject.getString("fullname"));
                        details.setUserEmail(followingObject.getString("email"));
                        details.setUserCountryId(followingObject.getString("country_id"));
                        details.setUserImage(followingObject.getString("user_image"));
                        JSONArray ranksArray = followingObject.getJSONArray("ranks");
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
                    if (arrayUserList.size() > 0 && arrayInterestList.size() > 0) {
                        openFollowingPeopleDetailsList();
                    } else {
                        Toast.makeText(getActivity(), "No Following People Found, Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}

