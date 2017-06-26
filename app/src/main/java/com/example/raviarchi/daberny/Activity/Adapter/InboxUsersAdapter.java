package com.example.raviarchi.daberny.Activity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Activity.ChatActivity;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.koushikdutta.async.http.socketio.ExceptionCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*** Created by Ravi archi on 2/21/2017.
 */

public class InboxUsersAdapter extends RecyclerView.Adapter<InboxUsersAdapter.MyViewHolder> {
    private String Id, loginUserId, interest;
    public Utils utils;
    private ArrayList<UserProfileDetails> arrayUserList;
    private Context context;

    public InboxUsersAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        this.utils = new Utils(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_inboxuser_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getUserId();
        holder.txtMessageTime.setText(userdetails.getUserMsgPostDate());
        loginUserId = Utils.ReadSharePrefrence(context, Constant.USERID);
        if (userdetails.getUserId().equalsIgnoreCase(loginUserId)) {
            // TODO: 6/12/2017 set the details of the other user which is in sender
            Utils.WriteSharePrefrence(context, Constant.OTHER_USERID, userdetails.getOtherUserId());
            holder.txtUsername.setText(userdetails.getOtherUserName());
            if (userdetails.getUserMsgType().equalsIgnoreCase("image")) {
                holder.txtMessage.setText(R.string.yousentmsg);
            } else {
                utils.setTextOfInboxList(userdetails,holder.txtMessage);
                holder.txtMessage.setText(userdetails.getUserMsgSender());
            }
            if (userdetails.getOtherUserImage().length() > 0) {
                Picasso.with(context).load(userdetails.getOtherUserImage()).
                        transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).transform(new RoundedTransformation(120, 2))
                        .placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            }
        } else {
            // TODO: 6/12/2017 set the details of the other user which is in receiver
            Utils.WriteSharePrefrence(context, Constant.OTHER_USERID, userdetails.getUserId());
            holder.txtUsername.setText(userdetails.getUserUserName());
            if (userdetails.getUserMsgType().equalsIgnoreCase("image")) {
                holder.txtMessage.setText(R.string.yousentmsg);
            } else {
                utils.setTextOfInboxList(userdetails,holder.txtMessage);
                holder.txtMessage.setText(userdetails.getUserMsgReceiver());
            }
            if (userdetails.getUserImage().length() > 0) {
                Picasso.with(context).load(userdetails.getUserImage()).
                        transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).transform(new RoundedTransformation(120, 2))
                        .placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
            }
        }
        final String otherUserId=Utils.ReadSharePrefrence(context,Constant.OTHER_USERID);
        holder.layoutInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                userdetails.setUserMsgStatus("0");
                holder.txtMessage.setTypeface(null, Typeface.NORMAL);
                Intent ichat = new Intent(context, ChatActivity.class);
                ichat.putExtra("userprofiledetails", gson.toJson(userdetails));
                context.startActivity(ichat);
            }
        });
        holder.layoutInbox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Delete Conversion?")
                        .setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        arrayUserList.remove(position);
                                        notifyDataSetChanged();
                                        new DeleteConverstion(loginUserId,otherUserId,position,arrayUserList).execute();
                                        dialog.cancel();
                                    }
                                }).show();
                return true;
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

    private class DeleteConverstion extends AsyncTask<String, String, String> {
        String userId, otheruserId;
        ProgressDialog pd;
        int position;
        ArrayList<UserProfileDetails> arrayUserList;

        private DeleteConverstion(String id, String queId, int position, ArrayList<UserProfileDetails> arrayUserList) {
            this.userId = id;
            this.otheruserId = queId;
            this.position=position;
            this.arrayUserList=arrayUserList;
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
            //http://181.224.157.105/~hirepeop/host2/surveys/api/delete_conversion/752/864
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "delete_conversion/" + userId + "/" + otheruserId );
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", "Delete Converstion..." + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    JSONObject voteObject = jsonObject.getJSONObject("data");
                    if (voteObject.length() > 0) {
                        UserProfileDetails details = new UserProfileDetails();
                        details.setUserImage(voteObject.getString("user_image"));
                        arrayUserList.add(details);
                        notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
