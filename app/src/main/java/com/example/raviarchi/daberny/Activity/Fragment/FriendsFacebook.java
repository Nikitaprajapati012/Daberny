package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.FriendListAdapter;
import com.example.raviarchi.daberny.Activity.Adapter.SearchLatestAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ravi archi on 1/10/2017.
 */

public class FriendsFacebook extends Fragment {
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchLatestAdapter adapter;
    public String SearchLatest;
    public JSONArray rawName = new JSONArray();
    ArrayList<String> friendList;
    private ArrayList<UserProfileDetails> arrayUserList;
    private String fbAccessToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_people, container, false);
        init();
        findViewId(view);
        return view;
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewPeople = (RecyclerView) view.findViewById(R.id.fragment_search_recyclerpeoplelist);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
    }

    private void openLatestList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchLatestAdapter(getActivity().getSupportFragmentManager(), getActivity(), arrayUserList);
        utils.setAdapterForList(recyclerViewPeople, adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAccessToken = Utils.ReadSharePrefrence(getActivity(), Constant.FB_USER_ID);
        friendList = new ArrayList<>();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response != null)
                            try {
                                if (response.getJSONObject() != null)
                                    rawName = response.getJSONObject().getJSONArray("data");
                                if (rawName != null) {
                                    for (int l = 0; l < rawName.length(); l++) {
                                        friendList.add(rawName.getJSONObject(l).getString("name"));
                                    }
                                }
                                if (friendList.size() > 0) {
                                    FriendListAdapter friendAdapter = new FriendListAdapter(getActivity(), friendList);
                                    recyclerViewPeople.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    recyclerViewPeople.setAdapter(friendAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                }
        ).executeAsync();

    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String searchlatest;

        public GetPeopleList(String searchPeople) {
            this.searchlatest = searchPeople;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_question/752/How many
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_question/" + userId + "/" + searchlatest);
            Log.d("RESPONSE", "Search latest List..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray searchArray = jsonObject.getJSONArray("searchd result");
                    for (int i = 0; i < searchArray.length(); i++) {
                        JSONObject userObject = searchArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setQueTitle(userObject.getString("title"));
                        details.setQueId(userObject.getString("id"));
                        arrayUserList.add(details);
                    }
                } else {
                    if (jsonObject.getString("status").equalsIgnoreCase("FALSE")) {
                        arrayUserList.clear();
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
            if (arrayUserList.size() > 0) {
                openLatestList();
            } else {
                arrayUserList.clear();
            }
        }
    }
}
