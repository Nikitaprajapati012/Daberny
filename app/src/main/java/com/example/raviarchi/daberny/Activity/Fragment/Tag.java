package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.raviarchi.daberny.Activity.Adapter.HomeAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiplespinner.MultiSelectionSpinner;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.raviarchi.daberny.Activity.Utils.Constant.ALLQUESTION;
import static com.example.raviarchi.daberny.Activity.Utils.Utils.WriteSharePrefrence;


/**
 * Created by Ravi archi on 1/10/2017.
 */

public class Tag extends Fragment implements View.OnClickListener {
    public String ID, userId;
    public int isLikedCount;
    public long time;
    public Utils utils;
    public UserProfileDetails details;
    public CountDownTimer countdowntimer;
    @BindView(R.id.adapter_home_list_layoutfb)
    LinearLayout layoutFacebook;
    @BindView(R.id.adapter_home_list_layouttwitter)
    LinearLayout layoutTwitter;
    @BindView(R.id.adapter_home_list_txtusername)
    TextView txtUserName;
    @BindView(R.id.adapter_home_list_txthour)
    TextView txtHour;
    @BindView(R.id.adapter_home_list_txtminute)
    TextView txtMinute;
    @BindView(R.id.adapter_home_list_txtsecond)
    TextView txtSecond;
    @BindView(R.id.adapter_home_list_txtinterest1)
    TextView txtInterest1;
    @BindView(R.id.adapter_home_list_txtinterest2)
    TextView txtInterest2;
    @BindView(R.id.adapter_home_list_txtinterest3)
    TextView txtInterest3;
    @BindView(R.id.adapter_home_list_txtlevel1)
    TextView txtLevel1;
    @BindView(R.id.adapter_home_list_txtlevel2)
    TextView txtLevel2;
    @BindView(R.id.adapter_home_list_txtlevel3)
    TextView txtLevel3;
    @BindView(R.id.adapter_home_list_txtvotecount)
    TextView txtVoteCount;
    @BindView(R.id.adapter_home_list_txtvote)
    TextView txtVote;
    @BindView(R.id.adapter_home_list_txtcomment)
    TextView txtComment;
    @BindView(R.id.adapter_home_list_edcomment)
    EditText edCommentText;
    @BindView(R.id.adapter_home_list_txtque)
    TextView txtQuetion;
    @BindView(R.id.adapter_home_list_txtcategory)
    TextView txtCategory;
    @BindView(R.id.adapter_home_list_txtdate)
    TextView txtPostDate;
    @BindView(R.id.adapter_home_list_txtcommenttext)
    TextView txtCommentText;
    @BindView(R.id.adapter_home_list_txtcommentuser)
    TextView txtCommentUser;
    @BindView(R.id.adapter_home_list_txtviewallcomments)
    TextView txtViewAllComments;
    @BindView(R.id.adapter_home_list_txtlikecount)
    TextView txtLikeCount;
    @BindView(R.id.adapter_home_list_rdoption1)
    RadioButton rdAnswer1;
    @BindView(R.id.adapter_home_list_rdoption2)
    RadioButton rdAnswer2;
    @BindView(R.id.adapter_home_list_rdoption3)
    RadioButton rdAnswer3;
    @BindView(R.id.adapter_home_list_rdoption4)
    RadioButton rdAnswer4;
    @BindView(R.id.adapter_home_list_imgquepic)
    ImageView imgQuestionPic;
    @BindView(R.id.adapter_home_list_imgprofilepic)
    ImageView imgProfilePic;
    @BindView(R.id.adapter_home_list_vdquevideo)
    VideoView vdProfile;
    @BindView(R.id.adapter_home_list_imglike)
    ImageView imgLike;
    @BindView(R.id.adapter_home_list_layout_like)
    LinearLayout layoutLike;
    @BindView(R.id.adapter_home_list_layout_comment)
    LinearLayout layoutComment;
    @BindView(R.id.adapter_home_list_layout_vote)
    LinearLayout layoutVote;
    @BindView(R.id.adapter_home_list_media)
    LinearLayout layoutMedia;
    @BindView(R.id.adapter_home_list_beforecounter)
    LinearLayout layoutCounter;
    MultiSelectionSpinner multiSelectionSpinnerFollowing;
    float progress, remainingProgress;
    RelativeLayout relativeProgress, relativeRemaining;
    @BindView(R.id.linear)
    LinearLayout layoutProgressController;
    private ArrayList<String> arrayInterestList;
    private ArrayList<String> arrayStartNameList;
    private ArrayList<String> arrayEndNameList;
    private ArrayList<String> arrayFollowingNameList;
    private ArrayList<String> arrayFollowingIdList;
    private ArrayList<UserProfileDetails> arrayUserList;
    private ArrayList<String> followingList;
    private Object[] getFollowingListSpinner;
    private String Id, queId, Answer, taskVote, isVoteStatus, followingPeopleName, commentText, isLiked, liketask;
    private ArrayList<UserProfileDetails> arrayList;
    private int seconds, minutes, hours;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.adapter_home_list, container, false);
        ButterKnife.bind(this, view);
        init();
        new GetQuetionList().execute();
        click();
        return view;
    }

    // TODO: 2/22/2017 click perform
    private void click() {
        rdAnswer3.setVisibility(View.VISIBLE);
        rdAnswer4.setVisibility(View.VISIBLE);
        txtCommentUser.setOnClickListener(this);
        txtUserName.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        layoutFacebook.setOnClickListener(this);
        layoutTwitter.setOnClickListener(this);
        txtVote.setOnClickListener(this);
        txtViewAllComments.setOnClickListener(this);
        txtComment.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        rdAnswer1.setOnClickListener(this);
        rdAnswer2.setOnClickListener(this);
        rdAnswer3.setOnClickListener(this);
        rdAnswer4.setOnClickListener(this);
        txtVote.setOnClickListener(this);
        //spinnnerFollowing.setListener(this);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(getActivity());
        Id = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        arrayUserList = new ArrayList<>();
        if (getArguments() != null) {
            Gson gson = new Gson();
            String strObj = getArguments().getString("userprofiledetails");
            details = gson.fromJson(strObj, UserProfileDetails.class);
            ID = details.getQueId();
            userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        }
    }

    // TODO: 4/11/2017 set the tag details
    private void openQuetionList() {
        rdAnswer3.setVisibility(View.VISIBLE);
        rdAnswer4.setVisibility(View.VISIBLE);
        rdAnswer1.setText(details.getQueOptionFirst());
        rdAnswer2.setText(details.getQueOptionSecond());
        txtVote.setVisibility(View.VISIBLE);
        txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
        txtUserName.setText(details.getUserUserName());
        txtCategory.setText(details.getQueCategory());
        txtPostDate.setText(details.getQuePostDate());
        txtQuetion.setText(details.getQueTitle());
        txtCommentText.setText(details.getQueComment());
        txtCommentUser.setText(details.getQueCommentUser());
        txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
        txtUserName.setText(details.getUserUserName());
        txtCategory.setText(details.getQueCategory());
        txtPostDate.setText(details.getQuePostDate());
        txtQuetion.setText(details.getQueTitle());
        txtCommentText.setText(details.getQueComment());
        txtCommentUser.setText(details.getQueCommentUser());
        queId = details.getQueId();

        // TODO: 4/11/2017 dynamic controller
        setProgress(20f);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // TODO: 4/11/2017 view for done progress layout
        View view = li.inflate(R.layout.linear_progress, null);
        relativeProgress = (RelativeLayout) view.findViewById(R.id.linear_progress_relative);
        relativeProgress.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, progress));
        layoutProgressController.addView(view);
        // TODO: 4/11/2017 view for remaining progress layout
        View view1 = li.inflate(R.layout.linear_remaining, null);
        relativeRemaining = (RelativeLayout) view1.findViewById(R.id.linear_remaining_relative);
        relativeRemaining.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, remainingProgress));
        layoutProgressController.addView(view1);

        // TODO: 3/21/2017 countdown timer
        Long timimg = details.getQueTiming();
        Long createdtime = details.getQueCreatedTime();
        Log.d("timing_que", "" + timimg);

        // countdowntimer = new CountDownTimerClass(holder, timimg, 1000).start();
        countdowntimer = new CountDownTimer(10000, 1000) {
            public void onFinish() {
                rdAnswer1.setClickable(false);
                rdAnswer2.setClickable(false);
                rdAnswer3.setClickable(false);
                rdAnswer4.setClickable(false);
                layoutCounter.setVisibility(View.GONE);
                layoutVote.setVisibility(View.GONE);
                layoutLike.setVisibility(View.VISIBLE);
                layoutComment.setVisibility(View.VISIBLE);
               /*optionProgress1.setVisibility(View.VISIBLE);
               optionProgress2.setVisibility(View.VISIBLE);
               optionProgress3.setVisibility(View.VISIBLE);
               optionProgress4.setVisibility(View.VISIBLE);
               rdAnswer1.setVisibility(View.GONE);
               rdAnswer2.setVisibility(View.GONE);
               rdAnswer3.setVisibility(View.GONE);
               rdAnswer4.setVisibility(View.GONE);*/
            }

            @Override
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000) % 60;
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                txtHour.setText("" + String.format("%2d", hours));
                txtMinute.setText("" + String.format("%2d", minutes));
                txtSecond.setText("" + String.format("%2d", seconds));
                layoutVote.setVisibility(View.VISIBLE);
                layoutLike.setVisibility(View.GONE);
                layoutComment.setVisibility(View.GONE);
                layoutCounter.setVisibility(View.VISIBLE);
               /*rdAnswer1.setVisibility(View.VISIBLE);
               rdAnswer2.setVisibility(View.VISIBLE);
               rdAnswer3.setVisibility(View.VISIBLE);
               rdAnswer4.setVisibility(View.VISIBLE);
               optionProgress1.setVisibility(View.GONE);
               optionProgress2.setVisibility(View.GONE);
               optionProgress3.setVisibility(View.GONE);
               optionProgress4.setVisibility(View.GONE);*/
            }
        }.start();

        // TODO: 4/11/2017 set current satus for like
        if (details.getQueLikeStatus().equalsIgnoreCase("1")) {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_heart_selected));
        } else {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_heart_unselected));
        }
        isVoteStatus = details.getQueVoteStatus();
        taskVote = isVoteStatus.equalsIgnoreCase("1") ? "hide" : "show";
        //for 0
        if (taskVote.equalsIgnoreCase("show")) {
            txtVote.setVisibility(View.VISIBLE);
            txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
            details.setQueVoteStatus("1");
        } else {
            //for 1
            rdAnswer1.setClickable(false);
            rdAnswer2.setClickable(false);
            rdAnswer3.setClickable(false);
            rdAnswer4.setClickable(false);
            txtVote.setVisibility(View.GONE);
            txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
        }
        if (details.getIntName().size() > 0
                ) {
            txtInterest1.setText(details.getIntName().get(0));
        } else {
            txtInterest1.setText("");
        }
        if (details.getIntName().size() > 1) {
            txtInterest2.setText(details.getIntName().get(1));
        } else {
            txtInterest2.setText("");
        }
        if (details.getIntName().size() > 2) {
            txtInterest3.setText(details.getIntName().get(2));
        } else {
            txtInterest3.setText("");
        }

        if (!details.getQueOptionThird().equalsIgnoreCase("")) {
            rdAnswer3.setText(details.getQueOptionThird());
        } else {
            rdAnswer3.setVisibility(View.GONE);
            //optionProgress3.setVisibility(View.GONE);

        }
        if (!details.getQueOptionFourth().equalsIgnoreCase("")) {
            rdAnswer4.setText(details.getQueOptionFourth());
        } else {
            rdAnswer4.setVisibility(View.GONE);
            //optionProgress4.setVisibility(View.GONE);
        }

        if (details.getUserImage() != null) {
            if (details.getUserImage().length() > 0) {
                Picasso.with(getActivity()).load(details.getUserImage()).placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
            }
        } else {
            Picasso.with(getActivity()).load(R.mipmap.ic_launcher).placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
        }

        // TODO: 2/23/2017 set image & video dynamically
        if (details.getQueImageName().length() > 0) {
            if (details.getQueImage() != null) {
                imgQuestionPic.setVisibility(View.VISIBLE);
                vdProfile.setVisibility(View.VISIBLE);
                layoutMedia.setVisibility(View.VISIBLE);
                if (details.getQueType().equalsIgnoreCase("0")) {
                    layoutMedia.setVisibility(View.GONE);
                } else if (details.getQueType().equalsIgnoreCase("1")) {
                    vdProfile.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(details.getQueImage()).placeholder(R.drawable.ic_placeholder).into(imgQuestionPic);
                } else if (details.getQueType().equalsIgnoreCase("2")) {
                    imgQuestionPic.setVisibility(View.GONE);
                    vdProfile.setVideoURI(Uri.parse(details.getQueImage()));
                    vdProfile.setMediaController(new MediaController(getActivity()));
                    vdProfile.requestFocus();
                    vdProfile.start();
                }
            }
        } else {
            layoutMedia.setVisibility(View.GONE);
        }

    }

    // TODO: 4/11/2017  set progress
    private void setProgress(float v) {
        progress = v;
        remainingProgress = 100 - progress;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        PackageManager pm = getActivity().getPackageManager();
        switch (v.getId()) {
            case R.id.adapter_home_list_layoutfb:
                Intent ifeb = new Intent("android.intent.category.LAUNCHER");
                ifeb.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
                getActivity().startActivity(ifeb);
                break;

            case R.id.adapter_home_list_layouttwitter:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.twitter.android", "com.twitter.android.LoginActivity");
                getActivity().startActivity(intent);
                break;

            case R.id.adapter_home_list_imglike:
                String taskLike = "";
                int taskLikeCount = 0;
                String isLiked = details.getQueLikeStatus();
                int isLikedCount = details.getQueLikeTotalCount();
                Log.d("like_status", details.getQueLikeStatus());
                if (isLiked.equalsIgnoreCase("0")) {
                    details.setQueLikeStatus("1");
                    details.setQueLikeTotalCount(details.getQueLikeTotalCount() + 1);
                } else {
                    details.setQueLikeStatus("0");
                    details.setQueLikeTotalCount(details.getQueLikeTotalCount() - 1);
                }
                taskLike = isLiked.equalsIgnoreCase("0") ? "add" : "remove";
                new LikePost(arrayList, Utils.ReadSharePrefrence(getActivity(), Constant.USERID), queId, taskLike).execute();
                break;

            case R.id.adapter_home_list_txtvote:
                Answer = Utils.ReadSharePrefrence(getActivity(), Constant.ANSWER);
                //isVoteStatus = details.getQueVoteStatus();
                taskVote = isVoteStatus.equalsIgnoreCase("1") ? "hide" : "show";
                //if vote stutus =0
                if (taskVote.equalsIgnoreCase("show")) {
                    txtVote.setVisibility(View.VISIBLE);
                    txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
                    details.setQueVoteStatus("1");
                } else {
                    // for 1
                    txtVote.setVisibility(View.GONE);
                    txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
                }
                if (Answer.length() > 0) {
                    Log.d("userid", Id);
                    Log.d("queid", queId);
                    Log.d("ansid", Answer);
                    new SubmitVote(Id, queId, Answer, arrayList).execute();
                } else {
                    Toast.makeText(getActivity(), "Please Select Your Answer", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.adapter_home_list_txtviewallcomments:
                //just pass question Id
                fragment = new CommentPeopleDetail();
                bundle.putString("queid", details.getQueId());
                break;
            case R.id.adapter_home_list_txtcomment:
                // TODO: 3/27/2017 store the comment
                commentText = edCommentText.getText().toString().replaceAll(" ", "%20");
                new CommentPost(arrayList, Id, queId, commentText).execute();
                break;

            case R.id.adapter_home_list_rdoption1:
                WriteSharePrefrence(getActivity(), Constant.ANSWER, "1");
                break;
            case R.id.adapter_home_list_rdoption2:
                WriteSharePrefrence(getActivity(), Constant.ANSWER, "2");
                break;
            case R.id.adapter_home_list_rdoption3:
                WriteSharePrefrence(getActivity(), Constant.ANSWER, "3");
                break;
            case R.id.adapter_home_list_rdoption4:
                WriteSharePrefrence(getActivity(), Constant.ANSWER, "4");
                break;

            case R.id.adapter_home_list_txtcommentuser:
                fragment = new OtherUserProfile();
                bundle.putString("id", details.getQueCommentUserId());
                break;
            case R.id.adapter_home_list_txtusername:
                fragment = new OtherUserProfile();
                bundle.putString("id", details.getUserId());
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.frame_contain_layout, fragment);
            transaction.commit();
        }
    }

    // TODO: 4/11/2017 show like & unlike
    private void showLikeDisLike() {
        if (details.getQueLikeStatus().equalsIgnoreCase("1")) {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_heart_selected));
        } else {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_heart_unselected));
        }
    }

    // TODO: 3/27/2017 store the comments
    private class CommentPost extends AsyncTask<String, String, String> {
        String id, queId, comment;
        ProgressDialog pd;
        ArrayList<UserProfileDetails> arrayList;
        HomeAdapter.MyViewHolder holder;

        public CommentPost(ArrayList<UserProfileDetails> arrayList, String id, String queId, String comment) {
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
                return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "add_comment/" + id + "/" + queId + "/" + URLEncoder.encode(comment, "utf-8"));
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
                        arrayList.add(details);
                    }
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (arrayList.size() > 0) {
                txtCommentText.setText(details.getQueComment());
                txtCommentUser.setText(details.getQueCommentUser());
            } else {
                Toast.makeText(getActivity(), "Please Comment", Toast.LENGTH_SHORT).show();
            }*/
        }

    }

    // TODO: 3/23/2017 submit vote
    private class SubmitVote extends AsyncTask<String, String, String> {
        String id, queId, answer;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;

        public SubmitVote(String id, String queId, String answer, ArrayList<UserProfileDetails> arrayList) {
            this.id = id;
            this.queId = queId;
            this.answer = answer;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/qustion_wotting/666/66/4
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "qustion_wotting/" + id + "/" + queId + "/" + answer);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Voting..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject voteObject = jsonObject.getJSONObject("inserted_data");
                    UserProfileDetails details = new UserProfileDetails();
                    details.setQueAnswer(voteObject.getString("vote_opn_val"));
                    arrayList.add(details);
                    Toast.makeText(getActivity(), "Success! Thanks for vote..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Vote Not Submit,Please Try Again", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class LikePost extends AsyncTask<String, String, String> {
        String id, queId, task;
        ProgressDialog pd;
        ArrayList<UserProfileDetails> arrayList;

        public LikePost(ArrayList<UserProfileDetails> arrayList, String id, String queId, String task) {
            this.id = id;
            this.queId = queId;
            this.task = task;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/like_un_like/805/15
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "like_un_like/" + id + "/" + queId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Like Post..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject likeObject = jsonObject.getJSONObject("inserted_data");
                    details = new UserProfileDetails();
                    details.setQueLikeStatus(likeObject.getString("liked"));
                    Gson gson = new Gson();
                    WriteSharePrefrence(getActivity(), ALLQUESTION, gson.toJson(arrayList));
                    boolean isSucess = task.equalsIgnoreCase("add") ? false : true;
                    if (isSucess) {
                        details.setQueLikeStatus("0");
                        details.setQueLikeTotalCount(details.getQueLikeTotalCount() - 1);
                    } else {
                        details.setQueLikeStatus("1");
                        details.setQueLikeTotalCount(details.getQueLikeTotalCount() + 1);
                    }
                    arrayList.add(details);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                showLikeDisLike();
            } else {
                Toast.makeText(getActivity(), "try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 2/21/2017 get list of Question from URL
    private class GetQuetionList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String timing;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayFollowingIdList = new ArrayList<>();
            arrayFollowingNameList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/question_tag_data/752/181
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "question_tag_data/" + userId + "/" + ID);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Searched Question..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject questionObject = jsonObject.getJSONObject("data");
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
                    details.setQueCreatedTime(questionObject.getLong("created_time"));
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

                    // TODO: 3/25/2017 get like details
                    JSONObject answerObj = questionObject.getJSONObject("answers");
                    details.setQueVoteTotalCount(answerObj.getInt("total_count"));
                    details.setQueLikeStatus(answerObj.getString("liked"));
                    details.setQueLikeTotalCount(answerObj.getInt("likes_count"));

                    // TODO: 3/27/2017 get the list of following
                   /* JSONArray followingArray = questionObject.getJSONArray("following");
                    for (int i = 0; i < followingArray.length(); i++) {
                        JSONObject followingObject = followingArray.getJSONObject(i);
                        //details.setUserId(followingObject.getString("user_id"));
                        arrayFollowingIdList.add(followingObject.getString("follow_user_id"));
                        arrayFollowingNameList.add(followingObject.getString("fullname"));
                        details.setUserFollowingId(arrayFollowingIdList);
                        details.setUserFollowingName(arrayFollowingNameList);
                    }
*/
                    // TODO: 3/22/2017 get user details
                    JSONObject userObject = questionObject.getJSONObject("user");
                    details.setUserImage(userObject.getString("user_image"));
                    details.setUserUserName(userObject.getString("username"));
//                    // TODO: 3/22/2017 get comment details
                    /*JSONArray commentArray = questionObject.getJSONArray("comments");
                    if (commentArray.length() > 0) {
                        for (int c = 0; c < commentArray.length(); c++) {
                            JSONObject countcommentObject = commentArray.getJSONObject(c);
                            details.setQueComment(countcommentObject.getString("comment_text"));
                            details.setQueCommentId(countcommentObject.getString("id"));
                            details.setQueId(countcommentObject.getString("qid"));
                            details.setQueCommentUser(countcommentObject.getString("username"));
                                                            details.setQueCommentUserId(countcommentObject.getString("uid"));

                        }
                    }*/
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
                    //String timing = questionObject.getString("timing");
                    // details.setQueTiming(Long.valueOf(questionObject.getString("timing")));
                            /*try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH);
                                Date eventDate = (Date) dateFormat.parseObject(timing);
                                //Date event = dateFormat.parseObject(timing);
                                //Date currentDate = new Date();
                               // Log.d("get_date", "" + eventDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                    arrayUserList.add(details);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                openQuetionList();
            } else {
                Toast.makeText(getActivity(), "No Question Found", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
