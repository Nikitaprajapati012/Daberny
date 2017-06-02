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
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Adapter.GeneralAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class General extends Fragment {
    public RecyclerView recyclerViewposts;
    public Utils utils;
    public UserProfileDetails details;
    public RecyclerView recyclerViewHome;
    private ArrayList<String> arrayInterestList;
    private ArrayList<String> arrayFollowingNameList;
    private ArrayList<String> arrayFollowingIdList;
    private ArrayList<String> arrayStartNameList;
    private ArrayList<String> arrayEndNameList;
    private ArrayList<UserProfileDetails> arrayUserList;
    public Toolbar toolBar;
    public TextView txtTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        init();
        findViewId(view);
        new GetQuetionList().execute();
        click();
        return view;
    }

    // TODO: 2/22/2017 click perform
    private void click() {

    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewposts = (RecyclerView) view.findViewById(R.id.fragment_general_recyclermainlist);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.general);
    }


    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
    }

    private void openQuetionList() {
        // TODO: 2/21/2017 bind list and show in adapter
        Collections.reverse(arrayUserList);
        GeneralAdapter adapter = new GeneralAdapter(getActivity(), arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewposts.setLayoutManager(mLayoutManager);
        recyclerViewposts.setItemAnimator(new DefaultItemAnimator());
        recyclerViewposts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    // TODO: 2/21/2017 get list of Question from URL
    private class GetQuetionList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String timing;

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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/all_questions/669/0
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "all_questions/" + Utils.ReadSharePrefrence(getActivity(),Constant.USERID) +"/0");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "General Question..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray userquetionArray = jsonObject.getJSONArray("user_questions");
                    for (int i = 0; i < userquetionArray.length(); i++) {
                        JSONArray quetionArray = userquetionArray.getJSONArray(i);
                        for (int k = 0; k < quetionArray.length(); k++) {
                            JSONObject questionObject = quetionArray.getJSONObject(k);
                            details = new UserProfileDetails();
                            details.setQueId(questionObject.getString("id"));
                            details.setQueTitle(questionObject.getString("title"));
                            details.setQuePostDate(questionObject.getString("post_date"));
                            details.setQueOptionFirst(questionObject.getString("option1"));
                            details.setQueOptionSecond(questionObject.getString("option2"));
                            details.setQueOptionThird(questionObject.getString("option3"));
                            details.setQueOptionFourth(questionObject.getString("option4"));
                            details.setUserId(questionObject.getString("user_id"));
                            details.setUserInterestId(questionObject.getString("in_id"));
                            details.setQueVoteStatus(questionObject.getString("vote_status"));
                            details.setUserCanVote(questionObject.getString("can_vote"));
                            // TODO: 3/29/2017 get remaining  time

                            details.setQueCategory(questionObject.getString("name"));
                            details.setQueType(questionObject.getString("type"));
                            details.setQueImageName(questionObject.getString("picture"));
                            if (!questionObject.getString("picture").equalsIgnoreCase("")) {
                                if (questionObject.getString("type").equalsIgnoreCase("0")) {
                                    details.setQueImage(questionObject.getString("picture_url"));
                                } else if (questionObject.getString("type").equalsIgnoreCase("1")) {
                                    details.setQueImage(questionObject.getString("picture_url"));
                                } else if (questionObject.getString("type").equalsIgnoreCase("2")) {
                                    details.setQueImage(questionObject.getString("picture_url"));
                                }
                            }
                            JSONObject answerObj = questionObject.getJSONObject("answers");
                            details.setQueVoteTotalCount(answerObj.getInt("total_count"));
                            details.setQueLikeStatus(answerObj.getString("liked"));
                            details.setQueLikeTotalCount(answerObj.getInt("likes_count"));
                            // TODO: 3/25/2017 get like object
                            // JSONObject userlike = answerObj.getJSONObject("likes");
                            // TODO: 3/22/2017 get user details
                            JSONObject userObject = questionObject.getJSONObject("user");
                            details.setUserImage(userObject.getString("user_image"));
                            details.setUserUserName(userObject.getString("username"));

                            // TODO: 3/22/2017 get comment details
                            JSONArray commentArray = questionObject.getJSONArray("comments");
                            if (commentArray.length() > 0) {
                                for (int c = 0; c < commentArray.length(); c++) {
                                    JSONObject countcommentObject = commentArray.getJSONObject(c);
                                    details.setQueComment(countcommentObject.getString("comment_text"));
                                    details.setQueCommentId(countcommentObject.getString("id"));
                                    details.setQueId(countcommentObject.getString("qid"));
                                    details.setQueCommentUserId(countcommentObject.getString("uid"));
                                    details.setQueCommentUser(countcommentObject.getString("username"));
                                }
                            }
                            // TODO: 3/22/2017 get data of rank_interest
                            JSONArray rankArray = questionObject.getJSONArray("rank_interest");
                            arrayInterestList = new ArrayList<>();
                            arrayStartNameList = new ArrayList<>();
                            arrayEndNameList = new ArrayList<>();
                            for (int r = 0; r < rankArray.length(); r++) {
                                JSONObject rankdetailObj = rankArray.getJSONObject(r);
                                arrayInterestList.add(rankdetailObj.getString("int_name"));
                                arrayStartNameList.add(rankdetailObj.getString("start_name"));
                                arrayEndNameList.add(rankdetailObj.getString("end_name"));
                            }
                            details.setIntName(arrayInterestList);
                            details.setStartRankName(arrayStartNameList);
                            details.setEndRankName(arrayEndNameList);

                            // TODO: 3/29/2017 take timing into miliseconds
                            String created_time = questionObject.getString("created_time");
                            details.setQueCreatedTime(Long.valueOf(created_time));
                            timing = questionObject.getString("timing");
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                            try {
                                Date d = df.parse(timing);
                                long timeStamp = d.getTime();
                                details.setQueTiming(timeStamp);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                      /*  // TODO: 3/27/2017 get the list of following
                        JSONArray followingArray = jsonObject.getJSONArray("following");
                        for (int i = 0; i < followingArray.length(); i++) {
                            JSONObject followingObject = followingArray.getJSONObject(i);
                            arrayFollowingIdList.add(followingObject.getString("follow_user_id"));
                            arrayFollowingNameList.add(followingObject.getString("fullname"));
                            details.setUserFollowingId(arrayFollowingIdList);
                            details.setUserFollowingName(arrayFollowingNameList);
                        }*/
                            arrayUserList.add(details);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                openQuetionList();

            } else {
                //Toast.makeText(getActivity(), "No Question Found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
