package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raviarchi.daberny.Activity.Adapter.SearchLatestAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.raviarchi.daberny.Activity.Fragment.Search.edSearch;
import static com.facebook.FacebookSdk.getApplicationContext;

/*** Created by Ravi archi on 1/10/2017.
 */

public class SearchLatest extends Fragment {
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchLatestAdapter adapter;
    public String SearchLatest;
    private ArrayList<UserProfileDetails> arrayUserList, searchedArraylist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_recent, container, false);
        init();
        findViewId(view);
        return view;
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewPeople = (RecyclerView) view.findViewById(R.id.fragment_search_recyclerrecentlist);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getApplicationContext());
        userId = Utils.ReadSharePrefrence(getApplicationContext(), Constant.USERID);
        new GetPeopleList(userId).execute();
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchFor = edSearch.getText().toString().trim();
                searchedArraylist = new ArrayList<>();
                for (int i = 0; i < arrayUserList.size(); i++) {
                    if (arrayUserList.get(i).getQueTitle().toLowerCase().startsWith(searchFor.toLowerCase())) {
                        searchedArraylist.add(arrayUserList.get(i));
                    }
                }
                // if (searchFor.length() > 0) {
                if (searchedArraylist.size() > 0) {
                    adapter = new SearchLatestAdapter(getFragmentManager(), getApplicationContext(), searchedArraylist);
                    utils.setAdapterForList(recyclerViewPeople, adapter);
                }
                //  }
            }
        });
    }

    private void openLatestList() {
        // TODO: 2/21/2017 bind list and show in adapter
        // if (edSearch.getText().toString().length() > 0) {
        adapter = new SearchLatestAdapter(getFragmentManager(), getApplicationContext(), arrayUserList);
        utils.setAdapterForList(recyclerViewPeople, adapter);
        // }
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String searchlatest;

        private GetPeopleList(String searchPeople) {
            this.searchlatest = searchPeople;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_question/752/How many
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_question/" + userId);
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
            }
        }
    }
}
