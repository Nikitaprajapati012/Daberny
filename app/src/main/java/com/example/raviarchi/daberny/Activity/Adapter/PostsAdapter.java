package com.example.raviarchi.daberny.Activity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.raviarchi.daberny.Activity.Activity.MultiSelectSpinner;
import com.example.raviarchi.daberny.Activity.Fragment.CommentPeopleDetail;
import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.CountDownTimerClass;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiplespinner.MultiSelectionSpinner;
import com.koushikdutta.async.http.socketio.ExceptionCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*** Created by Ravi archi on 2/21/2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> implements View.OnClickListener, MultiSelectSpinner.MultiSpinnerListener {
    public long time;
    public Utils utils;
    public UserProfileDetails details;
    private ArrayList<String> arrayfollowingUserList;
    private ArrayList<String> arrayfollowingUserIdList;
    private String loginUserId, queId, Answer, followingPeopleName, followingPeopleId, commentText;
    private CountDownTimerClass timer;
    private ArrayList<UserProfileDetails> arrayList;
    private Context context;
    private int seconds, minutes, hours;
    private Handler handler;
    private Long remainTime;
    private boolean mCancelled = false;

    public PostsAdapter(Context context, ArrayList<UserProfileDetails> arraylist, ArrayList<String> arrayFollowingNameList, ArrayList<String> arrayFollowingIdList) {
        this.context = context;
        this.arrayList = arraylist;
        this.arrayfollowingUserList = arrayFollowingNameList;
        this.arrayfollowingUserIdList = arrayFollowingIdList;
        this.utils = new Utils(context);
        notifyDataSetChanged();
        Log.d("Length", "" + arrayList.size());
        Log.d("Lengthfriendsid", "" + arrayfollowingUserIdList.size());
        Log.d("Lengthfriendsname", "" + arrayfollowingUserList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserProfileDetails details = arrayList.get(position);
        loginUserId = Utils.ReadSharePrefrence(context, Constant.USERID);
        String isRemainTime = Utils.ReadSharePrefrence(context, Constant.REMAINTIME);
        queId = details.getQueId();
        Long time;
        if (isRemainTime.length() > 0) {
            remainTime = arrayList.get(position).getQueRemainTime();
            if (remainTime != null) {
                Utils.WriteSharePrefrence(context, Constant.TIME, String.valueOf(remainTime));
            } else {
                time = 0000000000L;
                remainTime = time;
            }
        }


        // TODO: 6/6/2017 time handler
        timer = new CountDownTimerClass(remainTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               /* long years = millisUntilFinished / (365 * 60 * 60 * 24);
                long months = (millisUntilFinished - years * 365 * 60 * 60 * 24) / (30 * 60 * 60 * 24);
                long days = (millisUntilFinished - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24) / (60 * 60 * 24);
                long hours = (millisUntilFinished - years * 365 * 60 * 60 * 24
                        - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24) / (60 * 60);
                long minutes = (millisUntilFinished - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60) / 60;
                long seconds = (millisUntilFinished - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60);
                Log.d("TIMER", "--Y-"+years +"-M-"+ months +"-D-"+days
                        +"-H-"+hours+"-MI-"+minutes+"-S-"+seconds);*/
                /////////////////////////////
                hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                seconds = (int) (millisUntilFinished / 1000) % 60;
                holder.txtHour.setText("" + String.format("%2d", hours));
                holder.txtMinute.setText("" + String.format("%2d", minutes));
                holder.txtSecond.setText("" + String.format("%2d", seconds));
                holder.layoutLike.setVisibility(View.INVISIBLE);
                holder.layoutComment.setVisibility(View.GONE);
                holder.layoutAllVoteResult.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.VISIBLE);
                holder.layoutCounter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                holder.layoutCounter.setVisibility(View.GONE);
                holder.layoutVote.setVisibility(View.INVISIBLE);
                holder.layoutLike.setVisibility(View.VISIBLE);
                holder.layoutComment.setVisibility(View.VISIBLE);
                holder.layoutAllVoteResult.setVisibility(View.VISIBLE);
                holder.radioGroup.setVisibility(View.GONE);
            }
        }.start();

        // TODO: 5/25/2017 **************set the Visibility**********************
        holder.rdAnswer3.setVisibility(View.VISIBLE);
        holder.rdAnswer4.setVisibility(View.VISIBLE);
        holder.txtVoteSucess.setVisibility(View.GONE);

        if (details.getQueVoteStatus().equalsIgnoreCase("1")) {
            if (details.getUserCanVote().equalsIgnoreCase("1")) {
                holder.txtVote.setVisibility(View.VISIBLE);
            } else {
                holder.txtVote.setVisibility(View.INVISIBLE);
                utils.setRadioButtonAsPerVote(holder.rdAnswer1, holder.rdAnswer2, holder.rdAnswer3, holder.rdAnswer4);
                if (details.getUserId().equalsIgnoreCase(loginUserId)) {
                    holder.txtVoteSucess.setVisibility(View.GONE);
                } else {
                    holder.txtVoteSucess.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.txtVote.setVisibility(View.INVISIBLE);
            holder.txtVoteSucess.setVisibility(View.GONE);
            utils.setRadioButtonAsPerVote(holder.rdAnswer1, holder.rdAnswer2, holder.rdAnswer3, holder.rdAnswer4);
        }
        // TODO: 6/20/2017 set the layout if any user have any friends
        /*if (Utils.ReadSharePrefrence(context, Constant.USER_FOLLOWING).length() > 0) {
            holder.layoutShare.setVisibility(View.VISIBLE);
        } else {
            holder.layoutShare.setVisibility(View.GONE);
        }*/
        // TODO: 6/20/2017 set the layout if any comment on post
        if (details.getQueComment() != null) {
            holder.layoutCommentText.setVisibility(View.VISIBLE);
        } else {
            holder.layoutCommentText.setVisibility(View.GONE);
        }
        // TODO: 5/25/2017 ********************** End ************************

        // TODO: 5/25/2017 **************set the value in TextView**********************
        holder.txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
        holder.txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
        holder.txtUserName.setText(details.getUserUserName());
        holder.txtCategory.setText(details.getQueCategory());
        holder.txtPostDate.setText(details.getQuePostDate());
        holder.txtQuetion.setText(details.getQueTitle());
        if (!details.getQueTag().equalsIgnoreCase("")) {
            holder.txtQuetionTag.setText(details.getQueTag());
        } else {
            holder.txtQuetionTag.setVisibility(View.GONE);
        }
        if (details.getQueComment() != null) {
            holder.txtCommentText.setText(details.getQueComment().replaceAll("%20", " "));
            holder.txtCommentUser.setText(details.getQueCommentUser());
        }
        holder.rdAnswer1.setText(details.getQueOptionFirst());
        holder.rdAnswer2.setText(details.getQueOptionSecond());
        holder.txtAnswer1.setText(details.getQueOptionFirst());
        holder.txtAnswer2.setText(details.getQueOptionSecond());
        holder.txtVotePercentage1.setText("Vote:" + details.getQueVotePercentage1() + "%");
        holder.txtVoteQueCount1.setText("Vote Count:" + details.getQueVoteCount1());
        holder.txtVotePercentage2.setText("Vote:" + details.getQueVotePercentage2() + "%");
        holder.txtVoteQueCount2.setText("Vote Count:" + details.getQueVoteCount2());
        holder.optionProgress1.setProgress(Float.parseFloat(details.getQueVotePercentage1()));
        holder.optionProgress2.setProgress(Float.parseFloat(details.getQueVotePercentage2()));

        // TODO: 5/26/2017 set the radio button & progressbar as per option
        if (!details.getQueOptionThird().equalsIgnoreCase("")) {
            holder.rdAnswer3.setText(details.getQueOptionThird());
            holder.txtAnswer3.setText(details.getQueOptionThird());
            holder.txtVotePercentage3.setText("Vote:" + details.getQueVotePercentage3() + "%");
            holder.txtVoteQueCount3.setText("Vote Count:" + details.getQueVoteCount3());
            holder.optionProgress3.setProgress(Float.parseFloat(details.getQueVotePercentage3()));
        } else {
            holder.rdAnswer3.setVisibility(View.GONE);
            holder.layoutVoteResult3.setVisibility(View.GONE);
        }
        if (!details.getQueOptionFourth().equalsIgnoreCase("")) {
            holder.rdAnswer4.setText(details.getQueOptionFourth());
            holder.txtAnswer4.setText(details.getQueOptionFourth());
            holder.txtVotePercentage4.setText("Vote:" + details.getQueVotePercentage4() + "%");
            holder.txtVoteQueCount4.setText("Vote Count:" + details.getQueVoteCount4());
            holder.optionProgress4.setProgress(Float.parseFloat(details.getQueVotePercentage4()));

        } else {
            holder.rdAnswer4.setVisibility(View.GONE);
            holder.layoutVoteResult4.setVisibility(View.GONE);
        }

        if (details.getIntName().size() > 0) {
            holder.txtInterest1.setText(details.getIntName().get(0));
            holder.txtLevel1.setText(details.getStartRankName().get(0));
        } else {
            holder.layoutInterest1.setVisibility(View.GONE);
        }
        if (details.getIntName().size() > 1) {
            holder.txtInterest2.setText(details.getIntName().get(1));
            holder.txtLevel2.setText(details.getStartRankName().get(1));
        } else {
            holder.layoutInterest2.setVisibility(View.GONE);
        }
        if (details.getIntName().size() > 2) {
            holder.txtInterest3.setText(details.getIntName().get(2));
            holder.txtLevel3.setText(details.getStartRankName().get(2));
        } else {
            holder.layoutInterest3.setVisibility(View.GONE);
        }

        // TODO: 5/25/2017 ********************** End ************************

// TODO: 5/25/2017 **************set defalut icon status**********************
        // TODO: 3/28/2017 set like icon as per its selection
        if (details.getQueLikeStatus().equalsIgnoreCase("0")) {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_unlike_icon));
        } else {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_like_icon));
        }

        // TODO: 5/25/2017 ************** End **********************


        // TODO: 5/25/2017 **************set the value in ImageView & VideoView*******************

        // TODO: 2/23/2017 set user profile image of comment user
        utils.setCommentUserImageInPicasso(details, holder.imgCommentUserProfilePic);

        // TODO: 2/23/2017 set user profile image of post user
        utils.setPostUserImageInPicasso(details, holder.imgProfilePic);

        // TODO: 2/23/2017 set image & video dynamically
        utils.setPostImageVideo(details, holder.imgQuestionPic, holder.vdProfile, holder.layoutMedia);

        //TODO: 5/25/2017 ********************** End ************************


        // TODO: 5/25/2017 **************set the click event**********************
        holder.layoutFacebook.setOnClickListener(this);
        holder.layoutTwitter.setOnClickListener(this);
        // TODO: 6/7/2017 get list of following people
        holder.spinnnerFollowing.setItems(arrayfollowingUserList, "Following", this);

        // TODO: 3/28/2017 redirect to show all comments
        holder.txtViewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CommentPeopleDetail();
                utils.setIdOfQuestion(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        holder.txtCommentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CommentPeopleDetail();
                utils.setIdOfQuestion(details, fragment);
                utils.replaceFragment(fragment);

            }
        });
        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/27/2017 store the comment
                commentText = holder.edCommentText.getText().toString().replaceAll(" ", "%20");
                if (commentText.length() > 0) {
                    new CommentPost(holder, arrayList, loginUserId, details.getQueId(), commentText).execute();
                }
            }
        });

        // TODO: 3/30/2017 peform click to like and unlike the post
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String liketask = " ";
                utils.setDynamicLikeUnLike(details, position, arrayList, holder.imgLike, liketask);
                notifyDataSetChanged();
                new LikePost(position, holder, arrayList, loginUserId, details.getQueId(), liketask).execute();
            }
        });

        holder.txtVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answer = Utils.ReadSharePrefrence(context, Constant.ANSWER);
                utils.dynamicVoting(details, position, arrayList, holder.txtVote);
                notifyDataSetChanged();
                new SubmitVote(holder, position, loginUserId, details.getQueId(), Answer, arrayList).execute();
            }
        });

        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfPostUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        holder.imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfPostUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        holder.txtCommentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfCommentUser(details, fragment);
                utils.replaceFragment(fragment);
            }
        });

        // TODO: 3/24/2017 vote status
        holder.rdAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(context, Constant.ANSWER, "1");
            }
        });
        holder.rdAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(context, Constant.ANSWER, "2");
            }
        });
        holder.rdAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(context, Constant.ANSWER, "3");
            }
        });
        holder.rdAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.WriteSharePrefrence(context, Constant.ANSWER, "4");
            }
        });

        // TODO: 6/9/2017 share the post with friends
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePost(holder, position, loginUserId, followingPeopleId, details.getQueId(), arrayList).execute();
                //Toast.makeText(context, "Please select user to share.", Toast.LENGTH_SHORT).show();

            }
        });
