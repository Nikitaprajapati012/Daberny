package com.example.raviarchi.daberny.Activity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Fragment.Tag;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.raviarchi.daberny.Activity.Utils.Constant.USERID;
import static com.example.raviarchi.daberny.Activity.Utils.Utils.ReadSharePrefrence;
import static com.example.raviarchi.daberny.R.string.next;

/*** Created by Ravi archi on 2/21/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    public String Id, userId, interest, notification, status, notificationType, username;
    public Utils utils;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        this.utils = new Utils(context);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getQueId();
        userId = ReadSharePrefrence(context, USERID);
        Picasso.with(context).load(userdetails.getUserImage()).placeholder(R.drawable.ic_placeholder)
                .into(holder.imgProfilepic);
        holder.imgPostpic.setVisibility(View.VISIBLE);
        Picasso.with(context).load(userdetails.getQueImage()).placeholder(R.drawable.ic_placeholder)
                .into(holder.imgPostpic);
        username = userdetails.getUserUserName().substring(0, 1).toUpperCase()
                + userdetails.getUserUserName().substring(1);
        notificationType = userdetails.getQueNotificationType();
        status = userdetails.getQueNotificationStatus();
        if (status.equalsIgnoreCase("0")) {
            holder.linearLayoutNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.linearLayoutNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.home_bg));
        }
        holder.imgProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherUserProfile();
                utils.setIdOfPostUser(userdetails, fragment);
                utils.replaceFragment(fragment);

            }
        });
        if (!notificationType.equalsIgnoreCase("")) {
            if (notificationType.equalsIgnoreCase("share")) {
                notification = " Share Your Post. ";
            } else if (notificationType.equalsIgnoreCase("like")) {
                notification = " Liked Your Post. ";
            } else if (notificationType.equalsIgnoreCase("comment")) {
                notification = " Comment On Your Post. ";
            } else if (notificationType.equalsIgnoreCase("vote")) {
                notification = " Voted For Your Post. ";
            } else if (notificationType.equalsIgnoreCase("follow")) {
                notification = " Started Following You. ";
            }
            String next = "<font color='#5f5f5f'>" + userdetails.getQuePostDate() + "</font>";
            holder.txtNotification.setText(Html.fromHtml(username + notification + next));
        }
        holder.linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notifyStatus = userdetails.getQueNotificationStatus();
                if (notifyStatus.equalsIgnoreCase("1")) {
                    arrayUserList.get(position).setQueNotificationStatus("0");
                    holder.imgProfilepic.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    holder.imgProfilepic.setBackgroundColor(ContextCompat.getColor(context, R.color.home_bg));
                    holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.home_bg));
                    holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.black));
                }
                notifyDataSetChanged();
                new ReadNotification(userdetails, holder, position, notifyStatus, userId, userdetails.getQueNotificationId()).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_notification_list_txtnotification)
        TextView txtNotification;
        @BindView(R.id.adapter_notification_list_imgprofilepic)
        CircleImageView imgProfilepic;
        @BindView(R.id.adapter_notification_list_imgpostpic)
        ImageView imgPostpic;
        @BindView(R.id.adapter_notification_list_layout)
        LinearLayout linearLayoutNotification;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private class ReadNotification extends AsyncTask<String, String, String> {
        String user_id, notification_id, notify_status;
        ProgressDialog pd;
        int pos;
        MyViewHolder holder;
        UserProfileDetails user_details;


        public ReadNotification(UserProfileDetails userdetail, MyViewHolder holder, int position, String notifyStatus, String userId, String notificationId) {
            this.holder = holder;
            this.pos = position;
            this.notify_status = notifyStatus;
            this.user_id = userId;
            this.notification_id = notificationId;
            this.user_details = userdetail;
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/get_user_notification/732/527
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "get_user_notification/" + user_id + "/" + notification_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Read Notification..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    UserProfileDetails details = new UserProfileDetails();
                    if (notify_status.equalsIgnoreCase("1")) {
                        details.setQueNotificationStatus("0");
                    }
                    arrayUserList.add(details);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayUserList.size() > 0) {
                if (notify_status.equalsIgnoreCase("0")) {
                    holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.home_bg));
                    holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.black));
                }
                Fragment fragment = null;
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                if (user_details.getQueNotificationType().equalsIgnoreCase("follow")) {
                    bundle.putString("id", user_details.getUserId());
                    fragment = new OtherUserProfile();
                } else {
                    bundle.putString("userprofiledetails", gson.toJson(user_details));
                    fragment = new Tag();
                }
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    utils.replaceFragment(fragment);
                }
            }
        }
    }
}
