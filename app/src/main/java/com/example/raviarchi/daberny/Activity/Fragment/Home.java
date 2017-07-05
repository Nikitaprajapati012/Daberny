package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.HomeAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.koushikdutta.async.http.socketio.ExceptionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*** Created by Ravi archi on 1/10/2017.
 */

public class Home extends Fragment {
    public RecyclerView recyclerViewHome;
    public Utils utils;
    public String Interest, userId;
    public UserProfileDetails details;
    public HomeAdapter adapter;
    public LinearLayoutManager layoutManager;
    public Toolbar toolBar;
    public TextView txtTitle;
    public RelativeLayout layoutHeader;
    private ArrayList<UserProfileDetails> arrayUserList;
    private ArrayList<String> arrayFollowingNameList;
    private ArrayList<String> arrayFollowingIdList;
    private ArrayList<String> arrayInterestList;
    private ArrayList<String> arrayStartNameList;
    private ArrayList<String> arrayEndNameList;
    private ArrayList<String> arrayFollowingIDList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        findViewId(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (utils.isConnectingToInternet()) {
            new GetQuetionList().execute();
        } else {
            Toast.makeText(getActivity(), "Please Check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewHome = (RecyclerView) view.findViewById(R.id.fragment_home_recyclermainlist);
        layoutHeader = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        layoutHeader.setVisibility(View.VISIBLE);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.home);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
    }

    private void openQuetionList() {
        // TODO: 2/21/2017 bind list and show in adapter
        adapter = new HomeAdapter(getActivity(), arrayUserList, arrayFollowingNameList, arrayFollowingIdList);
        utils.setAdapterForList(recyclerViewHome, adapter);
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetQuetionList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String timing, remainTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
            arrayFollowingIdList = new ArrayList<>();
            arrayFollowingNameList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... s) {
            // http://181.224.157.105/~hirepeop/host2/surveys/api/home_que_api/752
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "home_que_api/" + userId);
            Log.d("RESPONSE", "All Question..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray quetionArray = jsonObject.getJSONArray("questions");
                    for (int k = 0; k < quetionArray.length(); k++) {
                        JSONObject questionObject = quetionArray.getJSONObject(k);
                        details = new UserProfileDetails();
                        details.setQueId(questionObject.getString("id"));
                        details.setQueTitle(questionObject.getString("title"));
                        details.setQueTag(questionObject.getString("tags"));
                        details.setQuePostDate(questionObject.getString("post_date"));
                        details.setQueOptionFirst(questionObject.getString("option1"));
                        details.setQueOptionSecond(questionObject.getString("option2"));
                        details.setQueOptionThird(questionObject.getString("option3"));
                        details.setQueOptionFourth(questionObject.getString("option4"));
                        details.setUserId(questionObject.getString("user_id"));
                        details.setUserInterestId(questionObject.getString("in_id"));
                        details.setQueVoteStatus(questionObject.getString("vote_status"));
                        details.setUserCanVote(questionObject.getString("can_vote"));

                        // TODO: 6/6/2017 set remain time
                        JSONObject remaintimeObj = questionObject.getJSONObject("remain_time");
                        if (remaintimeObj.length() > 0) {
                            remainTime = remaintimeObj.getString("remain_time");
                            details.setQueRemainTimeMiliSeconds(remaintimeObj.getLong("miliseconds"));
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                            try {
                                Date d = df.parse(remainTime);
                                long timeStamp = Math.abs(d.getTime());
                                details.setQueRemainTime(timeStamp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
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

                        // TODO: 5/26/2017 set vote count of particular question
                        JSONObject answerCountObj = answerObj.getJSONObject("answers_count");
                        details.setQueVoteCount1(answerCountObj.getString("1"));
                        details.setQueVoteCount2(answerCountObj.getString("2"));
                        details.setQueVoteCount3(answerCountObj.getString("3"));
                        details.setQueVoteCount4(answerCountObj.getString("4"));

                        // TODO: 5/26/2017 set vote percentage of particular question
                        JSONObject percentageCountObj = answerObj.getJSONObject("percentage_count");
                        details.setQueVotePercentage1(percentageCountObj.getString("1"));
                        details.setQueVotePercentage2(percentageCountObj.getString("2"));
                        details.setQueVotePercentage3(percentageCountObj.getString("3"));
                        details.setQueVotePercentage4(percentageCountObj.getString("4"));

                        // TODO: 3/22/2017 get user details
                        JSONObject userObject = questionObject.getJSONObject("user");
                        if (userObject.length() > 0) {
                            details.setUserImage(userObject.getString("user_image"));
                            details.setUserUserName(userObject.getString("username"));
                        }

                        // TODO: 3/22/2017 get comment details
                        JSONArray commentArray = questionObject.getJSONArray("comments");
                        if (commentArray.length() > 0) {
                            for (int c = 0; c < commentArray.length(); c++) {
                                JSONObject countcommentObject = commentArray.getJSONObject(c);
                                details.setQueComment(countcommentObject.getString("comment_text"));
                                details.setQueCommentId(countcommentObject.getString("id"));
                                details.setQueId(countcommentObject.getString("qid"));
                                details.setQueCommentUser(countcommentObject.getString("username"));
                                details.setQueCommentUserProfilePic(countcommentObject.getString("image"));
                                details.setQueCommentUserId(countcommentObject.getString("uid"));
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
                            long timeStamp = Math.abs(d.getTime());
                            details.setQueTiming(timeStamp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        arrayUserList.add(details);
                    }
                    // TODO: 3/27/2017 get the list of following
                    JSONArray followingArray = jsonObject.getJSONArray("following");
                    if (followingArray.length() > 0) {
                        for (int f = 0; f < followingArray.length(); f++) {
                            JSONObject followingObject = followingArray.getJSONObject(f);
                            arrayFollowingIdList.add(followingObject.getString("follow_user_id"));
                            String follower = followingObject.getString("username");
                            arrayFollowingNameList.add(follower);
                            Utils.WriteSharePrefrence(getActivity(), Constant.USER_FOLLOWING, follower);
                        }
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
                openQuetionList();
            } else {
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("EXP", "" + e.toString());
                    }
                };
            }
        }
    }
}
