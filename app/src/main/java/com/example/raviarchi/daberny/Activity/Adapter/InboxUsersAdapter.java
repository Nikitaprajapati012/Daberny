package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Activity.ChatActivity;
import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


/*** Created by Ravi archi on 2/21/2017.
 */

public class InboxUsersAdapter extends RecyclerView.Adapter<InboxUsersAdapter.MyViewHolder> {
    public String Id, loginUserId, interest;
    public Utils utils;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public InboxUsersAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_inboxuser_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getUserId();
        holder.txtMessageTime.setText(userdetails.getUserMsgPostDate());
        loginUserId = Utils.ReadSharePrefrence(context, Constant.USERID);
        if (userdetails.getUserId().equalsIgnoreCase(loginUserId)) {
            Utils.WriteSharePrefrence(context,Constant.OTHER_USERID,userdetails.getOtherUserId());
            if (userdetails.getUserMsgType().equalsIgnoreCase("image")){
                holder.txtMessage.setText("You sent a message");
            }else {
                holder.txtMessage.setText(userdetails.getUserMsgSender());
            }
            holder.txtUsername.setText(userdetails.getOtherUserName());
            if (userdetails.getOtherUserImage().length() > 0) {
                Picasso.with(context).load(userdetails.getOtherUserImage()).
                        transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).transform(new RoundedTransformation(120, 2))
                        .placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            }
        } else {
            Utils.WriteSharePrefrence(context,Constant.OTHER_USERID,userdetails.getUserId());
            if (userdetails.getUserMsgType().equalsIgnoreCase("image")){
                holder.txtMessage.setText(R.string.yousentmsg);
            }else {
                holder.txtMessage.setText(userdetails.getUserMsgReceiver());
            }
            holder.txtUsername.setText(userdetails.getUserUserName());
            if (userdetails.getUserImage().length() > 0) {
                Picasso.with(context).load(userdetails.getUserImage()).
                        transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).transform(new RoundedTransformation(120, 2))
                        .placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            }
        }

        holder.layoutInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent ichat =new Intent(context,ChatActivity.class);
                ichat.putExtra("userprofiledetails", gson.toJson(userdetails));
                context.startActivity(ichat);
                }
        });
    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_inboxuser_list_layout)
        LinearLayout layoutInbox;
        @BindView(R.id.adapter_inboxuser_list_imgprofilepic)
        ImageView imgProfile;
        @BindView(R.id.adapter_inboxuser_list_txtusername)
        TextView txtUsername;
        @BindView(R.id.adapter_inboxuser_list_txtmessage)
        TextView txtMessage;
        @BindView(R.id.adapter_inboxuser_list_txtmessagetime)
        TextView txtMessageTime;
        @BindView(R.id.adapter_inboxuser_list_txtinterest1)
        TextView txtInterest1;
        @BindView(R.id.adapter_inboxuser_list_txtinterest2)
        TextView txtInterest2;
        @BindView(R.id.adapter_inboxuser_list_txtinterest3)
        TextView txtInterest3;
        @BindView(R.id.adapter_inboxuser_list_txtlevel1)
        TextView txtLevel1;
        @BindView(R.id.adapter_inboxuser_list_txtlevel2)
        TextView txtLevel2;
        @BindView(R.id.adapter_inboxuser_list_txtlevel3)
        TextView txtLevel3;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
