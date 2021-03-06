package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.raviarchi.daberny.Activity.Activity.MultiSelectSpinner;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.CountDownTimerClass;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.koushikdutta.async.http.socketio.ExceptionCallback;
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


/*** Created by Ravi archi on 1/10/2017.
 */

public class Tag extends Fragment implements View.OnClickListener, MultiSelectSpinner.MultiSpinnerListener {
    public LinearLayoutManager layoutManager;
    public Toolbar toolBar;
    public Dialog dialog;
    public LinearLayout layoutFb, layoutTwitter, layoutList;
    public MultiSelectSpinner spinnnerFollowing;
    public TextView txtTitle;
    public RelativeLayout layoutHeader;
    public long time;
    public Utils utils;
    public UserProfileDetails details;
    @BindView(R.id.adapter_home_list_play_button)
    ImageButton imageButton;
    @BindView(R.id.adapter_home_list_btnshare)
    Button btnShare;
    @BindView(R.id.adapter_home_list_layout_all_voteresult)
    LinearLayout layoutAllVoteResult;
    @BindView(R.id.adapter_home_list_layoutCommentText)
    LinearLayout layoutCommentText;
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
    @BindView(R.id.adapter_home_list_txtvotesucess)
    TextView txtVoteSucess;
    @BindView(R.id.adapter_home_list_imgcomment)
    ImageView imgComment;
    @BindView(R.id.adapter_home_list_imgcommmentprofilepic)
    ImageView imgCommentUserProfilePic;
    @BindView(R.id.adapter_home_list_edcomment)
    EditText edCommentText;
    @BindView(R.id.adapter_home_list_txtque)
    TextView txtQuetion;
    @BindView(R.id.adapter_home_list_txttag)
    TextView txtQuetionTag;
    @BindView(R.id.adapter_home_list_txtcategory)
    TextView txtCategory;
    @BindView(R.id.adapter_home_list_txtdate)
    TextView txtPostDate;
    @BindView(R.id.adapter_home_list_txtvotepercentage1)
    TextView txtVotePercentage1;
    @BindView(R.id.adapter_home_list_txtvotepercentage2)
    TextView txtVotePercentage2;
    @BindView(R.id.adapter_home_list_txtvotepercentage3)
    TextView txtVotePercentage3;
    @BindView(R.id.adapter_home_list_txtvotepercentage4)
    TextView txtVotePercentage4;
    @BindView(R.id.adapter_home_list_txtvotequecount1)
    TextView txtVoteQueCount1;
    @BindView(R.id.adapter_home_list_txtvotequecount2)
    TextView txtVoteQueCount2;
    @BindView(R.id.adapter_home_list_txtvotequecount3)
    TextView txtVoteQueCount3;
    @BindView(R.id.adapter_home_list_txtvotequecount4)
    TextView txtVoteQueCount4;
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
    @BindView(R.id.adapter_home_list_txtoption1)
    TextView txtAnswer1;
    @BindView(R.id.adapter_home_list_txtoption2)
    TextView txtAnswer2;
    @BindView(R.id.adapter_home_list_txtoption3)
    TextView txtAnswer3;
    @BindView(R.id.adapter_home_list_txtoption4)
    TextView txtAnswer4;
    @BindView(R.id.adapter_home_list_imgquepic)
    ImageView imgQuestionPic;
    @BindView(R.id.adapter_home_list_imgprofilepic)
    ImageView imgProfilePic;
    @BindView(R.id.adapter_home_list_optionmenu)
    ImageView imgOptionMenu;
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
    @BindView(R.id.adapter_home_list_layoutvoteresult1)
    LinearLayout layoutVoteResult1;
    @BindView(R.id.adapter_home_list_layoutvoteresult2)
    LinearLayout layoutVoteResult2;
    @BindView(R.id.adapter_home_list_layoutvoteresult3)
    LinearLayout layoutVoteResult3;
    @BindView(R.id.adapter_home_list_layoutvoteresult4)
    LinearLayout layoutVoteResult4;
    @BindView(R.id.adapter_home_list_media)
    LinearLayout layoutMedia;
    @BindView(R.id.adapter_home_list_layoutinterest1)
    LinearLayout layoutInterest1;
    @BindView(R.id.adapter_home_list_layoutinterest2)
    LinearLayout layoutInterest2;
    @BindView(R.id.adapter_home_list_layoutinterest3)
    LinearLayout layoutInterest3;
    @BindView(R.id.adapter_home_list_radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.adapter_home_list_beforecounter)
    LinearLayout layoutCounter;
    @BindView(R.id.adapter_home_list_progressbar1)
    RoundCornerProgressBar optionProgress1;
    @BindView(R.id.adapter_home_list_progressbar2)
    RoundCornerProgressBar optionProgress2;
    @BindView(R.id.adapter_home_list_progressbar3)
    RoundCornerProgressBar optionProgress3;
    @BindView(R.id.adapter_home_list_progressbar4)
    RoundCornerProgressBar optionProgress4;
    private ArrayList<UserProfileDetails> arrayUserList;
    private ArrayList<String> arrayFollowingNameList, arrayFollowingIdList, arrayInterestList,
            arrayStartNameList, arrayEndNameList;
    private String loginUserId, questionId, Answer, followingPeopleName, followingPeopleId, commentText;
    private CountDownTimerClass timer;
    private int seconds, minutes, hours;
    private Long remainTime, remainTimeMiliSeconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        ButterKnife.bind(this, view);
        utils = new Utils(getActivity());
        init();
        findViewId(view);
        return view;
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        layoutHeader = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        layoutHeader.setVisibility(View.VISIBLE);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.tag);
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        loginUserId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        if (getArguments() != null) {
            Gson gson = new Gson();
            String strObj = getArguments().getString("userprofiledetails");
            UserProfileDetails userdetails = gson.fromJson(strObj, UserProfileDetails.class);
            questionId = userdetails.getQueId();
            Utils.WriteSharePrefrence(getActivity(), Constant.QUESTION_ID, userdetails.getQueId());
            new GetQuetionList(loginUserId, userdetails.getQueId()).execute();
        }
    }

    private void openQuetionList() {
        // TODO: 6/16/2017 set the question details
        loginUserId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        remainTime = details.getQueRemainTime();
        remainTimeMiliSeconds = details.getQueRemainTimeMiliSeconds();

        // TODO: 6/6/2017 time handler
        timer = new CountDownTimerClass(remainTimeMiliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                seconds = (int) (millisUntilFinished / 1000) % 60;
                txtHour.setText("" + String.format("%2d", hours));
                txtMinute.setText("" + String.format("%2d", minutes));
                txtSecond.setText("" + String.format("%2d", seconds));
                layoutLike.setVisibility(View.INVISIBLE);
                layoutComment.setVisibility(View.GONE);
                layoutAllVoteResult.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);
                layoutCounter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                layoutCounter.setVisibility(View.GONE);
                layoutVote.setVisibility(View.INVISIBLE);
                layoutLike.setVisibility(View.VISIBLE);
                layoutComment.setVisibility(View.VISIBLE);
                layoutAllVoteResult.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);
            }
        }.start();

        // TODO: 16/6/2017 **************set the Visibility**********************
        imgOptionMenu.setVisibility(View.INVISIBLE);
        rdAnswer3.setVisibility(View.VISIBLE);
        rdAnswer4.setVisibility(View.VISIBLE);
        txtVoteSucess.setVisibility(View.GONE);

        if (details.getQueVoteStatus().equalsIgnoreCase("1")) {
            if (details.getUserCanVote().equalsIgnoreCase("1")) {
                txtVote.setVisibility(View.VISIBLE);
            } else {
                txtVote.setVisibility(View.INVISIBLE);
                utils.setRadioButtonAsPerVote(rdAnswer1, rdAnswer2, rdAnswer3, rdAnswer4);

                if (details.getUserId().equalsIgnoreCase(loginUserId)) {
                    txtVoteSucess.setVisibility(View.GONE);
                } else {
                    txtVoteSucess.setVisibility(View.VISIBLE);
                }
            }
        } else {
            txtVote.setVisibility(View.INVISIBLE);
            txtVoteSucess.setVisibility(View.GONE);
            utils.setRadioButtonAsPerVote(rdAnswer1, rdAnswer2, rdAnswer3, rdAnswer4);
        }

        if (details.getQueComment() != null) {
            layoutCommentText.setVisibility(View.VISIBLE);
        } else {
            layoutCommentText.setVisibility(View.GONE);
        }
        // TODO: 16/6/2017 ********************** End ************************

        // TODO: 16/6/2017 **************set the value in TextView**********************
        txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
        txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
        txtUserName.setText(details.getUserUserName());
        txtCategory.setText(details.getQueCategory());
        txtPostDate.setText(details.getQuePostDate());
        txtQuetion.setText(details.getQueTitle());
        if (!details.getQueTag().equalsIgnoreCase("")) {
            txtQuetionTag.setText(details.getQueTag());
        } else {
            txtQuetionTag.setVisibility(View.GONE);
        }
        if (details.getQueComment() != null) {
            txtCommentText.setText(details.getQueComment().replaceAll("%20", " "));
            txtCommentUser.setText(details.getQueCommentUser());
        }
        rdAnswer1.setText(details.getQueOptionFirst());
        rdAnswer2.setText(details.getQueOptionSecond());
        txtAnswer1.setText(details.getQueOptionFirst());
        txtAnswer2.setText(details.getQueOptionSecond());
        txtVotePercentage1.setText("Vote:" + details.getQueVotePercentage1() + "%");
        txtVoteQueCount1.setText("Vote Count:" + details.getQueVoteCount1());
        txtVotePercentage2.setText("Vote:" + details.getQueVotePercentage2() + "%");
        txtVoteQueCount2.setText("Vote Count:" + details.getQueVoteCount2());
        optionProgress1.setProgress(Float.parseFloat(details.getQueVotePercentage1()));
        optionProgress2.setProgress(Float.parseFloat(details.getQueVotePercentage2()));

        // TODO: 5/26/2017 set the radio button & progressbar as per option
        if (!details.getQueOptionThird().equalsIgnoreCase("")) {
            rdAnswer3.setText(details.getQueOptionThird());
            txtAnswer3.setText(details.getQueOptionThird());
            txtVotePercentage3.setText("Vote:" + details.getQueVotePercentage3() + "%");
            txtVoteQueCount3.setText("Vote Count:" + details.getQueVoteCount3());
            optionProgress3.setProgress(Float.parseFloat(details.getQueVotePercentage3()));
        } else {
            rdAnswer3.setVisibility(View.GONE);
            layoutVoteResult3.setVisibility(View.GONE);
        }
        if (!details.getQueOptionFourth().equalsIgnoreCase("")) {
            rdAnswer4.setText(details.getQueOptionFourth());
            txtAnswer4.setText(details.getQueOptionFourth());
            txtVotePercentage4.setText("Vote:" + details.getQueVotePercentage4() + "%");
            txtVoteQueCount4.setText("Vote Count:" + details.getQueVoteCount4());
            optionProgress4.setProgress(Float.parseFloat(details.getQueVotePercentage4()));

        } else {
            rdAnswer4.setVisibility(View.GONE);
            layoutVoteResult4.setVisibility(View.GONE);
        }

        if (details.getIntName().size() > 0) {
            txtInterest1.setText(details.getIntName().get(0));
            txtLevel1.setText(details.getStartRankName().get(0));
        } else {
            layoutInterest1.setVisibility(View.GONE);
        }
        if (details.getIntName().size() > 1) {
            txtInterest2.setText(details.getIntName().get(1));
            txtLevel2.setText(details.getStartRankName().get(1));
        } else {
            layoutInterest2.setVisibility(View.GONE);
        }
        if (details.getIntName().size() > 2) {
            txtInterest3.setText(details.getIntName().get(2));
            txtLevel3.setText(details.getStartRankName().get(2));
        } else {
            layoutInterest3.setVisibility(View.GONE);
        }

        // TODO: 16/6/2017 ********************** End ************************

