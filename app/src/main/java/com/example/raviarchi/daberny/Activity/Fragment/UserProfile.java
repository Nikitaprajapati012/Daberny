package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.raviarchi.daberny.Activity.Activity.AskQuestionActivity;
import com.example.raviarchi.daberny.Activity.Activity.LoginActivity;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi archi on 1/10/2017.
 */

public class UserProfile extends Fragment implements View.OnClickListener {
    public Utils utils;
    public UserProfileDetails details;
    public Dialog dialog;
    public Button btnAskQue, btnUserProfile, btnLogout;
    public String ID, Interest;
    public ArrayList<String> arrayInterestList, arrayInterestIdList, interestList,
    arrayInterestStartNameList,arrayInterestStartPointsList,
            arrayInterestEndNameList,arrayInterestEndPointsList,
            arrayInterestTotalPointsList,arrayInterestUserPointsList,arrayInterestUserPercentageList;
    public ArrayList<UserProfileDetails> arrayList;
    public Toolbar toolBar;
    public TextView txtTitle;
    /*@BindView(R.id.fragment_user_profile_imgBrowse)
    ImageView imgBrowse;*/
    @BindView(R.id.fragment_user_profile_imgphoto)
    ImageView imgProfilePic;
    @BindView(R.id.fragment_user_profile_txtlevelstartpoint1)
    TextView txtInterestChartStartLevelPoints1;
    @BindView(R.id.fragment_user_profile_txtlevelstartpoint2)
    TextView txtInterestChartStartLevelPoints2;
    @BindView(R.id.fragment_user_profile_txtlevelstartpoint3)
    TextView txtInterestChartStartLevelPoints3;
    @BindView(R.id.fragment_user_profile_txtlevelendpoints1)
    TextView txtInterestChartEndLevelPoints1;
    @BindView(R.id.fragment_user_profile_txtlevelendpoints2)
    TextView txtInterestChartEndLevelPoints2;
    @BindView(R.id.fragment_user_profile_txtlevelendpoints3)
    TextView txtInterestChartEndLevelPoints3;
    @BindView(R.id.fragment_user_profile_txtlevelstart1)
    TextView txtInterestChartStartLevel1;
    @BindView(R.id.fragment_user_profile_txtlevelstart2)
    TextView txtInterestChartStartLevel2;
    @BindView(R.id.fragment_user_profile_txtlevelstart3)
    TextView txtInterestChartStartLevel3;
    @BindView(R.id.fragment_user_profile_txtlevelend1)
    TextView txtInterestChartEndLevel1;
    @BindView(R.id.fragment_user_profile_txtlevelend2)
    TextView txtInterestChartEndLevel2;
    @BindView(R.id.fragment_user_profile_txtlevelend3)
    TextView txtInterestChartEndLevel3;
    @BindView(R.id.fragment_user_profile_txtposts)
    TextView txtPosts;
    @BindView(R.id.fragment_user_profile_txtfollowers)
    TextView txtFollowers;
    @BindView(R.id.fragment_user_profile_txtfollowing)
    TextView txtFollowing;
    @BindView(R.id.fragment_user_profile_txteditprofile)
    TextView txtEditProfile;
    @BindView(R.id.fragment_user_profile_txtinterest1)
    TextView txtInterest1;
    @BindView(R.id.fragment_user_profile_txtinterestchart1)
    TextView txtInterestChart1;
    @BindView(R.id.fragment_user_profile_txtuserpointlevel1)
    TextView txtInterestChartUserPoints1;
    @BindView(R.id.fragment_user_profile_txtuserpointlevel2)
    TextView txtInterestChartUserPoints2;
    @BindView(R.id.fragment_user_profile_txtuserpointlevel3)
    TextView txtInterestChartUserPoints3;
    @BindView(R.id.fragment_user_profile_txtinterestchart2)
    TextView txtInterestChart2;
    @BindView(R.id.fragment_user_profile_txtinterestchart3)
    TextView txtInterestChart3;
    @BindView(R.id.fragment_user_profile_txtusername)
    TextView txtUserName;
    @BindView(R.id.fragment_user_profile_layoutposts)
    LinearLayout layoutPosts;
    @BindView(R.id.fragment_user_profile_layoutfollowers)
    LinearLayout layoutFollowers;
    @BindView(R.id.fragment_user_profile_layoutfollowing)
    LinearLayout layoutFollowing;
    @BindView(R.id.fragment_user_profile_layout_interest_third)
    LinearLayout layoutInterestChart3;
    @BindView(R.id.fragment_user_profile_layout_interest_second)
    LinearLayout layoutInterestChart2;
    @BindView(R.id.fragment_user_profile_layout_interest_first)
    LinearLayout layoutInterestChart1;
    @BindView(R.id.fragment_user_profile_progressbar1)
    RoundCornerProgressBar progressbar1;
    @BindView(R.id.fragment_user_profile_progressbar2)
    RoundCornerProgressBar progressbar2;
    @BindView(R.id.fragment_user_profile_progressbar3)
    RoundCornerProgressBar progressbar3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.profile);
        init();
        new GetUserProfileDetails().execute();
        click();
        return view;
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        ID = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        utils = new Utils(getActivity());
        arrayList = new ArrayList<>();
    }

    private void click() {
        txtEditProfile.setOnClickListener(this);
        //imgBrowse.setOnClickListener(this);
        layoutFollowers.setOnClickListener(this);
        layoutFollowing.setOnClickListener(this);
        layoutPosts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString("userprofiledetails", gson.toJson(details));

        switch (v.getId()) {
            case R.id.fragment_user_profile_txteditprofile:
                // TODO: 2/24/2017 goes to editprofile screen
                Utils.WriteSharePrefrence(getActivity(), Constant.USER_INTERESTID, Interest);
                fragment = new EditUserProfile();
                break;

           /* case R.id.fragment_user_profile_imgBrowse:
                openDialog();
                break;*/

            case R.id.fragment_user_profile_layoutfollowers:
                // TODO: 2/24/2017 goes to follower people screen
                fragment = new Follower();
                break;

            case R.id.fragment_user_profile_layoutfollowing:
                // TODO: 2/24/2017 goes to following people screen
                fragment = new Following();
                break;

            case R.id.fragment_user_profile_layoutposts:
                // TODO: 2/24/2017 goes to posts of particular person screen
                fragment = new Posts();
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_contain_layout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    // TODO: 2/24/2017 open dialog
    private void openDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_option);
        dialog.setTitle("");
        btnAskQue = (Button) dialog.findViewById(R.id.dialog_askque);
        btnUserProfile = (Button) dialog.findViewById(R.id.dialog_userprofile);
        btnLogout = (Button) dialog.findViewById(R.id.dialog_logout);
        dialog.show();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ilogout = new Intent(getActivity(), LoginActivity.class);
                ilogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Utils.ClearSharePref(getActivity(),Constant.USERID);
                startActivity(ilogout);
                dialog.dismiss();
            }
        });

        btnAskQue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iask = new Intent(getActivity(), AskQuestionActivity.class);
                iask.putExtra("id", Utils.ReadSharePrefrence(getActivity(), Constant.USERID));
                startActivity(iask);
                dialog.dismiss();
            }
        });

        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openUserProfileDetailsList() {
        // TODO: 2/27/2017 user details
        txtUserName.setText(details.getUserUserName());
        txtPosts.setText(details.getUserPosts());
        txtFollowers.setText(details.getUserFollowers());
        txtFollowing.setText(details.getUserFollowing());

        // TODO: 5/19/2017 set first progress
        progressbar1.setProgress(Float.parseFloat(details.getUserRankPercentage().get(0)));
        progressbar1.setBackgroundColor(getResources().getColor(R.color.progress));
        progressbar1.setProgressColor(R.color.signinbg);

        // TODO: 5/19/2017 set second progress
        progressbar2.setProgress(Float.parseFloat(details.getUserRankPercentage().get(1)));
        progressbar2.setProgressColor(R.color.signinbg);

        // TODO: 5/19/2017 set third progress
        progressbar3.setProgress(Float.parseFloat(details.getUserRankPercentage().get(2)));
        progressbar3.setProgressColor(R.color.signinbg);

        //TODO: 3/15/2017 get interest at this screen
        interestList = new ArrayList<>();
        if (arrayInterestList.size() > 0) {
            for (int i = 0; i < arrayInterestList.size(); i++) {
                interestList.add(arrayInterestList.get(i));
            }
        }
        Interest = "";
        if (interestList.size() > 0) {
            for (int i = 0; i < interestList.size(); i++) {
                if (Interest.length() > 0) {
                    Interest = Interest + " / " + interestList.get(i);
                } else {
                    Interest = interestList.get(i);
                }
            }
        }

// TODO: 3/9/2017 set interest in one text
        if (Interest.length() > 0) {
            txtInterest1.setText(Interest);
        } else {
            txtInterest1.setText("");
        }

        // TODO: 3/15/2017 for first interest
        if (Interest.split("/").length > 0) {
            String firstInterest = Interest.split("/")[0];
            txtInterestChart1.setText(firstInterest);
            txtInterestChartStartLevel1.setText(""+details.getStartRankName().get(0));
            txtInterestChartStartLevel2.setText(""+details.getStartRankName().get(0));
            txtInterestChartStartLevel3.setText(""+details.getStartRankName().get(0));
            txtInterestChartEndLevel1.setText(""+details.getStartRankName().get(0));
            txtInterestChartEndLevel2.setText(""+details.getStartRankName().get(0));
            txtInterestChartEndLevel3.setText(""+details.getStartRankName().get(0));
            txtInterestChartUserPoints1.setText(""+details.getUserRankPoints().get(0) + " Points");
        } else {
            txtInterestChart1.setText(" ");
            layoutInterestChart1.setVisibility(View.GONE);
        }

        // TODO: 3/15/2017 for second interest
        if (Interest.split("/").length > 1) {
            String secondInterest = Interest.split("/")[1];
            txtInterestChart2.setText(secondInterest);
            txtInterestChartStartLevel1.setText(""+details.getStartRankName().get(1));
            txtInterestChartStartLevel2.setText(""+details.getStartRankName().get(1));
            txtInterestChartStartLevel3.setText(""+details.getStartRankName().get(1));
            txtInterestChartEndLevel1.setText(""+details.getStartRankName().get(1));
            txtInterestChartEndLevel2.setText(""+details.getStartRankName().get(1));
            txtInterestChartEndLevel3.setText(""+details.getStartRankName().get(1));
            txtInterestChartUserPoints2.setText(""+details.getUserRankPoints().get(1)+ " Points");
        } else {
            txtInterestChart2.setText(" ");
            layoutInterestChart2.setVisibility(View.GONE);
        }

        // TODO: 3/15/2017 for third interest
        if (Interest.split("/").length > 2) {
            String thirdInterest = Interest.split("/")[2];
            txtInterestChart3.setText(thirdInterest );
            txtInterestChartStartLevel1.setText(""+details.getStartRankName().get(2));
            txtInterestChartStartLevel2.setText(""+details.getStartRankName().get(2));
            txtInterestChartStartLevel3.setText(""+details.getStartRankName().get(2));
            txtInterestChartEndLevel1.setText(""+details.getStartRankName().get(2));
            txtInterestChartEndLevel2.setText(""+details.getStartRankName().get(2));
            txtInterestChartEndLevel3.setText(""+details.getStartRankName().get(2));
            txtInterestChartUserPoints3.setText(""+details.getUserRankPoints().get(2)+ " Points");

        } else {
            txtInterestChart3.setText(" ");
            layoutInterestChart3.setVisibility(View.GONE);
        }

        // TODO: 3/9/2017 set user profile pic
        if (details.getUserImage().length() > 0) {
            Picasso.with(getActivity()).load(details.getUserImage())
                    .transform(new RoundedTransformation(120, 2))
                    .placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
        } else {
            Picasso.with(getActivity()).load(R.drawable.ic_placeholder)
            .transform(new RoundedTransformation(120, 2))
                    .into(imgProfilePic);
        }
    }

    private class GetUserProfileDetails extends AsyncTask<String, String, String> {
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/profile/677/669
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "profile/" + Utils.ReadSharePrefrence(getActivity(), Constant.USERID) + "/" + ID);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "User Details..." + s);
            pd.dismiss();
            try {
                //user profile
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = jsonObject.getJSONObject("data");
                    details = new UserProfileDetails();
                    arrayInterestList = new ArrayList<>();
                    arrayInterestIdList = new ArrayList<>();
                    arrayInterestStartNameList = new ArrayList<>();
                    arrayInterestStartPointsList = new ArrayList<>();
                    arrayInterestEndNameList = new ArrayList<>();
                    arrayInterestEndPointsList = new ArrayList<>();
                    arrayInterestTotalPointsList = new ArrayList<>();
                    arrayInterestUserPointsList = new ArrayList<>();
                    arrayInterestUserPercentageList = new ArrayList<>();
                    details.setUserId(userObject.getString("user_id"));
                    details.setUserFullName(userObject.getString("fullname"));
                    details.setUserUserName(userObject.getString("username"));
                    details.setUserEmail(userObject.getString("email"));
                    details.setUserPosts(userObject.getString("post"));
                    details.setUserFollowers(userObject.getString("followers"));
                    details.setUserFollowing(userObject.getString("following"));
                    details.setUserImage(userObject.getString("image"));
                    details.setUserCountryId(userObject.getString("country_id"));
                    details.setUserCountryName(userObject.getString("country_name"));
                    //user interest array
                    JSONArray userArray = userObject.getJSONArray("user_interests");
                    if (userArray.length() > 0) {
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayInterestList.add(interestObject.getString("name"));
                        arrayInterestIdList.add(interestObject.getString("id"));
                        arrayInterestStartNameList.add(interestObject.getString("start_name"));
                        arrayInterestStartPointsList.add(interestObject.getString("start_point"));
                        arrayInterestEndNameList.add(interestObject.getString("end_name"));
                        arrayInterestEndPointsList.add(interestObject.getString("end_point"));
                        arrayInterestTotalPointsList.add(interestObject.getString("total_point"));
                        arrayInterestUserPointsList.add(interestObject.getString("user_point"));
                        arrayInterestUserPercentageList.add(interestObject.getString("percentage_count"));
                        details.setStartRankName(arrayInterestStartNameList);
                        details.setStartRankPoints(arrayInterestStartPointsList);
                        details.setEndRankName(arrayInterestEndNameList);
                        details.setEndRankPoints(arrayInterestEndPointsList);
                        details.setTotalRankPoints(arrayInterestTotalPointsList);
                        details.setUserRankPoints(arrayInterestUserPointsList);
                        details.setUserRankPercentage(arrayInterestUserPercentageList);
                        details.setUserRankPercentage(arrayInterestUserPercentageList);
                    }
                }
                    arrayList.add(details);
                }
                if (arrayList.size() > 0) {
                    openUserProfileDetailsList();

                } else {
                    Toast.makeText(getActivity(), "No User Details Found, Please Try Again.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

