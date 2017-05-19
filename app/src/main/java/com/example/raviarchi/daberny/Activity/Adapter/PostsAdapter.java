package com.example.raviarchi.daberny.Activity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.raviarchi.daberny.Activity.Fragment.CommentPeopleDetail;
import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiplespinner.MultiSelectionSpinner;
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

/**
 * Created by Ravi archi on 2/21/2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> implements View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    public long time;
    public Utils utils;
    public UserProfileDetails details;
    private ArrayList<String> followingList;
    private Object[] getFollowingListSpinner;
    private String Id, queId, Answer, task, isVoteStatus, followingPeopleName, commentText, isLiked, liketask;
    private CountDownTimer countdowntimer;
    private ArrayList<UserProfileDetails> arrayList;
    private Context context;
    private int seconds, minutes, hours;

    public PostsAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayList = arraylist;
        this.utils = new Utils(context);
        notifyDataSetChanged();
        Log.d("Length", "" + arrayList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserProfileDetails details = arrayList.get(position);
        Id = Utils.ReadSharePrefrence(context, Constant.USERID);
        queId = details.getQueId();
        // TODO: 3/21/2017 countdown timer
        Long timimg = details.getQueTiming();
        Long createdtime = details.getQueCreatedTime();
        Log.d("timing_que", "" + timimg);

        // countdowntimer = new CountDownTimerClass(holder, timimg, 1000).start();
        countdowntimer = new CountDownTimer(timimg, 1000) {
            @Override
            public void onFinish() {
                holder.rdAnswer1.setClickable(false);
                holder.rdAnswer2.setClickable(false);
                holder.rdAnswer3.setClickable(false);
                holder.rdAnswer4.setClickable(false);
                holder.layoutCounter.setVisibility(View.GONE);
                holder.layoutVote.setVisibility(View.GONE);
                holder.layoutLike.setVisibility(View.VISIBLE);
                holder.layoutComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000) % 60;
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                holder.txtHour.setText("" + String.format("%2d", hours));
                holder.txtMinute.setText("" + String.format("%2d", minutes));
                holder.txtSecond.setText("" + String.format("%2d", seconds));
                holder.layoutVote.setVisibility(View.VISIBLE);
                holder.layoutLike.setVisibility(View.GONE);
                holder.layoutComment.setVisibility(View.GONE);
                holder.layoutCounter.setVisibility(View.VISIBLE);
            }
        }.start();

        holder.layoutFacebook.setOnClickListener(this);
        holder.layoutTwitter.setOnClickListener(this);
        holder.layoutTwitter.setOnClickListener(this);
        // holder.multiSelectionSpinnerFollowing.setListener(this);
        holder.txtVote.setVisibility(View.VISIBLE);

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
                    transaction.commit();
                }
            }
        });

        holder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/27/2017 store the comment
                commentText = holder.edCommentText.getText().toString().replaceAll(" ", "%20");
                new CommentPost(arrayList, Id, queId, commentText).execute();
            }
        });


        // TODO: 3/28/2017 set like icon as per its selection

       /* isLiked = arrayList.get(position).getQueLikeStatus();
        Log.d("like_status_beforeclick", isLiked);
        liketask = isLiked.equalsIgnoreCase("1") ? "UnLike" : "Like";
        if (liketask.equalsIgnoreCase("Like")) {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_unselected));
        } else {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_selected));
        }
*/
        // TODO: 3/30/2017 peform click to like and unlike the post
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String count = "";
                isLiked = details.getQueLikeStatus();
                liketask = isLiked.equalsIgnoreCase("1") ? "UnLike" : "Like";
                if (liketask.equalsIgnoreCase("Like")) {
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_unselected));
                    arrayList.get(position).setQueLikeStatus("1");
                    if (arrayList.get(position).getQueLikeTotalCount() != 0) {
                        arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() - 1);
                    } else {
                        arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount());
                    }
                } else {
                    holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_selected));
                    if (arrayList.get(position).getQueLikeTotalCount() != 0) {
                        arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() + 1);
                    } else {
                        arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount());
                    }
                }
                notifyDataSetChanged();
                new LikePost(position, holder, arrayList, Id, queId, liketask).execute();
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
        isVoteStatus = details.getQueVoteStatus();
        task = isVoteStatus.equalsIgnoreCase("1") ? "hide" : "show";
        //for 0
        if (task.equalsIgnoreCase("show")) {
            holder.txtVote.setVisibility(View.VISIBLE);
            holder.txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
            arrayList.get(position).setQueVoteStatus("1");
        } else {
            //for 1
            holder.rdAnswer1.setClickable(false);
            holder.rdAnswer2.setClickable(false);
            holder.rdAnswer3.setClickable(false);
            holder.rdAnswer4.setClickable(false);
            holder.txtVote.setVisibility(View.GONE);
            holder.txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
        }
        holder.txtVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answer = Utils.ReadSharePrefrence(context, Constant.ANSWER);
                //isVoteStatus = details.getQueVoteStatus();
                task = isVoteStatus.equalsIgnoreCase("1") ? "hide" : "show";
                //if vote stutus =0
                if (task.equalsIgnoreCase("show")) {
                    holder.txtVote.setVisibility(View.VISIBLE);
                    holder.txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
                    arrayList.get(position).setQueVoteStatus("1");
                } else {
                    // for 1
                    holder.txtVote.setVisibility(View.GONE);
                    holder.txtVoteCount.setText(String.valueOf(details.getQueVoteTotalCount()));
                }
                if (Answer.length() > 0) {
                    Log.d("userid", Id);
                    Log.d("queid", queId);
                    Log.d("ansid", Answer);
                    new SubmitVote(position, Id, queId, Answer, arrayList).execute();
                } else {
                    Toast.makeText(context, "Please Select Your Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO: 2/23/2017 set text from URL
        holder.txtLikeCount.setText(String.valueOf(details.getQueLikeTotalCount()));
        holder.txtUserName.setText(details.getUserUserName());
        holder.txtCategory.setText(details.getQueCategory());
        holder.txtPostDate.setText(details.getQuePostDate());
        holder.txtQuetion.setText(details.getQueTitle());
        holder.txtCommentText.setText(details.getQueComment());
        holder.txtCommentUser.setText(details.getQueCommentUser());

        // TODO: 3/20/2017 get list of following people
        // holder.spinnnerFollowing.setListener(this);

        // TODO: 3/20/2017 set the list as per data
        holder.rdAnswer3.setVisibility(View.VISIBLE);
        holder.rdAnswer4.setVisibility(View.VISIBLE);
        holder.rdAnswer1.setText(details.getQueOptionFirst());
        holder.rdAnswer2.setText(details.getQueOptionSecond());
        if (details.getIntName().size() > 0) {
            holder.txtInterest1.setText(details.getIntName().get(0));
        } else {
            holder.txtInterest1.setText("");
        }
        if (details.getIntName().size() > 1) {
            holder.txtInterest2.setText(details.getIntName().get(1));
        } else {
            holder.txtInterest2.setText("");
        }
        if (details.getIntName().size() > 2) {
            holder.txtInterest3.setText(details.getIntName().get(2));
        } else {
            holder.txtInterest3.setText("");
        }

        if (!details.getQueOptionThird().equalsIgnoreCase("")) {
            holder.rdAnswer3.setText(details.getQueOptionThird());
        } else {
            holder.rdAnswer3.setVisibility(View.GONE);
        }
        if (!details.getQueOptionFourth().equalsIgnoreCase("")) {
            holder.rdAnswer4.setText(details.getQueOptionFourth());
        } else {
            holder.rdAnswer4.setVisibility(View.GONE);
        }

        if (details.getUserImage() != null) {
            if (details.getUserImage().length() > 0) {
                Picasso.with(context).load(details.getUserImage()).placeholder(R.drawable.ic_placeholder).into(holder.imgProfilePic);
            }
        } else {
            Picasso.with(context).load(R.mipmap.ic_launcher).placeholder(R.drawable.ic_placeholder).into(holder.imgProfilePic);
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

    private void showUserLikeUnLike(MyViewHolder holder) {
        String count = "";
        if (details.getQueLikeStatus().equalsIgnoreCase("1")) {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_selected));
            //count = details.getQueLikeTotalCount() + 1;
        } else {
            holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_heart_unselected));
            // count = details.getQueLikeTotalCount();
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

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*RoundCornerProgressBar progress1 = (RoundCornerProgressBar) itemView.findViewById(R.id.progress_1);
            progress1.setProgressColor(Color.parseColor("#ed3b27"));
            progress1.setProgressBackgroundColor(Color.parseColor("#55628f"));
            progress1.setMax(70);
            progress1.setProgress(45);*/
            // multiSelectionSpinnerFollowing = (MultiSelectionSpinner) itemView.findViewById(R.id.adapter_home_list_spinnerFollowing);
        }
    }

    private class CountDownTimerClass extends CountDownTimer {
        MyViewHolder holder;
        long millisInFuture, countDownInterval;

        public CountDownTimerClass(MyViewHolder holder, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            //this.millisInFuture
            this.holder = holder;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);

            holder.txtHour.setText("" + String.format("%2d", hours));
            holder.txtMinute.setText("" + String.format("%2d", minutes));
            holder.txtSecond.setText("" + String.format("%2d", seconds));
            holder.layoutVote.setVisibility(View.VISIBLE);
            holder.layoutLike.setVisibility(View.GONE);
            holder.layoutComment.setVisibility(View.GONE);
            holder.layoutCounter.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            //holder.txtVoteCount.setText("done!");
            holder.rdAnswer1.setClickable(false);
            holder.rdAnswer2.setClickable(false);
            holder.rdAnswer3.setClickable(false);
            holder.rdAnswer4.setClickable(false);
            holder.layoutCounter.setVisibility(View.GONE);
            holder.layoutVote.setVisibility(View.GONE);
            holder.layoutLike.setVisibility(View.VISIBLE);
            holder.layoutComment.setVisibility(View.VISIBLE);
        }
    }

    // TODO: 3/23/2017 submit vote
    private class SubmitVote extends AsyncTask<String, String, String> {
        String id, queId, answer;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;

        public SubmitVote(int position, String id, String queId, String answer, ArrayList<UserProfileDetails> arrayList) {
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
                    Toast.makeText(context, "Success! Thanks for vote..", Toast.LENGTH_SHORT).show();
                }/* else {
                    Toast.makeText(context, "Vote Already submit", Toast.LENGTH_SHORT).show();
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 3/28/2017  like and dislike the post
    private class LikePost extends AsyncTask<String, String, String> {
        String id, queId, task;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayList;
        MyViewHolder holder;

        public LikePost(int position, MyViewHolder holder, ArrayList<UserProfileDetails> arrayList, String id, String queId, String task) {
            this.position = position;
            this.holder = holder;
            this.id = id;
            this.queId = queId;
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
                } else {
                    if (task.equalsIgnoreCase("Like")) {
                        arrayList.get(position).setQueLikeStatus("1");
                    } else {
                        arrayList.get(position).setQueLikeStatus("0");
                    }
                    notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 3/27/2017 store the comments
    private class CommentPost extends AsyncTask<String, String, String> {
        String id, queId, comment;
        ProgressDialog pd;
        ArrayList<UserProfileDetails> arrayList;
        MyViewHolder holder;

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
            pd = new ProgressDialog(context);
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
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (arrayList.size() > 0) {
                holder.txtCommentText.setText(details.getQueComment());
                holder.txtCommentUser.setText(details.getQueCommentUser());
            } else {
                Toast.makeText(context, "Please Comment", Toast.LENGTH_SHORT).show();
            }*/
        }

    }
}

