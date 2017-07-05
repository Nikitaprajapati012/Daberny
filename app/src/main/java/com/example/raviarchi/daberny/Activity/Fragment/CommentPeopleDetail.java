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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.CommentPeopleAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.koushikdutta.async.http.socketio.ExceptionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class CommentPeopleDetail extends Fragment implements View.OnClickListener {
    //  @BindView(R.id.fragment_comment_peopledetails_imgcomment)
    public static ImageView imgCommentSent;
    // @BindView(R.id.fragment_comment_peopledetails_edcomment)
    public static EditText edComment;
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String queId, loginUserId;
    public CommentPeopleAdapter adapter;
    public Toolbar toolBar;
    public RelativeLayout headerView;
    @BindView(R.id.headerview_edit)
    RelativeLayout layoutHeader;
    @BindView(R.id.header_icon)
    ImageView imgBack;
    @BindView(R.id.header_title)
    TextView txtTitle;
    private ArrayList<UserProfileDetails> arrayUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_comment_peopledetail, container, false);
        ButterKnife.bind(this, view);
        findViewId(view);
        init();
        new GetPeopleList().execute();
        click();
        return view;
    }

    // TODO: 3/28/2017 perform click
    private void click() {
        imgBack.setOnClickListener(this);
        imgCommentSent.setOnClickListener(this);
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewPeople = (RecyclerView) view.findViewById(R.id.fragment_comment_recyclerpeoplelist);
        headerView = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        headerView.setVisibility(View.GONE);
        txtTitle.setText(R.string.comments);
        imgBack.setVisibility(View.VISIBLE);
        imgCommentSent = (ImageView) view.findViewById(R.id.fragment_comment_peopledetails_imgcomment);
        edComment = (EditText) view.findViewById(R.id.fragment_comment_peopledetails_edcomment);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
        queId = getArguments().getString("queid");
        loginUserId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
    }

    private void openPeopleList(ArrayList<UserProfileDetails> arrayList) {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new CommentPeopleAdapter(getActivity(), arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.scrollToPosition(arrayList.size()-1);
        recyclerViewPeople.setLayoutManager(mLayoutManager);
        recyclerViewPeople.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPeople.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_comment_peopledetails_imgcomment:
                String commentText = edComment.getText().toString().replaceAll(" ", "%20");
                if (commentText.length() > 0) {
                    new CommentPost(arrayUserList, loginUserId, queId, commentText).execute();
                    edComment.setText(" ");
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.header_icon:
                getActivity().onBackPressed();
                break;
        }
    }


    // TODO: 2/21/2017 get list of Question from URL
    private class GetPeopleList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/get_question_comented_user_list/709
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "get_question_comented_user_list/" + queId);
            Log.d("RESPONSE", "Comment details people List..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray commentArray = jsonObject.getJSONArray("inserted_data");
                    for (int i = 0; i < commentArray.length(); i++) {
                        JSONObject userObject = commentArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setUserId(userObject.getString("user id"));
                        details.setQueCommentUser(userObject.getString("user name"));
                        details.setQueComment(userObject.getString("user comment"));
                        details.setQueCommentUserProfilePic(userObject.getString("user image"));
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
                openPeopleList(arrayUserList);
            } else {
                Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class CommentPost extends AsyncTask<String, String, String> {
        String id, queId, comment;
        ProgressDialog pd;
        ArrayList<UserProfileDetails> arrayList;

        private CommentPost(ArrayList<UserProfileDetails> arrayList, String id, String queId, String comment) {
            this.id = id;
            this.queId = queId;
            this.comment = comment;
            this.arrayList = arrayList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/add_comment/806/161/hello%20niki
            try {
                return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "add_comment/" + id + "/" + queId + "/" + URLEncoder.encode(comment, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Comment the Post..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray commentArray = jsonObject.getJSONArray("inserted_data");
                    for (int i = 0; i < commentArray.length(); i++) {
                        JSONObject commentObject = commentArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setQueComment(commentObject.getString("comment_text"));
                        details.setQueCommentUserProfilePic(commentObject.getString("image"));
                        details.setQueCommentUser(commentObject.getString("username"));
                        details.setQueCommentUserId(commentObject.getString("uid"));
                        arrayList.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                openPeopleList(arrayList);
            } else {
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("exception", "" + e);
                    }
                };
            }
        }
    }
}