// TODO: 16/6/2017 **************set defalut icon status**********************
        // TODO: 3/28/2017 set like icon as per its selection
        if (details.getQueLikeStatus().equalsIgnoreCase("0")) {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_unlike_icon));
        } else {
            imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_like_icon));
        }
        // TODO: 16/6/2017 ************** End **********************

        // TODO: 16/6/2017 **************set the value in ImageView & VideoView*******************
        // TODO: 2/23/2017 set user profile image of comment user
        if (details.getQueCommentUserProfilePic() != null) {
            if (details.getQueCommentUserProfilePic().length() > 0) {
                Picasso.with(getActivity()).load(details.getQueCommentUserProfilePic()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgCommentUserProfilePic);
            }

        } else {
            Picasso.with(getActivity()).load(R.drawable.ic_placeholder).
                    transform(new RoundedTransformation(120, 2)).
                    placeholder(R.drawable.ic_placeholder).into(imgCommentUserProfilePic);
        }
        // TODO: 2/23/2017 set user profile image of post user
        if (details.getUserImage() != null) {
            if (details.getUserImage().length() > 0) {
                Picasso.with(getActivity()).load(details.getUserImage()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
            }
        } else {
            Picasso.with(getActivity()).load(R.mipmap.ic_launcher).
                    transform(new RoundedTransformation(120, 2)).
                    placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
        }
        // TODO: 2/23/2017 set image & video dynamically
        if (details.getQueImageName().length() > 0) {
            if (details.getQueImage() != null) {
                imgQuestionPic.setVisibility(View.VISIBLE);
                vdProfile.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                layoutMedia.setVisibility(View.VISIBLE);
                if (details.getQueType().equalsIgnoreCase("0")) {
                    layoutMedia.setVisibility(View.GONE);
                } else if (details.getQueType().equalsIgnoreCase("1")) {
                    vdProfile.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(details.getQueImage()).placeholder(R.drawable.ic_placeholder).into(imgQuestionPic);
                } else if (details.getQueType().equalsIgnoreCase("2")) {
                    imgQuestionPic.setVisibility(View.GONE);
                    vdProfile.setVideoURI(Uri.parse(details.getQueImage()));
                    vdProfile.setMediaController(new MediaController(getActivity()));
                    vdProfile.setMediaController(null);
                    vdProfile.requestFocus();
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (vdProfile.isPlaying()) {
                                vdProfile.pause();
                            } else {
                                vdProfile.start();
                                new MediaController(getActivity()).show();
                                imageButton.setVisibility(View.GONE);
                                vdProfile.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        try {
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.stop();
                                                mediaPlayer.release();
                                                mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(details.getQueImage()));
                                                Log.d("AUDIO", "@Enter----");
                                            }
                                            mediaPlayer.start();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.d("AUDIOExp", "@" + e);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        } else {
            layoutMedia.setVisibility(View.GONE);
        }
        //TODO: 16/6/2017 ********************** End ************************

        // TODO: 16/6/2017 **************set the click event**********************

        // TODO: 3/28/2017 redirect to show all comments
        txtViewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CommentPeopleDetail();
                utils.setIdOfQuestion(details, fragment);
                utils.replaceFragment(fragment);
            }
        });
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/27/2017 store the comment
                commentText = edCommentText.getText().toString().replaceAll(" ", "%20");
                if (commentText.length() > 0) {
                    new CommentPost(arrayUserList, loginUserId, Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID), commentText).execute();
                }
            }
        });

        // TODO: 3/30/2017 peform click to like and unlike the post
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String liketask = " ";
                String isLiked = details.getQueLikeStatus();
                int likeTotalCount = details.getQueLikeTotalCount();
                if (isLiked != null && isLiked.equalsIgnoreCase("0")) {
                    details.setQueLikeStatus("1");
                    details.setQueLikeTotalCount(likeTotalCount + 1);
                } else {
                    details.setQueLikeStatus("0");
                    details.setQueLikeTotalCount(likeTotalCount - 1);
                }
                liketask = (isLiked != null && isLiked.equalsIgnoreCase("0")) ? "add" : "remove";
                new LikePost(loginUserId, Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID), liketask).execute();
            }
        });

        txtVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Answer = Utils.ReadSharePrefrence(getActivity(), Constant.ANSWER);
                String isVoteStatus = details.getQueVoteStatus();
                String isCanVote = details.getUserCanVote();
                if (isVoteStatus.equalsIgnoreCase("1")) {
                    if (isCanVote.equalsIgnoreCase("1")) {
                        details.setUserCanVote("0");
                        details.setQueVoteTotalCount(details.getQueVoteTotalCount() + 1);
                        txtVote.setVisibility(View.GONE);
                    }
                }
                new SubmitVote(loginUserId, Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID), Answer, arrayUserList).execute();
            }
        });

        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfPostUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfPostUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        txtCommentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfCommentUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        // TODO: 3/24/2017 vote status
        rdAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(getActivity(), Constant.ANSWER, "1");
            }
        });
        rdAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(getActivity(), Constant.ANSWER, "2");
            }
        });
        rdAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(getActivity(), Constant.ANSWER, "3");
            }
        });
        rdAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(getActivity(), Constant.ANSWER, "4");
            }
        });

        // TODO: 6/9/2017 share the post with friends
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForShare();
            }
        });