// TODO: 5/25/2017 ********************** End ************************
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onClick(View v) {
        PackageManager pm = context.getPackageManager();
        switch (v.getId()) {
            case R.id.adapter_home_list_layoutfb:
                Intent ifeb = new Intent("android.intent.category.LAUNCHER");
                ifeb.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
                context.startActivity(ifeb);
                break;

            case R.id.adapter_home_list_layouttwitter:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.twitter.android", "com.twitter.android.LoginActivity");
                context.startActivity(intent);
                //to share application link via twitter
                /*try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.raviarchi\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    context.startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }*/
                break;
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        utils.getSelectedFriendsId(selected, arrayfollowingUserIdList, followingPeopleId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_home_list_spinnerFollowing)
        MultiSelectSpinner spinnnerFollowing;
        @BindView(R.id.adapter_home_list_layoutfb)
        LinearLayout layoutFacebook;
        @BindView(R.id.adapter_home_list_layoutshare)
        LinearLayout layoutShare;
        @BindView(R.id.adapter_home_list_btnshare)
        Button btnShare;
        @BindView(R.id.adapter_home_list_layout_all_voteresult)
        LinearLayout layoutAllVoteResult;
        @BindView(R.id.adapter_home_list_layoutCommentText)
        LinearLayout layoutCommentText;
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
        MultiSelectionSpinner multiSelectionSpinnerFollowing;
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

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    // TODO: 3/23/2017 submit vote
    private class SubmitVote extends AsyncTask<String, String, String> {
        String id, queId, answer;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;
        MyViewHolder holder;

        public SubmitVote(MyViewHolder holder, int position, String id, String queId, String answer, ArrayList<UserProfileDetails> arrayList) {
            this.holder = holder;
            this.position = position;
            this.id = id;
            this.queId = queId;
            this.answer = answer;
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(context);
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
                    arrayList.add(details);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                holder.txtVote.setVisibility(View.GONE);
            } else {
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("E", "" + e);
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
        MyViewHolder holder;

        private SharePost(MyViewHolder holder, int position, String userid, String otheruserId, String queId, ArrayList<UserProfileDetails> arrayList) {
            this.holder = holder;
            this.position = position;
            this.loginuser_id = userid;
            this.otheruser_id = otheruserId;
            this.que_id = queId;
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(context);
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
                    Toast.makeText(context, "Success! Post shared to selected users", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
        int position;
        ArrayList<UserProfileDetails> arrayList;
        MyViewHolder holder;

        private LikePost(int position, MyViewHolder holder, ArrayList<UserProfileDetails> arrayList, String id, String queId, String task) {
            this.position = position;
            this.holder = holder;
            this.user_id = id;
            this.que_id = queId;
            this.task = task;
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(context);
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
                    JSONObject likeObject = jsonObject.getJSONObject("inserted_data");
                } else {
                    if (task.equalsIgnoreCase("add")) {
                        details.setQueLikeStatus("0");
                        details.setQueLikeTotalCount(details.getQueVoteTotalCount() - 1);
                    } else {
                        details.setQueLikeStatus("1");
                        details.setQueLikeTotalCount(details.getQueVoteTotalCount() + 1);
                    }
                    arrayList.add(details);
                    notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                if (details.getQueLikeStatus().equalsIgnoreCase("0")) {
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_unlike_icon));
                } else {
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_like_icon));
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
        MyViewHolder holder;

        public CommentPost(MyViewHolder holder, ArrayList<UserProfileDetails> arrayList, String id, String queId, String comment) {
            this.id = id;
            this.queId = queId;
            this.comment = comment;
            this.arrayList = arrayList;
            this.holder = holder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(context);
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
                holder.edCommentText.setText(" ");
                holder.layoutCommentText.setVisibility(View.VISIBLE);
                holder.txtCommentText.setText(details.getQueComment().replaceAll("%20", " "));
                holder.txtCommentUser.setText(details.getQueCommentUser());
                Picasso.with(context).load(details.getQueCommentUserProfilePic())
                        .transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder)
                        .into(holder.imgCommentUserProfilePic);
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

