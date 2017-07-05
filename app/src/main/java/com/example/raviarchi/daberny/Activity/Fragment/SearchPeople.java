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

/*** Created by Ravi archi on 1/10/2017.
 */

public class SearchPeople extends Fragment {
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchPeopleAdapter adapter;
    private ArrayList<UserProfileDetails> arrayUserList, searchArrayList;

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
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        new GetPeopleList(userId).execute();
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchFor =edSearch.getText().toString();
                searchArrayList = new ArrayList<>();
                for (int i = 0; i < arrayUserList.size(); i++) {
                    if (arrayUserList.get(i).getUserUserName().toLowerCase().startsWith(searchFor.toLowerCase())) {
                        searchArrayList.add(arrayUserList.get(i));
                    }
                }
                adapter = new SearchPeopleAdapter(getActivity(), searchArrayList);
                utils.setAdapterForList(recyclerViewPeople,adapter);
            }
        });
    }

    private void openPeopleList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchPeopleAdapter(getActivity(), arrayUserList);
        utils.setAdapterForList(recyclerViewPeople,adapter);
    }


    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String user_id;

        private GetPeopleList(String userId) {
            this.user_id=userId;
        }

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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_user/752/
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_user/" + user_id + "/");
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
            }
        }
    }
}
