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
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.SearchTagAdapter;
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

public class SearchTag extends Fragment {
    public RecyclerView recyclerViewRecent;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchTagAdapter adapter;
    public String SearchRecent;
    private ArrayList<UserProfileDetails> arrayUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_tag, container, false);
        init();
        findViewId(view);
        return view;
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewRecent = (RecyclerView) view.findViewById(R.id.fragment_search_recyclertaglist);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayUserList.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchRecent = edSearch.getText().toString().trim();
                new GetTagList(userId,SearchRecent).execute();
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

    private void openRecentList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchTagAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRecent.setLayoutManager(mLayoutManager);
        recyclerViewRecent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetTagList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String searchRecent,user_id;

        public GetTagList(String userId, String searchRecent) {
            this.searchRecent = searchRecent;
            this.user_id=userId;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_tags/752/%23g
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_tags/" + user_id +"/"+searchRecent);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Search Tag List..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray searchArray = jsonObject.getJSONArray("searchd result");
                    for (int i = 0; i < searchArray.length(); i++) {
                        JSONObject userObject = searchArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setQueTag(userObject.getString("tags"));
                        details.setQueTitle(userObject.getString("title"));
                        details.setQueId(userObject.getString("id"));
                        arrayUserList.add(details);
                    }
                }
                else {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                // if (searchRecent.equalsIgnoreCase(details.getQueTitle())) {
                openRecentList();
               /* } else {
                    arrayUserList.clear();
                    Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();
                }*/

            } else {

                //Toast.makeText(getActivity(), "No Data Found,Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
