package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raviarchi.daberny.Activity.Adapter.SearchPeopleAdapter;
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

/**
 * Created by Ravi archi on 1/10/2017.
 */

public class SearchPeople extends Fragment {
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchPeopleAdapter adapter;
    public String SearchPeople;
    private ArrayList<UserProfileDetails> arrayUserList;

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

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayUserList.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchPeople = edSearch.getText().toString().trim();
                new GetPeopleList(SearchPeople).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
        userId = Utils.ReadSharePrefrence(getActivity(),Constant.USERID);
    }

    private void openPeopleList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchPeopleAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPeople.setLayoutManager(mLayoutManager);
        recyclerViewPeople.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPeople.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String searchpeople;

        public GetPeopleList(String searchPeople) {
            this.searchpeople = searchPeople;
        }

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

            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_user/752/nikita
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_user/" +userId + "/" + searchpeople);
            Log.d("RESPONSE", "Search People List..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray searchArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < searchArray.length(); i++) {
                        JSONObject userObject = searchArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setUserUserName(userObject.getString("username"));
                        details.setUserFullName(userObject.getString("fullname"));
                        details.setUserImage(userObject.getString("image"));
                        details.setUserId(userObject.getString("id"));
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
            pd.dismiss();
            if (arrayUserList.size() > 0) {
                openPeopleList();
            } else {
                arrayUserList.clear();
                //Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
