package com.example.raviarchi.daberny.Activity.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    public static EditText edMessage;
    public static TextView txtSend;
    public ChatAdapter chatAdapter;
    public ArrayList<UserProfileDetails> arrayUserList;
    public UserProfileDetails details;
    public String strMsg, picturepath;
    public Uri uri;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public boolean result;
    @BindView(R.id.activity_chat_imggallery)
    ImageView imgGallery;
    @BindView(R.id.activity_chat_imglike)
    ImageView imgLike;
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
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        click();
    }


    // TODO: 5/29/2017 perform click event
    private void click() {
        txtSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edMessage.getText().toString().trim().length() > 0) {
                    layoutAttachment.setVisibility(View.GONE);
                    txtSend.setVisibility(View.VISIBLE);
                } else {
                    layoutAttachment.setVisibility(View.VISIBLE);
                    txtSend.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onSelectFromGalleryResult(data);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 6/2/2017 image pic from gallery
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                if (cursor == null || cursor.getCount() < 1) {
                    return; // no cursor or no record. DO YOUR ERROR HANDLING
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturepath = cursor.getString(columnIndex);
                if (columnIndex < 0) // no column index
                    return; // DO YOUR ERROR HANDLING
                picturepath = cursor.getString(columnIndex);
                cursor.close(); // close cursor
                // TODO: 6/2/2017 send image in chat
                if (picturepath != null) {
                    sendmessageImage(arrayUserList, loginUserId, receiverId, strMsg, picturepath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 5/29/2017 initilization
    private void init() {
        details = new UserProfileDetails();
        utils = new Utils(this);
        loginUserId = Utils.ReadSharePrefrence(this, Constant.USERID);
        edMessage = (EditText) findViewById(R.id.activity_chat_msg_edmsg);
        txtSend = (TextView) findViewById(R.id.activity_chat_txtsend);
        result = Boolean.parseBoolean(Utils.ReadSharePref(ChatActivity.this, Constant.PERMISSION));
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
        // TODO: 6/30/2017 get list of chat
        new GetChat(loginUserId, receiverId).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_chat_imggallery:
                if (result) {
                    galleryIntent();
                }
                break;

            case R.id.activity_chat_imglike:
                Utils.WriteSharePrefrence(ChatActivity.this, Constant.CHAT_LIKE, "1");
                Uri imageUri = null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sendheart), null, null));
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imageUri,
                            filePathColumn, null, null, null);
                    if (cursor == null || cursor.getCount() < 1) {
                        return; // no cursor or no record. DO YOUR ERROR HANDLING
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturepath = cursor.getString(columnIndex);
                    if (picturepath != null) {
                        sendmessageImage(arrayUserList, loginUserId, receiverId, strMsg, picturepath);
                    }
                } catch (NullPointerException e) {
                }
                break;

            case R.id.activity_chat_txtsend:
                String strMsg = edMessage.getText().toString().replaceAll(" ", "%20");
                if (strMsg.trim().length() > 0) {
                    new SendMessage(arrayUserList, loginUserId, receiverId, strMsg).execute();
                    edMessage.setText(" ");
                }
                break;


            case R.id.header_icon:
                onBackPressed();
                break;
        }
    }

    // TODO: 6/2/2017 image from gallery intent
    private void galleryIntent() {
        Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickImage, SELECT_FILE);

    }

    // TODO: 6/2/2017 send the message as well as image in chat
    private void sendmessageImage(ArrayList<UserProfileDetails> arrayList, final String loginUserId, final String receiverId, String strMsg, String picturepath) {
        //http://181.224.157.105/~hirepeop/host2/surveys/api/insert_chat_msg/677/669/hello
        if (picturepath != null) {
            arrayList = new ArrayList<>();
            final ArrayList<UserProfileDetails> finalArrayList = arrayList;
            Ion.with(this)
                    .load(Constant.QUESTION_BASE_URL + "insert_chat_msg")
                    .setMultipartParameter("sender_id", loginUserId)
                    .setMultipartParameter("receiver_id", receiverId)
                    .setMultipartFile("chat_picture", new File(picturepath))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.d("JSONRESULTChat", "#@@@@" + result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                                    JSONObject object = jsonObject.getJSONObject("inserted_data");
                                    details = new UserProfileDetails();
                                    details.setUserMsgType(object.getString("type"));
                                    details.setUserMessage(object.getString("msg"));
                                    // TODO: 6/5/2017 sender details
                                    JSONObject senderobject = object.getJSONObject("sender_detail");
                                    details.setUserId(senderobject.getString("id"));
                                    details.setUserUserName(senderobject.getString("username"));
                                    details.setUserImage(senderobject.getString("user_image"));
                                    // TODO: 6/5/2017 receiver details
                                    JSONObject receiverobject = object.getJSONObject("receiver_detail");
                                    details.setOtherUserId(receiverobject.getString("id"));
                                    details.setOtherUserName(receiverobject.getString("username"));
                                    details.setOtherUserImage(receiverobject.getString("user_image"));
                                    finalArrayList.add(details);
                                }
                                if (finalArrayList.size() > 0) {
                                    new GetChat(loginUserId, receiverId).execute();
                                    openChatDetailsList(finalArrayList);
                                }
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }

                        }
                    });
        }
    }

    // TODO: 5/30/2017 bind the list with adapter
    private void openChatDetailsList(ArrayList<UserProfileDetails> arrayList) {
        chatAdapter = new ChatAdapter(ChatActivity.this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.scrollToPosition(arrayList.size()-1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

    }

    private class GetChat extends AsyncTask<String, String, String> {
        String receiver_id, login_id;
        ProgressDialog pd;

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
            receivemessage = Constant.QUESTION_BASE_URL + "get_chat_msg"
                    + "/" + login_id + "/" + receiver_id;
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
                        details.setUserChatPostDate(followingObject.getString("post_date"));
                        details.setOtherUserId(followingObject.getString("recipient_id"));
                        details.setOtherUserFullName(followingObject.getString("recipient_fullname"));
                        details.setOtherUserName(followingObject.getString("recipient_username"));
                        details.setOtherUserImage(followingObject.getString("recipient_image"));
                        details.setUserMsgReceiver(followingObject.getString("receiver_msg"));
                        details.setOtherChatPostDate(followingObject.getString("post_date"));
                        details.setUserMsgSender(followingObject.getString("sender_msg"));
                        details.setUserMsgTime(followingObject.getString("time_distance"));
                        details.setUserMsgType(followingObject.getString("type"));
                        arrayUserList.add(details);
                    }
                    if (arrayUserList.size() > 0) {
                        openChatDetailsList(arrayUserList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 6/16/2017 send message in text
    private class SendMessage extends AsyncTask<String, String, String> {
        String strMessage, userId, receiver_Id;
        ArrayList<UserProfileDetails> arrayList;

        private SendMessage(ArrayList<UserProfileDetails> arrayUserList, String user_id, String receipt_id, String strMsg) {
            this.userId = user_id;
            this.receiver_Id = receipt_id;
            this.strMessage = strMsg;
            this.arrayList = arrayUserList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/insert_chat_msg/677/669/hello
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("sender_id", loginUserId);
            hashMap.put("receiver_id", receiver_Id);
            hashMap.put("msg", strMessage);
            return Utils.getResponseofPost(Constant.QUESTION_BASE_URL + "insert_chat_msg/", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject object = jsonObject.getJSONObject("inserted_data");
                    details = new UserProfileDetails();
                    details.setUserMsgType(object.getString("type"));
                    details.setUserMessage(object.getString("msg"));

                    // TODO: 6/5/2017 sender details
                    JSONObject senderobject = object.getJSONObject("sender_detail");
                    details.setUserId(senderobject.getString("id"));
                    details.setUserUserName(senderobject.getString("username"));
                    details.setUserImage(senderobject.getString("user_image"));

                    // TODO: 6/5/2017 receiver details
                    JSONObject receiverobject = object.getJSONObject("receiver_detail");
                    details.setOtherUserId(receiverobject.getString("id"));
                    details.setOtherUserName(receiverobject.getString("username"));
                    details.setOtherUserImage(receiverobject.getString("user_image"));
                    arrayList.add(details);
                }
                if (arrayList.size() > 0) {
                    new GetChat(loginUserId, receiverId).execute();
                    openChatDetailsList(arrayList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
