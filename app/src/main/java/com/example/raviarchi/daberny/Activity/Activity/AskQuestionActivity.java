package com.example.raviarchi.daberny.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

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

import static com.example.raviarchi.daberny.Activity.Utils.DbBitmapUtility.getBytes;

/**
 * Created by Ravi archi on 1/10/2017.
 */
public class AskQuestionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "AskQuestion";
    public Utils utils;
    public UserProfileDetails details;
    public String Question, Option1, Option2, Option3, Option4, PicturePath, Time, Interest, CurrentDate;
    public String ID;
    //public long time;
    public byte[] inputData;
    public String imagePath;
    public ArrayAdapter<String> spinnerAdapter;
    public String[] spinnerTimeList;
    public ArrayList<UserProfileDetails> arrayList;
    /*@BindView(R.id.activity_ask_question_imgbrowse)
    ImageView imgBrowse;*/
    /*@BindView(R.id.activity_ask_question_txtback)
    TextView txtBack;*/
    @BindView(R.id.activity_ask_question_btnbrowse)
    Button btnBrowse;
    @BindView(R.id.activity_ask_question_btnsubmit)
    Button btnSubmit;
    @BindView(R.id.activity_ask_question_edoption4)
    EditText edOption4;
    @BindView(R.id.activity_ask_question_edoption3)
    EditText edOption3;
    @BindView(R.id.activity_ask_question_edoption2)
    EditText edOption2;
    @BindView(R.id.activity_ask_question_edoption1)
    EditText edOption1;
    @BindView(R.id.activity_ask_question_edquestion)
    TextView txtQuetion;
    @BindView(R.id.activity_ask_question_timespinner)
    Spinner spinnerTime;
    @BindView(R.id.activity_ask_question_spinnerinterest)
    Spinner spinnerIntererst;
    private ArrayList<String> arrayInterestList;
    private ArrayList<String> arrayInterestIdList;
    private int SELECT_FILE = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ask_quetion);
        ButterKnife.bind(this);
        init();
        new GetInterest().execute();
        click();
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        utils = new Utils(AskQuestionActivity.this);
        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
        }
    }

    private void click() {
        btnBrowse.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        spinnerTime.setOnItemSelectedListener(this);
        spinnerIntererst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //  Interest = spinnerIntererst.getItemAtPosition(position).toString();
                int sel;
                if (position > 0) {
                    sel = (int) spinnerIntererst.getItemIdAtPosition(position);

                } else {
                    sel = (int) spinnerIntererst.getItemIdAtPosition(position);
                    //Interest = arrayInterestIdList.get(sel);
                }
                Interest = arrayInterestIdList.get(sel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_ask_question_btnsubmit:
                postQuestionDetails();
                break;
//            case R.id.activity_ask_question_txtback:
//                onBackPressed();
//                break;

            case R.id.activity_ask_question_btnbrowse:
                Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentPickImage, SELECT_FILE);
                break;
        }
    }

    // TODO: 2/27/2017 enter the question details
    private void postQuestionDetails() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        CurrentDate = df.format(c.getTime());

        // TODO: 2/27/2017 for edittext data
        Question = txtQuetion.getText().toString().trim();
        Option1 = edOption1.getText().toString().trim();
        Option2 = edOption2.getText().toString().trim();
        Option3 = edOption3.getText().toString().trim();
        Option4 = edOption4.getText().toString().trim();

        // TODO: 2/27/2017 for spinner data of time
        spinnerTimeList = getResources().getStringArray(R.array.time);

        if (Question.length() > 0) {
            if (Option1.length() > 0) {
                if (Option2.length() > 0) {
                    if (Interest.length() > 0) {
                        if (Time.length() > 0) {
                            new AddAskQuestionDetails(ID, CurrentDate, Question, Option1, Option2, Option3, Option4, PicturePath, Interest, Time).execute();
                        } else {
                            Toast.makeText(this, "Please Select Time", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please Select Interest", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter Option2", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Option1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Enter Question", Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("imagecompress","" +encodedImage);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imgBrowse.setImageBitmap(bitmap);
                onSelectFromGalleryResult(data);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

                if (saveImageInDB(uri)) {
                    Toast.makeText(this, "GalleryImage Saved in Database...", Toast.LENGTH_SHORT).show();
                    //imgBrowse.setVisibility(View.VISIBLE);
                    //imgBrowse.setImageURI(uri);
                }
                imagePath = cursor.getString(columnIndex);
                if (columnIndex < 0) // no column index
                    return; // DO YOUR ERROR HANDLING

                String image = getStringImage(bitmap);
                PicturePath = cursor.getString(columnIndex);

                cursor.close(); // close cursor
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean saveImageInDB(Uri selectedImageUri) {
        try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            inputData = getBytes(iStream);
            Log.d("save", "" + inputData);
            return true;
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Time = spinnerTime.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    // TODO: 2/21/2017 get list of Interest from URL
    private class GetInterest extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayInterestList = new ArrayList<>();
            arrayInterestIdList = new ArrayList<>();
            pd = new ProgressDialog(AskQuestionActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://hire-people.com/host2/surveys/api/interests
            return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "interests");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Ask Que Interest..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = jsonObject.getJSONObject("user_intrests");
                    JSONArray userArray = userObject.getJSONArray("interests");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayInterestList.add(interestObject.getString("name"));
                        arrayInterestIdList.add(interestObject.getString("id"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayInterestList.size() > 0) {
                // TODO: 2/27/2017 for spinner data of interest
                spinnerAdapter = new ArrayAdapter<String>(AskQuestionActivity.this, android.R.layout.simple_spinner_item, arrayInterestList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIntererst.setAdapter(spinnerAdapter);

            } else {
                Toast.makeText(AskQuestionActivity.this, "No Interest Found, Please Try Again.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AddAskQuestionDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String userid, Question, Option1, Option2, Option3, Option4, PicturePath, Interest, CurrentDate;
        String Time;

        public AddAskQuestionDetails(String id, String currentdate, String question, String option1, String option2, String option3, String option4, String picturePath, String interest, String time) {
            this.userid = id;
            this.CurrentDate = currentdate;
            this.Question = question;
            this.Option1 = option1;
            this.Option2 = option2;
            this.Option3 = option3;
            this.Option4 = option4;
            this.PicturePath = picturePath;
            this.Interest = interest;
            this.Time = time;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(AskQuestionActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://hire-people.com/host2/surveys/api/add_questions/669/hello/df/fd/fdf/fd/imagesss1.png/1/03:25:05/
            HashMap<String, String> hashMap = new HashMap<>();
            try {
                hashMap.put("title", URLEncoder.encode(Question, "utf-8"));
                hashMap.put("option1", URLEncoder.encode(Option1, "utf-8"));
                hashMap.put("option2", URLEncoder.encode(Option2, "utf-8"));
                hashMap.put("option3", URLEncoder.encode(Option3, "utf-8"));
                hashMap.put("option4", URLEncoder.encode(Option4, "utf-8"));
                hashMap.put("timing", URLEncoder.encode(Time, "utf-8"));
                hashMap.put("in_id", URLEncoder.encode(Interest, "utf-8"));
                hashMap.put("user_id", userid);
                if (PicturePath != null) {
                    hashMap.put("picture", URLEncoder.encode(PicturePath, "utf-8"));
                }
                hashMap.put("post_date", URLEncoder.encode(CurrentDate, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return utils.getResponseofPost(Constant.QUESTION_BASE_URL + "add_questions", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE", "Add Question Details..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(AskQuestionActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AskQuestionActivity.this, "No Question Details Post, Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*Fragment fragment = null;
            fragment = new Posts();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString("userprofiledetails", gson.toJson(details));
            if (fragment != null) {
                fragment.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_contain_layout, fragment);
                transaction.commit();
            }*/
        }

    }
}


