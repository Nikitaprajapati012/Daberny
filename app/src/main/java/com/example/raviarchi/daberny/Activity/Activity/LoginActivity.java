package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    public TextView txtRegHere,txtHeaderTitle;
    public EditText edemail, edpassword;
    public Utils utils;
    public String email, password,saveLogin;
    public LoginButton btnFbLogin;
    public CallbackManager callbackManager;
    public LinearLayout layoutFacebookLogin,layoutLogin;
    public RelativeLayout headerview;
    public ImageView imgIcon;
    public CheckBox ckRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        hashKey();
        init();
        findViewId();
        click();
    }

    private void hashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.raviarchi.daberny",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

//                ((TextView) findViewById(R.id.package_name)).setText(info.packageName);
                Log.e("HASHKEY", "" + Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    // TODO: 2/21/2017  initialization
    private void init() {
        utils = new Utils(LoginActivity.this);
    }

    // TODO: 2/21/2017 click perform
    private void click() {
        txtRegHere.setOnClickListener(this);
        layoutLogin.setOnClickListener(this);
        layoutFacebookLogin.setOnClickListener(this);
    }

    // TODO: 2/21/2017 bind data
    private void findViewId() {
        headerview = (RelativeLayout) findViewById(R.id.headerview);
        imgIcon = (ImageView) findViewById(R.id.header_icon);
        txtHeaderTitle = (TextView) findViewById(R.id.header_title);
        txtRegHere = (TextView) findViewById(R.id.activity_login_txtsignuphere);
        edemail = (EditText) findViewById(R.id.actvity_login_edituname);
        edpassword = (EditText) findViewById(R.id.actvity_login_edpassword);
        layoutLogin = (LinearLayout) findViewById(R.id.activity_login_layoutsignin);
        layoutFacebookLogin = (LinearLayout) findViewById(R.id.activity_login_layoutsigninwithfb);
        ckRememberMe = (CheckBox)findViewById(R.id.activity_login_ckrememberme);
        callbackManager = CallbackManager.Factory.create();
        btnFbLogin = (LoginButton) findViewById(R.id.btnfblogin);
        btnFbLogin.setReadPermissions("public_profile");
        btnFbLogin.setReadPermissions("email");
        headerview = (RelativeLayout) findViewById(R.id.headerview);
        imgIcon = (ImageView) findViewById(R.id.header_icon);
        txtHeaderTitle = (TextView) findViewById(R.id.header_title);
        imgIcon.setVisibility(View.INVISIBLE);
        txtHeaderTitle.setText(R.string.signup);
        saveLogin = Utils.ReadSharePref(LoginActivity.this,Constant.REMEMBER_ME);
        if (!saveLogin.equalsIgnoreCase("")) {
            edemail.setText(Utils.ReadSharePref(LoginActivity.this,Constant.USER_EMAIL));
            edpassword.setText(Utils.ReadSharePref(LoginActivity.this,Constant.USER_PASSWORD));
            ckRememberMe.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layoutLogin) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edemail.getWindowToken(), 0);
            email = edemail.getText().toString().trim();
            password = edpassword.getText().toString().trim();

            if (ckRememberMe.isChecked()) {
                Utils.WriteSharePrefrence(LoginActivity.this,Constant.REMEMBER_ME,"saveLogin");
                Utils.WriteSharePrefrence(LoginActivity.this,Constant.USER_USERNAME,email);
                Utils.WriteSharePrefrence(LoginActivity.this,Constant.USER_PASSWORD,password);
            }
            doSomethingElse();
        }
        switch (v.getId()) {
            case R.id.activity_login_layoutsigninwithfb:
                //btnFbLogin.performClick();
                break;

            case R.id.activity_login_txtsignuphere:
                Intent ireg = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(ireg);
                break;

            case R.id.activity_login_layoutsignin:
                // TODO: 2/21/2017 get user entered details

                if (!email.equalsIgnoreCase("")) {
                    if (!password.equalsIgnoreCase("")) {
                        new SignIn(email, password).execute();
                    } else {
                        Toast.makeText(this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LOGIN_RESULT", "" + loginResult);
                Toast.makeText(LoginActivity.this, "Facebook Login", Toast.LENGTH_SHORT).show();
              /*  GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoginActivity Response ", response.toString() + " -- " + object.toString());
                        try {
                            FbName = object.getString("name");
                            Fbemail = object.getString("email");
                            FbId = object.getString("id");

                          //  new GetLogin().execute();

                            Log.d("Data", FbId + " -- " + Fbemail + " -- " + FbName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Dat " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void doSomethingElse() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    /*private class GetFacebookLogin extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("Response", s);

            JSONObject object = null;
            try {
                object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = object.getJSONObject("data");
                   // Utils.WriteSharePrefrence(LoginActivity.this, Constant.USERID, userObject.getString("id"));
                  //  Utils.WriteSharePrefrence(LoginActivity.this, Constant.ISFACEBOOK, "1");
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                   *//* in.putExtra("id", userObject.getString("id"));*//*
                    startActivity(in);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap map = new HashMap();
            //   map.put("email", Fbemail);
            //   map.put("token_id", FbId);
            //  map.put("full_name", FbName);

            // return utils.getResponseofPost(Constant.BASE_URL + "login_with_facebook.php", map);

            return null;
        }
    }*/


    // TODO: 2/21/2017 make user login
    private class SignIn extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String email, password;

        public SignIn(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/login/archirayan40%40gmail.com/archirayan40
            try {
                return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "login/" + URLEncoder.encode(email, "UTF-8") + "/" + password);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("RESPONSE" + "LOGIN", "" + s);

                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonSecondOnject = jsonObject.getJSONObject("user_details");
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.USERID, jsonSecondOnject.getString("id"));
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.USER_EMAIL, jsonSecondOnject.getString("email"));
                    //String interest = Utils.ReadSharePrefrence(LoginActivity.this, Constant.INTERESTID);
                    //if (interest.length() > 0) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    /*} else {
                        Intent i = new Intent(LoginActivity.this, InterestActivity.class);
                        startActivity(i);
                        finish();
                    }*/
                } else {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
}

