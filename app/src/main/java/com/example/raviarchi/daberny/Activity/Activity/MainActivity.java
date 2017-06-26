package com.example.raviarchi.daberny.Activity.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Fragment.AddFriends;
import com.example.raviarchi.daberny.Activity.Fragment.AskQuestionFragment;
import com.example.raviarchi.daberny.Activity.Fragment.BlockedUsers;
import com.example.raviarchi.daberny.Activity.Fragment.General;
import com.example.raviarchi.daberny.Activity.Fragment.Home;
import com.example.raviarchi.daberny.Activity.Fragment.InboxUsers;
import com.example.raviarchi.daberny.Activity.Fragment.Notification;
import com.example.raviarchi.daberny.Activity.Fragment.Search;
import com.example.raviarchi.daberny.Activity.Fragment.UserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.BadgeDrawable;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

import static com.example.raviarchi.daberny.Activity.Utils.Utils.ReadSharePrefrence;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Utils utils;
    public RelativeLayout headerView, footerView;
    public ImageView imgUserAdd, imgInbox, imgOption, imgHome, imgGeneral, imgSearch, imgAdd, imgNotification, imgUserProfile/*, imgCamera*/;
    public FragmentManager fragmentManager;
    public FragmentTransaction transaction;
    public String userId,fbUserId;
    public Uri uri;
    public UserProfileDetails details;
    public ArrayList<UserProfileDetails> arrayInterestList;
    //private int REQUEST_CAMERA = 0;
    public Toolbar toolBar;
    public TextView txtTitle,txtNotificationCount;
    private boolean isChangedStat = false;
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2/21/2017 if user is login then redirect on relevant screen
        // TODO: 3/6/2017 utils intialize
        utils = new Utils(MainActivity.this);
        userId = ReadSharePrefrence(this, Constant.USERID);
        fbUserId = ReadSharePrefrence(this, Constant.FB_USER_ID);
        String interest = ReadSharePrefrence(this, Constant.USER_INTERESTS);
        if (userId.equalsIgnoreCase("") && fbUserId.equalsIgnoreCase("")) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
        } else {
            if (interest.length() < 0) {
                    Intent i = new Intent(MainActivity.this, InterestActivity.class);
                    startActivity(i);
                    utils.clearSharedPreferenceData();
            } else {
                //TODO: 2/22/2017 actvitity main start
                setContentView(R.layout.activity_main);
                findById();
                click();
            }
        }
    }

    // TODO: 2/23/2017 get id from previous account
    private void click() {
        //imgCamera.setOnClickListener(this);
        imgUserAdd.setOnClickListener(this);
        imgInbox.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        imgGeneral.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        imgNotification.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
        imgHome.performClick();
    }

    // TODO: 2/22/2017 bind field
    private void findById() {
        //bind the data
        headerView = (RelativeLayout) findViewById(R.id.mainview);
        footerView = (RelativeLayout) findViewById(R.id.footerview);
        imgHome = (ImageView) findViewById(R.id.footer_imghome);
        imgGeneral = (ImageView) findViewById(R.id.footer_imggeneral);
        //imgCamera = (ImageView) findViewById(R.id.header_camera);
        imgSearch = (ImageView) findViewById(R.id.footer_imgsearch);
        imgAdd = (ImageView) findViewById(R.id.footer_imgaddquestion);
        imgNotification = (ImageView) findViewById(R.id.footer_imgnotification);
        txtNotificationCount = (TextView) findViewById(R.id.footer_badge_notification_count);
        imgUserProfile = (ImageView) findViewById(R.id.footer_imguserprofile);
        footerView.setVisibility(View.VISIBLE);
        toolBar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolBar);
        imgUserAdd = (ImageView) findViewById(R.id.toolbar_useradd);
        imgInbox = (ImageView) findViewById(R.id.toolbar_inbox);
        txtTitle = (TextView) findViewById(R.id.toolbar_title);
        if(Utils.ReadSharePrefrence(MainActivity.this,Constant.NOTIFICATION) != null){
            if (Utils.ReadSharePrefrence(MainActivity.this,Constant.ISREAD).equalsIgnoreCase("unread"))
            txtNotificationCount.setVisibility(View.VISIBLE);
            txtNotificationCount.setText(Utils.ReadSharePrefrence(MainActivity.this,Constant.NOTIFICATION));
        }
        else{
            txtNotificationCount.setVisibility(View.GONE);
        }
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImage(data);

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void onCaptureImage(Intent data) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor =
                managedQuery(uri, projection, null,
                        null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String picturePath = cursor.getString(column_index_data);
        Log.d("imagepath", "" + picturePath);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
           /* case R.id.header_camera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    uri = getContentResolver()
                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                    takePictureIntent
                            .putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }
                break;
*/

            case R.id.toolbar_inbox:
                fragment = new InboxUsers();
                break;
            case R.id.toolbar_useradd:
                fragment = new AddFriends();
                break;

            //home
            case R.id.footer_imghome:
                headerView.setVisibility(View.VISIBLE);
                fragment = new Home();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                break;

            //general
            case R.id.footer_imggeneral:
                headerView.setVisibility(View.VISIBLE);
                fragment = new General();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                break;

            //add question
            case R.id.footer_imgaddquestion:
                headerView.setVisibility(View.VISIBLE);
                fragment = new AskQuestionFragment();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                break;

            //notification
            case R.id.footer_imgnotification:
                headerView.setVisibility(View.VISIBLE);
                fragment = new Notification();
                txtNotificationCount.setVisibility(View.GONE);
                Utils.WriteSharePrefrence(MainActivity.this,Constant.ISREAD,"read");
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                break;

            // search
            case R.id.footer_imgsearch:
                headerView.setVisibility(View.GONE);
                fragment = new Search();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                break;

            //profile
            case R.id.footer_imguserprofile:
                headerView.setVisibility(View.VISIBLE);
                fragment = new UserProfile();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.signinbg));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.choosefile));
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            utils.replaceFragment(fragment);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userprofilemenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //option menu
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.menu_item_userprofile:
                fragment = new UserProfile();
                break;

            case R.id.menu_item_blockeduser:
                fragment = new BlockedUsers();
                break;

            case R.id.menu_item_logout:
                Intent ilogout = new Intent(MainActivity.this, LoginActivity.class);
                ilogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Utils.ClearSharePref(MainActivity.this, Constant.USERID);
                Utils.ClearSharePref(MainActivity.this, Constant.FB_USER_ID);
                LoginManager.getInstance().logOut();
                startActivity(ilogout);
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
            utils.replaceFragment(fragment);
        }
        return super.onOptionsItemSelected(item);
    }
}

