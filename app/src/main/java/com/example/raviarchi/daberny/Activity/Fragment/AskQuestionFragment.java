package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Activity.K4LVideoTrimmer;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.BitUtility;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.facebook.FacebookSdk.getApplicationContext;

/*** Created by Ravi archi on 1/10/2017.
 */

public class AskQuestionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "AskQuestion";
    public Utils utils;
    public UserProfileDetails details;
    public String userChoosenTask, Question, Tags, Option1, Option2, Option3, Option4, PicturePath, Time, Interest, CurrentDate;
    public String userId;
    public byte[] inputData;
    public ArrayAdapter<String> spinnerAdapter;
    public String[] spinnerTimeList;
    public ArrayList<UserProfileDetails> arrayList;
    public Toolbar toolBar;
    public TextView txtTitle;
    public Uri uri, videoUri;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_VIDEO_FILE = 2, REQUEST_CAMERA_VIDEO = 3;
    @BindView(R.id.activity_ask_question_txtfilepath)
    TextView txtFilePath;
    @BindView(R.id.activity_ask_question_btncamera)
    ImageView imgCamera;
    @BindView(R.id.activity_ask_question_btnvideo)
    ImageView imgVideo;
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
    @BindView(R.id.activity_ask_question_edtags)
    EditText edTags;
    @BindView(R.id.activity_ask_question_edquestion)
    EditText edQuetion;
    @BindView(R.id.activity_ask_question_timespinner)
    Spinner spinnerTime;
    @BindView(R.id.activity_ask_question_spinnerinterest)
    Spinner spinnerIntererst;
    private ArrayList<String> arrayInterestList;
    private ArrayList<String> arrayInterestIdList;
    private Bitmap bitmap;
    public  boolean result;
    public K4LVideoTrimmer videoTrimmer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_ask_quetion, container, false);
        ButterKnife.bind(this, view);
                init();
        new GetInterest().execute();
        click();
        return view;
    }

    // TODO: 2/21/2017 initilization
    private void init() {
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        txtTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        txtTitle.setText(R.string.question);
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
        result = Boolean.parseBoolean(Utils.ReadSharePref(getActivity(), Constant.PERMISSION));

    }

    private void click() {
        imgCamera.setOnClickListener(this);
        imgVideo.setOnClickListener(this);
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

            case R.id.activity_ask_question_btncamera:
                openDialogPhotos();
                break;
            case R.id.activity_ask_question_btnvideo:
                openDialogVideos();
                break;
        }
    }

    // TODO: 5/31/2017  select video from camera and gallery
    private void openDialogVideos() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result)
                        cameraIntentVideos();
                } else
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntentVideos();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    // TODO: 4/13/2017 choose from camera for video
    private void cameraIntentVideos() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "myvideo.mp4";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, fileName);
            videoUri = getActivity().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    values);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(takeVideoIntent,REQUEST_CAMERA_VIDEO);
        }
    }
    private void galleryIntentVideos() {
        Intent intentPickVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickVideo, SELECT_VIDEO_FILE);
    }

    // TODO: 5/31/2017 select image from camera and gallery
    private void openDialogPhotos() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_CAMERA_VIDEO) {
                onCaptureVideoResult(data);
            } else if (requestCode == SELECT_VIDEO_FILE) {
                onSelectVideoFromGalleryResult(data);
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 5/31/2017 video from gallery
    private void onSelectVideoFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                if (cursor == null || cursor.getCount() < 1) {
                    return; // no cursor or no record. DO YOUR ERROR HANDLING
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                PicturePath = cursor.getString(columnIndex);
                cursor.close(); // close cursor
                if (PicturePath != null) {
                  MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(PicturePath));
                   int duration = mp.getDuration();
                   mp.release();
                   if ((duration / 1000) > 30) {
                       Log.d("##","duration above 30");
                       videoTrimmer = ((K4LVideoTrimmer) getActivity().findViewById(R.id.timeLine));
                        if (videoTrimmer != null) {
                         videoTrimmer.setVideoURI(Uri.parse(PicturePath));
                         //videoTrimmer.setMaxDuration(30);
                            Toast.makeText(getActivity(), "size limit 30", Toast.LENGTH_SHORT).show();
                         Log.d("##","above 30");
                     }
                    } else {
                       Log.d("##","below 30");
                       String filename=PicturePath.substring(PicturePath.lastIndexOf("/")+1);
                        txtFilePath.setText(filename);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }
    // TODO: 5/31/2017 capture video
    public void onCaptureVideoResult(Intent data) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().managedQuery(videoUri, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        PicturePath = cursor.getString(column_index_data);
        String filename=PicturePath.substring(PicturePath.lastIndexOf("/")+1);
        txtFilePath.setText(filename);
    }

    // TODO: 5/31/2017 image from gallery
    public void onCaptureImageResult(Intent data) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null,
                null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        PicturePath = cursor.getString(column_index_data);
        String filename=PicturePath.substring(PicturePath.lastIndexOf("/")+1);
        txtFilePath.setText(filename);
    }

    // TODO: 4/13/2017 choose from gallery for image
    private void galleryIntent() {
        Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickImage, SELECT_FILE);
    }

    // TODO: 4/13/2017 choose from camera for image
    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    // TODO: 2/27/2017 enter the question details
    private void postQuestionDetails() {
              // TODO: 2/27/2017 for edittext data
        Question = edQuetion.getText().toString().trim();
        Tags = edTags.getText().toString().trim();
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
                        if (Time.length() > 1) {
                            AddAskQuestionDetails(userId,Question, Tags, Option1, Option2, Option3, Option4, PicturePath, Interest, Time);
                        } else {
                            Toast.makeText(getActivity(), "Please Select Time", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Select Interest", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Option2", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Enter Option1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please Enter Question", Toast.LENGTH_SHORT).show();
        }
    }
   // TODO: 4/22/2017 store the details of question
           private void AddAskQuestionDetails(String userId,String question, String tags, String option1, String option2, String option3, String option4, String picturePath, String interest, String time) {
             Log.d("IMAGE","@@@@"+picturePath);
             Log.d("interest","@@@@"+interest);
               //http://hire-people.com/host2/surveys/api/add_questions/669/hello/df/fd/fdf/fd/imagesss1.png/1/03:25:05/
               try {
                   Ion.with(getContext())
                           .load(Constant.QUESTION_BASE_URL + "add_questions")
                           .setMultipartParameter("user_id", userId)
                           .setMultipartParameter("title", URLEncoder.encode(question, "utf-8"))
                           .setMultipartParameter("tags", URLEncoder.encode(tags, "utf-8"))
                           .setMultipartParameter("option1", URLEncoder.encode(option1, "utf-8"))
                           .setMultipartParameter("option2", URLEncoder.encode(option2, "utf-8"))
                           .setMultipartParameter("option3", URLEncoder.encode(option3, "utf-8"))
                           .setMultipartParameter("option4", URLEncoder.encode(option4, "utf-8"))
                           .setMultipartParameter("timing", time)
                           .setMultipartParameter("in_id", interest)
                           .setMultipartFile("picture", new File(picturePath))
                           .asString()
                           .setCallback(new FutureCallback<String>() {
                               @Override
                               public void onCompleted(Exception e, String result) {
                                   Log.d("JSONRESULT","@@@@"+result);
                                   if (result != null && result.length() > 0) {
                                       try {
                                           JSONObject jsonObject= new JSONObject(result);
                                           if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                                               Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                               JSONArray queArray = jsonObject.getJSONArray("inserted_data");
                                               if (queArray.length() > 0 ) {
                                                   for (int i = 0; i < queArray.length(); i++) {
                                                       JSONObject queObject = queArray.getJSONObject(i);
                                                       arrayList = new ArrayList<>();
                                                       details = new UserProfileDetails();
                                                       details.setUserId(queObject.getString("user_id"));
                                                       arrayList.add(details);
                                                   }
                                               }
                                               if (arrayList.size() > 0) {
                                                   Fragment fragment = new Posts();
                                                   Bundle bundle = new Bundle();
                                                   Gson gson = new Gson();
                                                   bundle.putString("userprofiledetails", gson.toJson(details));
                                                   fragment.setArguments(bundle);
                                                   FragmentManager fm = getActivity().getSupportFragmentManager();
                                                   FragmentTransaction transaction = fm.beginTransaction();
                                                   transaction.replace(R.id.frame_contain_layout, fragment);
                                                   transaction.commit();
                                               }
                                           } else {
                                               Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                           }

                                       } catch (JSONException e1) {
                                           e1.printStackTrace();
                                       }

                                   }
                               }
                           });


               } catch (UnsupportedEncodingException e1) {
                   e1.printStackTrace();
               }
           }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri,
                        filePathColumn, null, null, null);

                if (cursor == null || cursor.getCount() < 1) {
                    return; // no cursor or no record. DO YOUR ERROR HANDLING
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                PicturePath = cursor.getString(columnIndex);
                if (columnIndex < 0) // no column index
                    return; // DO YOUR ERROR HANDLING
                String image = getStringImage(bitmap);
                PicturePath = cursor.getString(columnIndex);
                String filename=PicturePath.substring(PicturePath.lastIndexOf("/")+1);
                txtFilePath.setText(filename);
                cursor.close(); // close cursor
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
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
            pd = new ProgressDialog(getActivity());
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
                spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayInterestList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIntererst.setAdapter(spinnerAdapter);
            } else {
                Toast.makeText(getActivity(), "No Interest Found, Please Try Again.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}


