package com.example.raviarchi.daberny.Activity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.raviarchi.daberny.Activity.Fragment.CommentPeopleDetail;
import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
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
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/*** Created by Ravi archi on 2/21/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    public long time;
    public Utils utils;
    public UserProfileDetails details;
    float progress, remainingProgress;
    RelativeLayout relativeProgress, relativeRemaining;
    private ArrayList<String> followingList;
    private Object[] getFollowingListSpinner;
    private String loginUserId, queId, Answer,followingPeopleName, commentText;
    private CountDownTimer countdowntimer;
    private ArrayList<UserProfileDetails> arrayList;
    private Context context;
    private int seconds, minutes, hours;

    public HomeAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayList = arraylist;
        this.utils = new Utils(context);
        notifyDataSetChanged();
        Log.d("Length", "" + arrayList.size());
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
        queId = details.getQueId();

        // TODO: 5/25/2017 **************set the Visibility**********************
        holder.rdAnswer3.setVisibility(View.VISIBLE);
        holder.rdAnswer4.setVisibility(View.VISIBLE);
        if (details.getQueVoteStatus().equalsIgnoreCase("1")){
            if (details.getUserCanVote().equalsIgnoreCase("1")) {
                holder.txtVote.setVisibility(View.VISIBLE);
            }
            else {
                holder.txtVote.setVisibility(View.INVISIBLE);
                holder.rdAnswer1.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer2.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer3.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer4.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                if (details.getUserId().equalsIgnoreCase(loginUserId)) {
                    holder.txtVoteSucess.setVisibility(View.GONE);
                } else {
                    holder.txtVoteSucess.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.txtVote.setVisibility(View.INVISIBLE);
            holder.txtVoteSucess.setVisibility(View.GONE);
            holder.rdAnswer1.setButtonDrawable(new ColorDrawable(0xFFFFFF));
            holder.rdAnswer2.setButtonDrawable(new ColorDrawable(0xFFFFFF));
            holder.rdAnswer3.setButtonDrawable(new ColorDrawable(0xFFFFFF));
            holder.rdAnswer4.setButtonDrawable(new ColorDrawable(0xFFFFFF));
        }

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
        if (!details.getQueTag().equalsIgnoreCase("")){
            holder.txtQuetionTag.setText(details.getQueTag());
        }
        else
        {
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
        /*holder.optionProgress1.setPadding(5);
        holder.optionProgress2.setPadding(5);*/

        // TODO: 5/26/2017 set the radio button & progressbar as per option
        if (!details.getQueOptionThird().equalsIgnoreCase("")) {
            holder.rdAnswer3.setText(details.getQueOptionThird());
            holder.txtAnswer3.setText(details.getQueOptionThird());
            holder.txtVotePercentage3.setText("Vote:" + details.getQueVotePercentage3() + "%");
            holder.txtVoteQueCount3.setText("Vote Count:" + details.getQueVoteCount3());
            // holder.optionProgress3.setPadding(5);
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
            //holder.optionProgress4.setPadding(5);
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
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlike_icon));
        } else {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_icon));
        }

        // TODO: 5/25/2017 ************** End **********************


        // TODO: 5/25/2017 **************set the value in ImageView & VideoView*******************
        // TODO: 2/23/2017 set user profile image of comment user
        if (details.getQueCommentUserProfilePic() != null) {
            if (details.getQueCommentUserProfilePic().length() > 0) {
                Picasso.with(context).load(details.getQueCommentUserProfilePic()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(holder.imgCommentUserProfilePic);
            }

        } else {
            Picasso.with(context).load(R.drawable.ic_placeholder).
                    transform(new RoundedTransformation(120, 2)).
                    placeholder(R.drawable.ic_placeholder).into(holder.imgCommentUserProfilePic);
        }
        // TODO: 2/23/2017 set user profile image of post user
        if (details.getUserImage() != null) {
            if (details.getUserImage().length() > 0) {
                Picasso.with(context).load(details.getUserImage()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(holder.imgProfilePic);
            }
        } else {
            Picasso.with(context).load(R.mipmap.ic_launcher).
                    transform(new RoundedTransformation(120, 2)).
                    placeholder(R.drawable.ic_placeholder).into(holder.imgProfilePic);
        }
        // TODO: 2/23/2017 set image & video dynamically
        if (details.getQueImageName().length() > 0) {
            if (details.getQueImage() != null) {
                holder.imgQuestionPic.setVisibility(View.VISIBLE);
                holder.vdProfile.setVisibility(View.VISIBLE);
                holder.layoutMedia.setVisibility(View.VISIBLE);
                if (details.getQueType().equalsIgnoreCase("0")) {
                    holder.layoutMedia.setVisibility(View.GONE);
                } else if (details.getQueType().equalsIgnoreCase("1")) {
                    holder.vdProfile.setVisibility(View.GONE);
                    Picasso.with(context).load(details.getQueImage()).placeholder(R.drawable.ic_placeholder).into(holder.imgQuestionPic);
                } else if (details.getQueType().equalsIgnoreCase("2")) {
                    holder.imgQuestionPic.setVisibility(View.GONE);
                    holder.vdProfile.setVideoURI(Uri.parse(details.getQueImage()));
                    holder.vdProfile.setMediaController(new MediaController(context));
                    holder.vdProfile.requestFocus();
                    holder.vdProfile.start();
                }
            }
        } else {
            holder.layoutMedia.setVisibility(View.GONE);
        }
        //TODO: 5/25/2017 ********************** End ************************


        // TODO: 5/25/2017 **************set the click event**********************
        holder.layoutFacebook.setOnClickListener(this);
        holder.layoutTwitter.setOnClickListener(this);
        holder.txtCommentUser.setOnClickListener(this);
        // TODO: 3/28/2017 redirect to show all comments
        holder.txtViewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //just pass question Id
                Fragment fragment = null;
                fragment = new CommentPeopleDetail();
                Bundle bundle = new Bundle();
                bundle.putString("queid", details.getQueId());
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame_contain_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/27/2017 store the comment
                commentText = holder.edCommentText.getText().toString().replaceAll(" ", "%20");
                new CommentPost(holder, arrayList, loginUserId, details.getQueId(), commentText).execute();
            }
        });
        // TODO: 3/30/2017 peform click to like and unlike the post
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String liketask = " ";
                String isLiked = details.getQueLikeStatus();
                liketask = isLiked.equalsIgnoreCase("1") ? "remove" : "add";
                if (liketask.equalsIgnoreCase("add")) {
                    arrayList.get(position).setQueLikeStatus("1");
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_icon));
                    arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() + 1);
                } else {
                    arrayList.get(position).setQueLikeStatus("0");
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlike_icon));
                    arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() - 1);
                }
                notifyDataSetChanged();
                new LikePost(position, holder, arrayList, loginUserId, details.getQueId(), liketask).execute();
            }
        });

        holder.txtVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answer = Utils.ReadSharePrefrence(context, Constant.ANSWER);
                String isVoteStatus = details.getQueVoteStatus();
                String isCanVote = details.getUserCanVote();

                if (isVoteStatus.equalsIgnoreCase("1")) {
                    if (isCanVote.equalsIgnoreCase("1")) {
                        arrayList.get(position).setUserCanVote("0");
                        arrayList.get(position).setQueVoteTotalCount(arrayList.get(position).getQueVoteTotalCount() + 1);
                        holder.txtVote.setVisibility(View.GONE);
                    }
                }
                notifyDataSetChanged();
                new SubmitVote(holder,position, loginUserId,details.getQueId(), Answer, arrayList).execute();
            }
        });

        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("id", details.getUserId());
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame_contain_layout, fragment);
                    transaction.commit();
                }
            }
        });
        holder.txtCommentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("id", details.getQueCommentUserId());
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame_contain_layout, fragment);
                    transaction.commit();
                }
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
// TODO: 5/25/2017 ********************** End ************************


        //TODO: 3/21/2017 countdown timer
        Long timimg = details.getQueTiming();
        Long createdtime = details.getQueCreatedTime();
        Log.d("timing_que", "" + timimg);
        Log.d("created_time", "" + createdtime);

        // countdowntimer = new CountDownTimerClass(holder, timimg, 1000).start();
        countdowntimer = new CountDownTimer(timimg, 1000) {
            @Override
            public void onFinish() {
                holder.rdAnswer1.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer2.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer3.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.rdAnswer4.setButtonDrawable(new ColorDrawable(0xFFFFFF));
                holder.layoutCounter.setVisibility(View.GONE);
                holder.layoutVote.setVisibility(View.INVISIBLE);
                holder.layoutLike.setVisibility(View.VISIBLE);
                holder.layoutComment.setVisibility(View.VISIBLE);
                holder.layoutAllVoteResult.setVisibility(View.VISIBLE);
                holder.radioGroup.setVisibility(View.GONE);
                holder.txtVoteSucess.setVisibility(View.GONE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000) % 60;
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                holder.txtHour.setText("" + String.format("%2d", hours));
                holder.txtMinute.setText("" + String.format("%2d", minutes));
                holder.txtSecond.setText("" + String.format("%2d", seconds));
                holder.layoutLike.setVisibility(View.INVISIBLE);
                holder.layoutComment.setVisibility(View.GONE);
                holder.layoutAllVoteResult.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.VISIBLE);
                holder.layoutCounter.setVisibility(View.VISIBLE);
            }
        }.start();

        // TODO: 3/20/2017 get list of following people
        // holder.spinnnerFollowing.setListener(this);

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
                break;
        }
    }


    @Override
    public void selectedIndices(List<Integer> indices) {
        followingList = new ArrayList<>();
        ArrayList<String> followingNameList = new ArrayList<>();
        getFollowingListSpinner = indices.toArray();
        for (int i = 0; i < getFollowingListSpinner.length; i++) {
            //followingList.add(details.getUserFollowingId().get(i));
            followingList.add(details.getUserFollowingName().get((Integer) getFollowingListSpinner[i]));
            //followingNameList.add(details.getUserFollowingName().get(i));
        }
        /*interestID = new ArrayList<>();
        getinterestidspinner = indices.toArray();
        for (int i = 0; i < getinterestidspinner.length; i++) {
            interestID.add(arrayInterestIDList.get((Integer) getinterestidspinner[i]));
        }*/
        Log.d("follow_id", "" + followingList.toString());
    }

    @Override
    public void selectedStrings(List<String> strings) {
        followingPeopleName = strings.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");
        Log.d("following_name ", "string=" + followingPeopleName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MultiSelectionSpinner spinnnerFollowing;
        public Handler handler;
        public Runnable runnable;
        @BindView(R.id.adapter_home_list_layoutfb)
        LinearLayout layoutFacebook;
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
            // multiSelectionSpinnerFollowing = (MultiSelectionSpinner) itemView.findViewById(R.id.adapter_home_list_spinnerFollowing);
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
            this.holder=holder;
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
                    Toast.makeText(context, "Success! Thanks for vote..", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(arrayList.size() > 0){
                holder.txtVote.setVisibility(View.GONE);
            }
            else{
                new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        Log.d("E",""+e);
                    }
                };
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

        public LikePost(int position, MyViewHolder holder, ArrayList<UserProfileDetails> arrayList, String id, String queId, String task) {
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
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlike_icon));
                } else {
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_icon));
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

