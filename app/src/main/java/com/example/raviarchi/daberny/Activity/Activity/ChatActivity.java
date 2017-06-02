package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.ChatAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    public ChatAdapter chatAdapter;
    public ArrayList<UserProfileDetails> arrayUserList;
    public UserProfileDetails details;
    public String strMsg;
    @BindView(R.id.activity_chat_msg_edmsg)
    EditText edMessage;
    @BindView(R.id.activity_chat_txtsend)
    TextView txtSend;
    @BindView(R.id.activity_chat_imggallery)
    ImageView imgGallery;
    @BindView(R.id.headerview)
    RelativeLayout headerChatView;
    @BindView(R.id.activity_chat_layoutattachment)
    LinearLayout layoutAttachment;
    @BindView(R.id.header_icon)
    ImageView imgBack;
    @BindView(R.id.header_iconplus)
    ImageView imgAdd;
    @BindView(R.id.header_title)
    TextView txtHeaderTitle;
    String receiverId, loginUserId, strReceiverName;
    Utils utils;
    @BindView(R.id.activity_chat_msg_recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        click();
    }

    // TODO: 5/29/2017 perform click event
    //git
     private void click() {
        txtSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutAttachment.setVisibility(View.GONE);
                txtSend.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //  layoutAttachment.setVisibility(View.VISIBLE);
        //  txtSend.setVisibility(View.GONE);
    }

    // TODO: 5/29/2017 initilization
    private void init() {
        details = new UserProfileDetails();
        utils = new Utils(this);
        loginUserId = Utils.ReadSharePrefrence(this, Constant.USERID);
        if (getIntent().getExtras() != null) {
            Gson gson = new Gson();
            String strdetails = getIntent().getExtras().getString("userprofiledetails");
            details = gson.fromJson(strdetails, UserProfileDetails.class);
            if (loginUserId.equalsIgnoreCase(details.getUserId())) {
                txtHeaderTitle.setText(details.getOtherUserName());
                receiverId = details.getOtherUserId();
            } else {
                txtHeaderTitle.setText(details.getUserUserName());
                receiverId = details.getUserId();
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 5/29/2017 API Call for get chat details
        new GetChat(loginUserId, receiverId).execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_chat_imggallery:

                break;

            case R.id.activity_chat_txtsend:
                strMsg = edMessage.getText().toString().replaceAll(" ", "%20");
                new SendMessage(loginUserId, receiverId, strMsg).execute();
                break;

            case R.id.header_icon:
                onBackPressed();
                break;
        }
    }

    // TODO: 5/30/2017 bind the list with adapter
    private void openChatDetailsList() {
        chatAdapter = new ChatAdapter(ChatActivity.this, arrayUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    private class GetChat extends AsyncTask<String, String, String> {
        String receiver_id, login_id;
        private ProgressDialog pd;

        private GetChat(String loginUserId, String user_id) {
            this.login_id = loginUserId;
            this.receiver_id = user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayUserList = new ArrayList<>();
            pd = new ProgressDialog(ChatActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/get_chat_msg/752/669/
            String receivemessage;
            receivemessage = Constant.QUESTION_BASE_URL + "get_chat_msg" + "/" + login_id + "/" + receiver_id;
           /*
            if (Constant.ON_CLICK_NOTIFICATION == 0) {
                receivemessage = Constant.Base_URL + "get_chat_msg.php?" + "sender_id=" + user_id + "&recipient_id=" + receipt_id;
            } else {
                receivemessage = Constant.Base_URL + "get_chat_msg.php?" + "sender_id=" + sender_id + "&recipient_id=" + receipt_id;
                Constant.ON_CLICK_NOTIFICATION = 0;
            }*/
            return Utils.getResponseofGet(receivemessage);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPOSNE Message List", "" + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject followingObject = jsonArray.getJSONObject(i);
                        details = new UserProfileDetails();
                        details.setUserId(followingObject.getString("sender_id"));
                        details.setUserUserName(followingObject.getString("sender_username"));
                        details.setUserFullName(followingObject.getString("sender_fullname"));
                        details.setUserImage(followingObject.getString("sender_image"));
                        details.setOtherUserId(followingObject.getString("recipient_id"));
                        details.setOtherUserFullName(followingObject.getString("recipient_fullname"));
                        details.setOtherUserName(followingObject.getString("recipient_username"));
                        details.setOtherUserImage(followingObject.getString("recipient_image"));
                        details.setUserMsgReceiver(followingObject.getString("receiver_msg"));
                        details.setUserMsgSender(followingObject.getString("sender_msg"));
                        arrayUserList.add(details);
                    }
                    if (arrayUserList.size() > 0) {
                        openChatDetailsList();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMessage extends AsyncTask<String, String, String> {
        String strMessage, userId, receiverId;

        private SendMessage(String user_id, String receipt_id, String strMsg) {
            this.userId = user_id;
            this.receiverId = receipt_id;
            this.strMessage = strMsg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/insert_chat_msg/677/669/hello
            String sendmessage = Constant.QUESTION_BASE_URL + "insert_chat_msg" + loginUserId + "/" + userId + "/" + strMessage;
            Log.d("URL", "" + sendmessage);
            return Utils.getResponseofGet(sendmessage);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("POST EXECUTE", "" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(ChatActivity.this, "Message Sent Successfully....", Toast.LENGTH_SHORT).show();
                   /* MessageDetails details = new MessageDetails();
                    details.setRecipient(user_id_img);
                    details.setSender(user_id);
                    details.setName("archi");
                    details.setSubject("Archirayan");
                    details.setText(msgEdt.getText().toString());
                    listmsgdetails.add(details);*/
                    edMessage.setText("");
                    chatAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ChatActivity.this, "Message Sent Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
