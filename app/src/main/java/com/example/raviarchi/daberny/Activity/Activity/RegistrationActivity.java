package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtSignIn,txtHeaderTitle;
    public EditText edUserName, edFullName, edEmail, edPassword;
    public String UserName, FullName, Email, Password, Country;
    public Utils utils;
    public Spinner spinnerCountry;
    public ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> arrayCountryList;
    private ArrayList<String> arrayCountryIDList;
    public LinearLayout layoutSignUp;
    public RelativeLayout headerview;
    public ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        findViewId();
        click();
    }

    // TODO: 2/21/2017 initialization
    private void init() {
        utils = new Utils(RegistrationActivity.this);
        new GetCountryList().execute();
    }

    // TODO: 2/21/2017 bind data
    private void findViewId() {
        layoutSignUp = (LinearLayout) findViewById(R.id.activity_register_layoutsignup);
        txtSignIn = (TextView) findViewById(R.id.activity_registration_txtsignin);
        edFullName = (EditText) findViewById(R.id.activity_register_edfullname);
        edUserName = (EditText) findViewById(R.id.activity_register_edusername);
        edEmail = (EditText) findViewById(R.id.activity_register_edemail);
        edPassword = (EditText) findViewById(R.id.activity_register_edpassword);
        spinnerCountry = (Spinner) findViewById(R.id.activity_register_spinnercountry);
        spinnerCountry.setPrompt("Select Country");
        headerview = (RelativeLayout) findViewById(R.id.headerview);
        imgIcon = (ImageView) findViewById(R.id.header_icon);
        txtHeaderTitle = (TextView) findViewById(R.id.header_title);
        imgIcon.setVisibility(View.INVISIBLE);
        txtHeaderTitle.setText(R.string.signup);
    }

    // TODO: 2/21/2017 click perform
    private void click() {
        layoutSignUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int sel;
                if (position > 0) {
                    sel = (int) spinnerCountry.getItemIdAtPosition(position);
                    Country = arrayCountryIDList.get(sel);
                } else {
                    sel = (int) spinnerCountry.getItemIdAtPosition(position);
                    Country = arrayCountryIDList.get(sel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_registration_txtsignin:
                Intent iSign = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(iSign);
                break;

            case R.id.activity_register_layoutsignup:

                // TODO: 2/21/2017 get user entered details
                FullName = edFullName.getText().toString().trim();
                UserName = edUserName.getText().toString().trim();
                Email = edEmail.getText().toString().trim().trim();
                Password = edPassword.getText().toString().trim();

                if (!FullName.equalsIgnoreCase("")) {
                    if (!UserName.equalsIgnoreCase("")) {
                        if (!Email.equalsIgnoreCase("")) {
                            if (!Password.equalsIgnoreCase("")) {
                                if (Country.length() > 0) {
                                    new SignUp(FullName, UserName, Email, Password, Country).execute();
                                } else {
                                    Toast.makeText(this, "Please Country", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    // TODO: 2/21/2017 make user register 
    private class SignUp extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String fullname, username, email, password, country;

        public SignUp(String fullName, String userName, String email, String password, String country) {
            this.fullname = fullName;
            this.username = userName;
            this.email = email;
            this.password = password;
            this.country = country;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegistrationActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/register/rahul/rahul77/rssweqsnidhi%40gmail.com/srnidhi/4
            try {
                return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "register/" + fullname + "/" + username
                        + "/" + URLEncoder.encode(email, "UTF-8") + "/" + password + "/" + country);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "REGISTER" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    String id = jsonObject.getString("id");
                    String email = jsonObject.getString("email");
                    //utils.setSharedPrefrenceIsFirstTime(true);
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(RegistrationActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayCountryList = new ArrayList<>();
            arrayCountryIDList = new ArrayList<>();
            pd = new ProgressDialog(RegistrationActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/country_list/
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "country_list/");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Country List..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(RegistrationActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    JSONArray userArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayCountryList.add(interestObject.getString("country_name"));
                        arrayCountryIDList.add(interestObject.getString("country_id"));
                    }
                }
                else
                {
                    Toast.makeText(RegistrationActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayCountryList.size() > 0) {
                // TODO: 2/27/2017 for spinner data of interest
                spinnerAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, arrayCountryList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCountry.setAdapter(spinnerAdapter);

            } else {
                Toast.makeText(RegistrationActivity.this, "No Country Found, Please Try Again.", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
