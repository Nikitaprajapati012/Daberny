package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Activity.ChatActivity;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*** Created by archi on 07-Apr-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENDERVIEW = 0;
    private static final int RECEIVERVIEW = 1;
    private Context mContext;
    private Utils utils;
    private ArrayList<UserProfileDetails> arrayList;
    private UserProfileDetails details;
    private String loginuserId,senderId,strMessageSender,strMessageReceiver;

       public ChatAdapter(ChatActivity context, ArrayList<UserProfileDetails> arrayUserList) {
        this.mContext = context;
        utils = new Utils(context);
        this.arrayList = arrayUserList;
    }

    @Override
    public int getItemViewType(final int position) {
        if (arrayList.get(position).getUserId().equalsIgnoreCase(Utils.ReadSharePrefrence(mContext, Constant.USERID))) {
            return SENDERVIEW;
        } else {
            return RECEIVERVIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SENDERVIEW:
                View sender = inflater.inflate(R.layout.adapter_senderview, parent, false);
                viewHolder = new SenderViewHolder(sender);
                break;

            case RECEIVERVIEW:
                View receiver = inflater.inflate(R.layout.adapter_receiverview, parent, false);
                viewHolder = new ReceiverViewHolder(receiver);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        details =arrayList.get(position);
        loginuserId = Utils.ReadSharePrefrence(mContext,Constant.USERID);
        if (loginuserId.equalsIgnoreCase(details.getUserId())){
            senderId=details.getOtherUserId();
            strMessageReceiver=details.getUserMsgReceiver();
            strMessageSender=details.getUserMsgSender();
          }else
        {
            senderId=details.getUserId();
            strMessageSender=details.getUserMsgSender();
            strMessageReceiver=details.getUserMsgReceiver();
        }

        switch (holder.getItemViewType()) {
            case SENDERVIEW:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                if(details.getUserMsgType().equalsIgnoreCase("image")){
                    Picasso.with(mContext).load(strMessageSender).placeholder(R.drawable.ic_placeholder)
                            .transform(new RoundedTransformation(120,2))
                            .into(senderViewHolder.imgMsg);
                    /*if(Utils.ReadSharePrefrence(mContext,Constant.CHAT_LIKE).equalsIgnoreCase("1")){
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                        senderViewHolder.imgMsg.setLayoutParams(layoutParams);
                    }*/
                    senderViewHolder.msgTv.setVisibility(View.GONE);
                }else
                {
                    senderViewHolder.imgMsg.setVisibility(View.GONE);
                    senderViewHolder.msgTv.setVisibility(View.VISIBLE);
                    senderViewHolder.msgTv.setText(strMessageSender);
                }
                break;

            case RECEIVERVIEW:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                if(details.getUserMsgType().equalsIgnoreCase("image")){
                    Picasso.with(mContext).load(strMessageReceiver).placeholder(R.drawable.ic_placeholder)
                            .transform(new RoundedTransformation(120,2))
                            .into(receiverViewHolder.imgMsg);
                   /* if(Utils.ReadSharePrefrence(mContext,Constant.CHAT_LIKE).equalsIgnoreCase("1")){
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                        receiverViewHolder.imgMsg.setLayoutParams(layoutParams);
                    }*/
                    receiverViewHolder.msgTv.setVisibility(View.GONE);

                }else
                {
                    receiverViewHolder.imgMsg.setVisibility(View.GONE);
                    receiverViewHolder.msgTv.setVisibility(View.VISIBLE);
                    receiverViewHolder.msgTv.setText(strMessageReceiver);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;
        ImageView imgMsg;

        private SenderViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_sendview_text);
            imgMsg = (ImageView) itemView.findViewById(R.id.adapter_sendview_image);
        }
    }

    private class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;
        ImageView imgMsg;

        private ReceiverViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_receiverview_text);
            imgMsg = (ImageView) itemView.findViewById(R.id.adapter_receiverview_image);
        }
    }
}