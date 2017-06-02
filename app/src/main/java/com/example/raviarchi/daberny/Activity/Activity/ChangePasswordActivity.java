package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Fragment.EditUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.example.raviarchi.daberny.Activity.Utils.DbBitmapUtility.getBytes;

/**
 * Created by Ravi archi on 1/10/2017.
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AskQuestion";
    public Utils utils;
    public UserProfileDetails details;
    public String oldPassword, newPassword, confirmNewPassword;
    public String ID;
    public ArrayList<UserProfileDetails> arrayList;
    @BindView(R.id.activity_change_password_btnsubmit)
    Button btnSubmit;
    @BindView(R.id.activity_change_password_edoldpwd)
    EditText edOldPassword;
    @BindView(R.id.activity_change_password_ednewpwd)
    EditText edNewPassword;
    @BindView(R.id.activity_change_password_edconfirmpwd)
    EditText edConfirmNewPassword;
    @BindView(R.id.headerview_edit)
    RelativeLayout layoutHeader;
    @BindView(R.id.header_icon)
    ImageView imgBack;
    @BindView(R.id.header_title)
    TextView txtTitle;
    public RelativeLayout headerView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        txtTitle.setText(R.string.changethepwd);
        imgBack.setVisibility(View.VISIBLE);
        init();
        click();
    }


    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(ChangePasswordActivity.this);
        ID = Utils.ReadSharePrefrence(ChangePasswordActivity.this,Constant.USERID);
        if (getIntent().getExtras() != null){
            String strDetails = getIntent().getExtras().getString("userprofiledetails");
            Gson gson = new Gson();
            details = gson.fromJson(strDetails,UserProfileDetails.class);
        }
    }

    private void click() {
        imgBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_change_password_btnsubmit:
                ChangePasswordDetails();
                break;
            case R.id.header_icon:
                ChangePasswordActivity.this.onBackPressed();
                break;
        }
    }

    // TODO: 2/27/2017 enter the password details
    private void ChangePasswordDetails() {
        // TODO: 2/27/2017 for edittext data
        oldPassword = edOldPassword.getText().toString().trim();
        newPassword = edNewPassword.getText().toString().trim();
        confirmNewPassword = edConfirmNewPassword.getText().toString().trim();

        if (oldPassword.length() > 0) {
            if (newPassword.length() > 0) {
                if (confirmNewPassword.length() > 0) {
                    if (newPassword.equalsIgnoreCase(confirmNewPassword)){
                        new ChangePasswordDetails(ID,oldPassword, newPassword, confirmNewPassword).execute();
                    }else {
                        Toast.makeText(ChangePasswordActivity.this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();                    }

                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter Confirm New Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "Please Enter Old Password", Toast.LENGTH_SHORT).show();
        }
    }




    private class ChangePasswordDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String userid, Question, oldPassword, newPassword, confirmNewPassword, Option4, PicturePath, Interest, CurrentDate;
        String Time;

        public ChangePasswordDetails(String id,String oldPassword, String newPassword, String confirmNewPassword) {
            this.userid = id;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.confirmNewPassword = confirmNewPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(ChangePasswordActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/reset_password/889/123456/archi1/archi1
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "reset_password"+ "/" +userid + "/" +oldPassword+ "/" +newPassword+ "/" +confirmNewPassword);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Change Password Details..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    edOldPassword.setText("");
                    edNewPassword.setText("");
                    edConfirmNewPassword.setText("");
                    finish();

                } else {
                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


