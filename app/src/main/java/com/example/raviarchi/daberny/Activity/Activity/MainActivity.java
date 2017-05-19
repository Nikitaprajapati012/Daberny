package com.example.raviarchi.daberny.Activity.Activity;

import android.content.ContentValues;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Fragment.AddQuestions;
import com.example.raviarchi.daberny.Activity.Fragment.General;
import com.example.raviarchi.daberny.Activity.Fragment.Home;
import com.example.raviarchi.daberny.Activity.Fragment.Notification;
import com.example.raviarchi.daberny.Activity.Fragment.Search;
import com.example.raviarchi.daberny.Activity.Fragment.UserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import java.util.ArrayList;

import static com.example.raviarchi.daberny.Activity.Utils.Utils.ReadSharePrefrence;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Utils utils;
    public RelativeLayout headerView, footerView;
    public ImageView imgHome, imgGeneral, imgSearch, imgAdd, imgNotification, imgUserProfile, imgCamera;
    public FragmentManager fragmentManager;
    public FragmentTransaction transaction;
    public String Email, userId, InterestId;
    public Uri uri;
    public UserProfileDetails details;
    public ArrayList<UserProfileDetails> arrayInterestList;
    private int REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2/21/2017 if user is login then redirect on relevant screen
        // TODO: 3/6/2017 utils intialize
        utils = new Utils(MainActivity.this);
        userId = ReadSharePrefrence(this, Constant.USERID);
        String interest = ReadSharePrefrence(this, Constant.INTERESTID);
        Boolean isFirstTimeReg = utils.getReadSharedPrefrenceIsFirstTime();
        if (userId.equals("")) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            if (interest.length() < 0) {
                if (isFirstTimeReg.equals(true)) {
                    Intent i = new Intent(MainActivity.this, InterestActivity.class);
                    startActivity(i);
                    utils.clearSharedPreferenceData();
                    //   if (isFirstTimeReg.toString().length() > 0) {
                   /* } else {
                        Intent intent = new Intent(MainActivity.this, InterestActivity.class);
                        startActivity(intent);
                    }*/
                }
            } else {
                //TODO: 2/22/2017 actvitity main start
                setContentView(R.layout.activity_main);
                getSupportActionBar().hide();
                findById();
                click();
            }
        }
    }

    // TODO: 2/23/2017 get id from previous account

    private void click() {
        imgCamera.setOnClickListener(this);
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
        headerView = (RelativeLayout) findViewById(R.id.headerview);
        footerView = (RelativeLayout) findViewById(R.id.footerview);
        imgHome = (ImageView) findViewById(R.id.footer_imghome);
        imgGeneral = (ImageView) findViewById(R.id.footer_imggeneral);
        imgCamera = (ImageView) findViewById(R.id.header_camera);
        imgSearch = (ImageView) findViewById(R.id.footer_imgsearch);
        imgAdd = (ImageView) findViewById(R.id.footer_imgaddquestion);
        imgNotification = (ImageView) findViewById(R.id.footer_imgnotification);
        imgUserProfile = (ImageView) findViewById(R.id.footer_imguserprofile);
        footerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImage(data);

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }

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
        /*MyImage image = new MyImage();
        image.setTitle("Test");
        image.setDescription(
                "test take a photo and add it to list view");
        image.setDatetime(System.currentTimeMillis());
        image.setPath(picturePath);
        images.add(image);*/
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        fragmentManager = getSupportFragmentManager();

        switch (v.getId()) {
            case R.id.header_camera:
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

            //home
            case R.id.footer_imghome:
                headerView.setVisibility(View.VISIBLE);
                fragment = new Home();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                break;

            //general
            case R.id.footer_imggeneral:
                headerView.setVisibility(View.VISIBLE);
                fragment = new General();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                break;

            //add question
            case R.id.footer_imgaddquestion:
                headerView.setVisibility(View.VISIBLE);
                fragment = new AddQuestions();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                break;

            //notification
            case R.id.footer_imgnotification:
                headerView.setVisibility(View.VISIBLE);
                fragment = new Notification();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                break;

            // search
            case R.id.footer_imgsearch:
                headerView.setVisibility(View.GONE);
                fragment = new Search();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                break;

            //profile
            case R.id.footer_imguserprofile:
                headerView.setVisibility(View.GONE);
                fragment = new UserProfile();
                imgHome.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgGeneral.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgNotification.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.askque));
                imgUserProfile.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.choosefile));
                break;
        }

        if (fragment != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_contain_layout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}

