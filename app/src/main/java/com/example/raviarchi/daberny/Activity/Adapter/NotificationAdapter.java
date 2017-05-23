package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Fragment.Tag;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi archi on 2/21/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    public String Id, interest, notification,status,notificationType,contentId,username;
    public Utils utils;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getQueId();
        username = userdetails.getUserUserName().substring(0,1).toUpperCase()
                +userdetails.getUserUserName().substring(1) ;
        notificationType = userdetails.getQueNotificationType();
        status = userdetails.getQueNotificationStatus();
        if (status.equalsIgnoreCase("0"))
        {   holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.login_bg));
            holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.txtNotification.setBackgroundColor(ContextCompat.getColor(context, R.color.signinbg));
            holder.txtNotification.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        if (!notificationType.equalsIgnoreCase("")) {
            if (notificationType.equalsIgnoreCase("share")) {
                notification = " Share Your Post";
            } else if (notificationType.equalsIgnoreCase("like")) {
                notification = " Like Your Post";
            }  else if (notificationType.equalsIgnoreCase("comment")) {
                notification = " Comment On Your Post";
            } else if (notificationType.equalsIgnoreCase("vote")) {
                notification = " Voted For Your Post";
            }else if (notificationType.equalsIgnoreCase("follow")) {
                notification = " Stared Following You";
            }
            holder.txtNotification.setText(username+ notification);
        }
        holder.linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=null;
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
               /* if (notificationType.equalsIgnoreCase("share")
                        || notificationType.equalsIgnoreCase("like")
                        || notificationType.equalsIgnoreCase("comment")
                        || notificationType.equalsIgnoreCase("vote")){

                }
                else*/ if (notificationType.equalsIgnoreCase("follow"))
                {
                    bundle.putString("id", userdetails.getUserId());
                    fragment = new OtherUserProfile();
                }
                else{
                    bundle.putString("userprofiledetails", gson.toJson(userdetails));
                    fragment = new Tag();
                }

                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame_contain_layout, fragment);
                    transaction.commit();
                }
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
        @BindView(R.id.adapter_notification_list_layout)
        LinearLayout linearLayoutNotification;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
