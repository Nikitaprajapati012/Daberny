package com.example.raviarchi.daberny.Activity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Activity.ChangePasswordActivity;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiplespinner.MultiSelectionSpinner;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.raviarchi.daberny.Activity.Utils.DbBitmapUtility.getBytes;
import static com.facebook.FacebookSdk.getApplicationContext;

/*** Created by Ravi archi on 1/10/2017.
 */

public class EditUserProfile extends Fragment implements View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    private static final String TAG = "EditUserProfile";
    public Utils utils;
    public UserProfileDetails details;
    public String ID,userChoosenTask, FullName, UserName, Email, Country, InterestId, ImagePath, InterestName, StrInterest;
    public Uri uri;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public ArrayAdapter<String> spinnerAdapter;
    public ArrayList<UserProfileDetails> arrayList;
    public Collection<String> enums;
    public ArrayList<String> arrayCountryList, arrayCountryIDList, arrayInterestList, arrayInterestIdList, interestID;
    public Object[] getinterestidspinner;
    public File file;
    public RelativeLayout headerView;
    public Toolbar toolBar;
    @BindView(R.id.fragment_edit_user_profile_edfullname)
    EditText edFullName;
    @BindView(R.id.fragment_edit_user_profile_edusername)
    EditText edUserName;
    @BindView(R.id.fragment_edit_user_profile_edemail)
    EditText edEmail;
    @BindView(R.id.fragment_edit_user_profile_imgprofilepic)
    ImageView imgProfilePic;
    @BindView(R.id.fragment_edit_user_profile_btnsubmit)
    Button btnSubmit;
    @BindView(R.id.fragment_edit_user_profile_spinnercountry)
    Spinner spinnerCountry;
    @BindView(R.id.fragment_edit_user_profile_spinnerinterest)
    MultiSelectionSpinner spinnerIntererst;
    @BindView(R.id.fragment_edit_user_profile_imgchangepic)
    ImageView imgChangePic;
    @BindView(R.id.fragment_edit_user_profile_txtchangepwd)
    TextView txtChangePassword;
    @BindView(R.id.headerview_edit)
    RelativeLayout layoutHeader;
    @BindView(R.id.header_icon)
    ImageView imgBack;
    @BindView(R.id.header_title)
    TextView txtTitle;
    private Bitmap bitmap;
    public  boolean result;

    public static String convertStreamToString(FileOutputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(is)));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.test_edit_user_profile, container, false);
        ButterKnife.bind(this, view);
        headerView = (RelativeLayout) getActivity().findViewById(R.id.mainview);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        headerView.setVisibility(View.GONE);
        txtTitle.setText(R.string.editprofile);
        imgBack.setVisibility(View.VISIBLE);
        init();
        click();
        return view;
    }

    // TODO: 2/21/2017 initialization
    private void init() {
        arrayInterestList = new ArrayList<>();
        arrayInterestIdList = new ArrayList<>();
        utils = new Utils(getActivity());
        result = Boolean.parseBoolean(Utils.ReadSharePref(getActivity(), Constant.PERMISSION));

        //TODO: 3/3/2017 get country list
        new GetCountryList().execute();

        // TODO: 3/3/2017 get interest list
        new GetInterest().execute();

        if (getArguments() != null) {
            // TODO: 3/6/2017 get using Gson
            Gson gson = new Gson();
            String strObj = getArguments().getString("userprofiledetails");
            details = gson.fromJson(strObj, UserProfileDetails.class);
            StrInterest = Utils.ReadSharePrefrence(getActivity(), Constant.USER_INTERESTID);
            //spinnerIntererst.setSelection(new String[]{StrInterest});
            // TODO: 3/3/2017  get data from previous screen
            ID = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
            FullName = details.getUserFullName();
            UserName = details.getUserUserName();
            Email = details.getUserEmail();
            Country = details.getUserCountryName();
            ImagePath = details.getUserImage();
            edFullName.setText(FullName);
            edUserName.setText(UserName);
            edEmail.setText(Email);
            arrayCountryList.add(Country);
            File imgFile = new File(ImagePath);
            Bitmap myBitmap = BitmapFactory.decodeFile(ImagePath);
            imgProfilePic.setImageBitmap(myBitmap);
            Picasso.with(getActivity()).load(ImagePath).
                    transform(new RoundedTransformation(120, 2)).
                    placeholder(R.drawable.ic_placeholder).into(imgProfilePic);

          /*  // TODO: 3/16/2017 get list from previous activity
            arrayInterestList = new ArrayList<String>(enums);

            if (arrayInterestList.size() > 0) {
                Toast.makeText(getActivity(), "" + arrayInterestList.size(), Toast.LENGTH_SHORT).show();
                // TODO: 3/14/2017 set the previous value in spinner for update
                //spinnerAdapter = (ArrayAdapter<String>) spinnerIntererst.getAdapter();
                spinnerIntererst.setSelection(arrayInterestList);
            }*/
        }
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

               ImagePath = cursor.getString(columnIndex);
                Log.d("image@@", ImagePath);
                if (columnIndex < 0) // no column index
                    return; // DO YOUR ERROR HANDLING
                String image = getStringImage(bitmap);
                cursor.close(); // close cursor
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("imagecompress", "" + encodedImage);
        return encodedImage;
    }


    private void click() {
        txtChangePassword.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgChangePic.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        spinnerIntererst.setListener(this);
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
            case R.id.fragment_edit_user_profile_imgchangepic:
                openDialogPhotos();
                break;

            case R.id.fragment_edit_user_profile_txtchangepwd:
                Gson gson = new Gson();
                Intent ichange = new Intent(getActivity(), ChangePasswordActivity.class);
                ichange.putExtra("userprofiledetails", gson.toJson(details));
                getActivity().startActivity(ichange);
                break;

            case R.id.header_icon:
                getActivity().onBackPressed();
                break;

            case R.id.fragment_edit_user_profile_btnsubmit:
                getUserUpdatedDetails();
                break;
        }
    }

    // TODO: 6/7/2017 choose the option from camera and gallery
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
                imgProfilePic.setImageBitmap(bitmap);
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // TODO: 5/31/2017 image from camera
    public void onCaptureImageResult(Intent data) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null,
                null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        ImagePath = cursor.getString(column_index_data);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfilePic.setImageBitmap(bitmap);
    }
    // TODO: 6/7/2017  choose from gallery for image
    private void galleryIntent() {
        Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickImage, SELECT_FILE);
    }

    // TODO: 6/7/2017 choose from camera for image
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
    private void getUserUpdatedDetails() {
        FullName = edFullName.getText().toString();
        UserName = edUserName.getText().toString();
        Email = edEmail.getText().toString();
        InterestId = "";
        for (int i = 0; i < interestID.size(); i++) {
            if (InterestId.length() > 0) {
                InterestId = InterestId + "," + interestID.get(i);
            } else {
                InterestId = interestID.get(i);
            }
        }
        Log.d("interest_id@@", "interest id =" + InterestId + "\n" + "interest name" + InterestName);
        // if (FullName.equalsIgnoreCase("")) {
        //   if (UserName.equalsIgnoreCase("")) {
        //  if (Email.equalsIgnoreCase("")) {
        //   if (Country.length() > 0) {
        if (InterestId.length() > 0) {
            try {
                Ion.with(getContext())
                        .load(Constant.QUESTION_BASE_URL + "user_update_profile_with_interest")
                        .setMultipartParameter("user_id", URLEncoder.encode(ID, "UTF-8"))
                        .setMultipartParameter("fullname", URLEncoder.encode(FullName, "UTF-8"))
                        .setMultipartParameter("username", URLEncoder.encode(UserName, "UTF-8"))
                        .setMultipartParameter("email", URLEncoder.encode(Email, "UTF-8"))
                        .setMultipartParameter("country_id", URLEncoder.encode(Country, "UTF-8"))
                        .setMultipartParameter("interest_id", InterestId)
                        .setMultipartFile("image", new File(ImagePath))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                Log.d("JSONRESULT@@" + "", result);
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            new UpdateUserDetails(ID, ImagePath, FullName, UserName, Email, Country, InterestId).execute();
        } else {
            Toast.makeText(getActivity(), "Please Enter Interest", Toast.LENGTH_SHORT).show();
            // }
                            /*} else {
                                Toast.makeText(getActivity(), "Please Enter Country", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter User Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                }*/
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
        interestID = new ArrayList<>();
        getinterestidspinner = indices.toArray();

        for (int i = 0; i < getinterestidspinner.length; i++) {
            interestID.add(arrayInterestIdList.get((Integer) getinterestidspinner[i]));
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
            Log.d("RESPONSE", "Edit Profile Interest..." + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = jsonObject.getJSONObject("user_intrests");
                    JSONArray userArray = userObject.getJSONArray("interests");
                    for (int i = 1; i < userArray.length(); i++) {
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
                spinnerIntererst.setItems(arrayInterestList);
            } else {
                Toast.makeText(getActivity(), "No Interest Found, Please Try Again.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayCountryList = new ArrayList<>();
            arrayCountryIDList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
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

                    JSONArray userArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject interestObject = userArray.getJSONObject(i);
                        arrayCountryList.add(interestObject.getString("country_name"));
                        arrayCountryIDList.add(interestObject.getString("country_id"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayCountryList.size() > 0) {
                // TODO: 2/27/2017 for spinner data of interest
                spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayCountryList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCountry.setAdapter(spinnerAdapter);

            } else {
                Toast.makeText(getActivity(), "No Country Found, Please Try Again.", Toast.LENGTH_SHORT).show();
            }

        }

    }

//    private class UpdateUserDetails extends AsyncTask<String, String, String> {
//        ProgressDialog pd;
//        String userid, fullname, username, email, country, interest
//        , profilepicfile;
//
//        public UpdateUserDetails(String userid, String profilePic, String fullName, String userName, String email, String country, String interest) {
//            this.userid = userid;
//            this.profilepicfile = profilePic;
//            this.fullname = fullName;
//            this.username = userName;
//            this.email = email;
//            this.country = country;
//            this.interest = interest;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            arrayList = new ArrayList<>();
//            pd = new ProgressDialog(getActivity());
//            pd.setMessage("Loading");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            HashMap<String, String> hashMap = new HashMap<>();
//
//            try {
//                hashMap.put("user_id", URLEncoder.encode(userid, "UTF-8"));
//                hashMap.put("fullname", URLEncoder.encode(fullname, "UTF-8"));
//                hashMap.put("username", URLEncoder.encode(username, "UTF-8"));
//                hashMap.put("email", URLEncoder.encode(email, "UTF-8"));
//                hashMap.put("image", URLEncoder.encode(profilepicfile, "UTF-8"));
//                hashMap.put("country_id", country);
//                hashMap.put("interest_id", interest);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            //http://181.224.157.105/~hirepeop/host2/surveys/api/user_update_profile_with_interest/805/Tulips.jpg/archirayan/nikita/archirayan35%40gmail.com/1/0,1,4
//            // return utils.getResponseofGet(Constant.QUESTION_BASE_URL + "user_update_profile_with_interest/" + ID + "/" + profilepic + "/" + fullname + "/" + username + "/" + URLEncoder.encode(email, "UTF-8") + "/" + country + "/" + interest);
//            return utils.getResponseofPost(Constant.QUESTION_BASE_URL + "user_update_profile_with_interest", hashMap);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d("RESPONSE", "Update UserDetails..." + s);
//            pd.dismiss();
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
//                   /* JSONObject userObject = jsonObject.getJSONObject("inserted_data");
//                    JSONObject interestObject = jsonObject.getJSONObject("0");
//                    JSONArray interestArray = interestObject.getJSONArray("interest_id");
//                    for (int i = 0; i < interestArray.length(); i++) {
//                        JSONObject interest = interestArray.getJSONObject(i);
//                        arrayInterestIdList.add(interest.getString("id"));
//                        arrayInterestList.add(interest.getString("name"));
//                    }
//                    details = new UserProfileDetails();
//                    details.setUserFullName(userObject.getString("fullname"));
//                    details.setUserUserName(userObject.getString("username"));
//                    details.setUserEmail(userObject.getString("email"));
//                    details.setUserCountryId(userObject.getString("country_id"));
//                    details.setUserImage(userObject.getString("image"));
//
//                    arrayList.add(details);*/
//                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//           /* if (arrayList.size() > 0) {
//                Toast.makeText(getActivity(), "User Details Updated Successfully", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(getActivity(), "User Details not Updated, Please Try Again.", Toast.LENGTH_SHORT).show();
//            }*/
//        }
//    }
}
