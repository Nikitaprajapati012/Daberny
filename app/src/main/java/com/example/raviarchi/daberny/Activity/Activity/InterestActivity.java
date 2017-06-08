package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiplespinner.MultiSelectionSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterestActivity extends AppCompatActivity implements View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    public TextView txtBack, txtSave;
    public Utils utils;
    public String ID, InterestId, InterestName;
    public Object[] getinterestidspinner;
    public ArrayList<String> interestID;
    public ArrayList<UserProfileDetails> arrayList;
    @BindView(R.id.activity_interest_spinnerInterest)
    MultiSelectionSpinner spinnerInterest;
    private ArrayList<String> arrayInterestIDList;
    private ArrayList<String> arrayInterestNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        ButterKnife.bind(this);
        init();
        new GetInterest().execute();
        findViewId();
        click();
    }

    // TODO: 2/21/2017 initilization

    private void init() {
        utils = new Utils(InterestActivity.this);
        ID = Utils.ReadSharePrefrence(this, Constant.USERID);
    }

    private void click() {
        txtBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        spinnerInterest.setListener(this);
    }

    private void findViewId() {
        txtBack = (TextView) findViewById(R.id.activity_interest_txtback);
        txtSave = (TextView) findViewById(R.id.activity_interest_txtsave);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_interest_txtback:
                Intent iback = new Intent(InterestActivity.this, LoginActivity.class);
                startActivity(iback);
                break;

            case R.id.activity_interest_txtsave:
                //Utils.ClearaSharePrefrence(InterestActivity.this);
                InterestId = "";
                for (int i = 0; i < interestID.size(); i++) {
                    if (InterestId.length() > 0) {
                        InterestId = InterestId + "," + interestID.get(i);
                    } else {
                        InterestId = interestID.get(i);
                    }
                }
                Log.d("interest_id", InterestId);

                // Toast.makeText(this, "interest id =" + InterestId + "\n" + "interest name" + InterestName, Toast.LENGTH_SHORT).show();
                if (InterestId.length() > 0) {
                    new StoreInterest(InterestId).execute();
                    Intent isave = new Intent(InterestActivity.this, MainActivity.class);
                    Utils.WriteSharePrefrence(InterestActivity.this, Constant.INTERESTID, InterestId);
                    Log.d("interest_write", InterestId);
                    startActivity(isave);
                } else {
                  //  Toast.makeText(this, "Interest not Stored,Please try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
        interestID = new ArrayList<>();
        getinterestidspinner = indices.toArray();

        for (int i = 0; i < getinterestidspinner.length; i++) {
            interestID.add(arrayInterestIDList.get((Integer) getinterestidspinner[i]));
        }
    }


    @Override
    public void selectedStrings(List<String> strings) {
        InterestName = strings.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        Log.d("interest_name ", "string=" + InterestName);
    }


    // TODO: 2/21/2017 get list of Interest from URL
    private class GetInterest extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayInterestIDList = new ArrayList<>();
            arrayInterestNameList = new ArrayList<>();
            pd = new ProgressDialog(InterestActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://hire-people.com/host2/surveys/api/interests
            return Utils.getResponseofGet(Constant.QUESTION_BASE_URL + "interests");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Interest..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = jsonObject.getJSONObject("user_intrests");
                    JSONArray userArray = userObject.getJSONArray("interests");
                    for (int i = 1; i < userArray.length(); i++) {
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayInterestNameList.add(interestObject.getString("name"));
                        arrayInterestIDList.add(interestObject.getString("id"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayInterestNameList.size() > 0) {
                spinnerInterest.setItems(arrayInterestNameList);

            } else {
                Toast.makeText(InterestActivity.this, "Please select interest", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class StoreInterest extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String interestid;

        public StoreInterest(String interestId) {
            this.interestid = interestId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(InterestActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://181.224.157.105/~hirepeop/host2/surveys/api/add_with_interest/815/1,2,3
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "add_with_interest/" + ID + "/" + InterestId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Interest Stored..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray userArray = jsonObject.getJSONArray("inserted_data");
                    for (int i = 0; i < userArray.length(); i++) {
                        arrayInterestIDList = new ArrayList<>();
                        arrayInterestNameList = new ArrayList<>();
                        UserProfileDetails details = new UserProfileDetails();
                        // arrayList = new ArrayList<>();
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayInterestIDList.add(interestObject.getString("id"));
                        arrayInterestNameList.add(interestObject.getString("name"));
                       /* details.setIntId(arrayInterestIDList);
                        arrayList.add(details);*/
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayInterestNameList.size() > 0) {
                spinnerInterest.setItems(arrayInterestNameList);

            } else {
               // Toast.makeText(InterestActivity.this, "No Interest Stored, Please Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


