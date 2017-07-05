package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Adapter.SearchChatPeopleAdapter;
import com.example.raviarchi.daberny.Activity.Adapter.SearchPeopleAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class SearchChatPeople extends Fragment {
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public SearchChatPeopleAdapter adapter;
    public String SearchPeople;
    private ArrayList<UserProfileDetails> arrayUserList,searchArrayList;
    public  EditText edSearch;
    public Toolbar toolBar;
    @BindView(R.id.header_icon)
    ImageView imgback;
    @BindView(R.id.header_iconplus)
    ImageView imgAdd;
    @BindView(R.id.header_title)
    TextView txtTitle;
    public RelativeLayout headerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_searchchatuser, container, false);
        ButterKnife.bind(this, view);
        headerView = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        headerView.setVisibility(View.GONE);
        txtTitle.setText(R.string.newmsg);
        findViewId(view);
        init();
        click();
        return view;
    }

    private void click() {
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewPeople = (RecyclerView) view.findViewById(R.id.fragment_search_recyclerpeoplelist);
        edSearch = (EditText) view.findViewById(R.id.fragment_searchdata_edsearch);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(),Constant.USERID);
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
                adapter = new SearchChatPeopleAdapter(getActivity(), searchArrayList);
                utils.setAdapterForList(recyclerViewPeople,adapter);

            }
        });
    }

    private void openPeopleList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new SearchChatPeopleAdapter(getActivity(), arrayUserList);
        utils.setAdapterForList(recyclerViewPeople,adapter);
    }


    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String searchpeople;

        public GetPeopleList(String searchPeople) {
            this.searchpeople = searchPeople;
        }

        @Override
        protected void onPreExecute()
        {
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
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "searched_user/" +userId + "/");
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