// TODO: 16/6/2017 ********************** End ************************

    }

    private void openDialogForShare() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_share_option);
        layoutFb = (LinearLayout) dialog.findViewById(R.id.dialog_share_option_layoutfb);
        layoutTwitter = (LinearLayout) dialog.findViewById(R.id.dialog_share_option_layouttwitter);
        layoutList = (LinearLayout) dialog.findViewById(R.id.dialog_share_option_layoutshare);
        spinnnerFollowing = (MultiSelectSpinner) dialog.findViewById(R.id.dialog_share_option_spinnerFollowing);
        dialog.show();
        layoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Daberny");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.raviarchi\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, "https://i.diawi.com/UBMuRn");
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                dialog.dismiss();
            }
        });

        layoutTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setClassName("com.twitter.android", "com.twitter.android.LoginActivity");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Daberny");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.raviarchi\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, "https://i.diawi.com/UBMuRn");
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                dialog.dismiss();
            }
        });
        layoutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnnerFollowing.performClick();
                dialog.dismiss();
            }
        });
        // TODO: 6/7/2017 get list of following people
        spinnnerFollowing.setItems(arrayFollowingNameList, "Following", this);
    }

    @Override
    public void onClick(View v) {
        PackageManager pm = getActivity().getPackageManager();
        switch (v.getId()) {
            case R.id.adapter_home_list_layoutfb:
                /*Intent ifeb = new Intent("android.intent.category.LAUNCHER");
                ifeb.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
                startActivity(ifeb);*/
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Daberny");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.raviarchi\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, "https://i.diawi.com/UBMuRn");
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;

            case R.id.adapter_home_list_layouttwitter:
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.twitter.android", "com.twitter.android.LoginActivity");
                startActivity(intent);*/
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    //i.setType("text/plain");
                    i.setClassName("com.twitter.android", "com.twitter.android.LoginActivity");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Daberny");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.raviarchi\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, "https://i.diawi.com/UBMuRn");
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        ArrayList<String> newGetId = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                newGetId.add(arrayFollowingIdList.get(i));
            }
            followingPeopleId = "";
            for (int j = 0; j < newGetId.size(); j++) {
                if (followingPeopleId.length() > 0) {
                    followingPeopleId = followingPeopleId + "," + newGetId.get(j);
                } else {
                    followingPeopleId = newGetId.get(j);
                }
            }
        }
        if (followingPeopleId != null && Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID) != null) {
            Log.d("QUEID", "@@" + Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID));
            Log.d("friends", "@@" + followingPeopleId);
            if (followingPeopleId.trim().length() > 0) {
                new SharePost(loginUserId, followingPeopleId,
                        Utils.ReadSharePrefrence(getActivity(), Constant.QUESTION_ID), arrayUserList).execute();
            } else {
                Toast.makeText(getActivity(), "Please select user to share.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 3/23/2017 submit vote
    private class SubmitVote extends AsyncTask<String, String, String> {
        String id, queId, answer;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;


        private SubmitVote(String id, String queId, String answer, ArrayList<UserProfileDetails> arrayList) {
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
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "qustion_wotting/" + id + "/" + queId + "/" + answer);
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
                    arrayUserList.add(details);
                    //Toast.makeText(getActivity(), "Success! Thanks for vote..", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                txtVote.setVisibility(View.GONE);
            } else {
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("Exp", "" + e);
                    }
                };
            }
        }

    }

    private class SharePost extends AsyncTask<String, String, String> {
        String id, que_id, otheruser_id, loginuser_id;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;

        private SharePost(String userid, String otheruserId, String queId, ArrayList<UserProfileDetails> arrayList) {
            this.loginuser_id = userid;
            this.otheruser_id = otheruserId;
            this.que_id = queId;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/question_share_data/752/669,885/709
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "question_share_data/" + loginuser_id + "/" + otheruser_id + "/" + que_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Share Post..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject voteObject = jsonObject.getJSONObject("data");
                    Toast.makeText(getActivity(), "Success! Post shared to selected users", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // TODO: 3/28/2017  like and dislike the post
    private class LikePost extends AsyncTask<String, String, String> {
        String user_id, que_id, task;
        ProgressDialog pd;

        private LikePost(String id, String queId, String task) {
            this.user_id = id;
            this.que_id = queId;
            this.task = task;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/like_un_like/805/15
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "like_un_like/" + user_id + "/" + que_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Like Post..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = jsonObject.getJSONObject("inserted_data");
                    details = new UserProfileDetails();
                    details.setQueLikeStatus(userObject.getString("liked"));
                    details.setQueLikeTotalCount(userObject.getInt("likes_count"));
                    boolean isSucess = task.equalsIgnoreCase("add") ? false : true;
                    if (isSucess) {
                        details.setQueLikeStatus("0");
                    } else {
                        details.setQueLikeStatus("1");
                    }
                    arrayUserList.add(details);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                if (details.getQueLikeStatus().equalsIgnoreCase("0")) {
                    imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_unlike_icon));
                    txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
                } else {
                    imgLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_like_icon));
                    txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
                }
            } else {
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("exp", "" + e);
                    }
                };
            }
        }
    }

    // TODO: 3/27/2017 store the comments
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
                        arrayUserList.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                edCommentText.setText(" ");
                layoutCommentText.setVisibility(View.VISIBLE);
                txtCommentText.setText(details.getQueComment().replaceAll("%20", " "));
                txtCommentUser.setText(details.getQueCommentUser());
                Picasso.with(getActivity()).load(details.getQueCommentUserProfilePic())
                        .transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder)
                        .into(imgCommentUserProfilePic);
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

    // TODO: 6/16/2017 get list of Question from URL
    private class GetQuetionList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String timing, remainTime, user_id, que_id;

        private GetQuetionList(String userId, String queId) {
            this.user_id = userId;
            this.que_id = queId;
        }

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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/question_tag_data/752/181
            String response = Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "question_tag_data/" + user_id + "/" + que_id);
            Log.d("RESPONSE", "Tag Question..." + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject questionObject = jsonObject.getJSONObject("data");
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
                        arrayFollowingNameList.add(followingObject.getString("username"));
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
            }
        }
    }
}
