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

import com.example.raviarchi.daberny.Activity.Adapter.SearchLatestAdapter;
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
    public RecyclerView recyclerViewTag;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchTagAdapter adapter;
    public String SearchRecent;
    private ArrayList<UserProfileDetails> arrayUserList,searchedArraylist;

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
        recyclerViewTag = (RecyclerView) view.findViewById(R.id.fragment_search_recyclertaglist);
            }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        new GetTagList(userId).execute();
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SearchRecent = edSearch.getText().toString().replaceAll("#", "%23");
                searchedArraylist = new ArrayList<>();
                for (int i = 0; i < arrayUserList.size(); i++) {
                    if (arrayUserList.get(i).getQueTag().toLowerCase().startsWith(SearchRecent.toLowerCase())) {
                        searchedArraylist.add(arrayUserList.get(i));
                    }
                }
                adapter = new SearchTagAdapter(getActivity(), searchedArraylist);
                utils.setAdapterForList(recyclerViewTag,adapter);
            }
        });
    }

    private void openRecentList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchTagAdapter(getActivity(), arrayUserList);
        utils.setAdapterForList(recyclerViewTag,adapter);
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetTagList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String  user_id;

        public GetTagList(String userId) {
            this.user_id = userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/searched_tags/752/%23g
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_tags/" + user_id + "/");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Search Tag List..." + s);
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
