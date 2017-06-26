package com.example.raviarchi.daberny.Activity.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.raviarchi.daberny.Activity.Adapter.BlockedUsersAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archirayan on 4/29/2016.
 */
public class Utils {

    Context context;
    SharedPreferences sp;
    Location getLocation;

    public Utils(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(Constant.Prefrence, Context.MODE_PRIVATE);
    }

    public static void WriteSharePrefrence(Context context, String key, String value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void WriteSharePre(Context context, String key, String value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String ReadSharePrefrence(Context context, String key) {
        String data;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        data = preferences.getString(key, "");
        editor.apply();
        return data;
    }

    public static String ReadSharePref(Context context, String key) {
        String data;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        data = preferences.getString(key, "");
        editor.apply();
        return data;
    }

    public static void ClearaSharePrefrence(Context context) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit().clear();
        editor.apply();
    }

    public static void ClearSharePref(Context context, String value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit().remove(value);
        editor.apply();
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static long getDifferent(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        return different / 1000;
    }

    public static String getTimeInCounter(long different) {

        long years = different / (365 * 60 * 60 * 24);
        long months = (different - years * 365 * 60 * 60 * 24) / (30 * 60 * 60 * 24);
        long days = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24) / (60 * 60 * 24);
        long hours = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24) / (60 * 60);
        long minutes = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60) / 60;
        long seconds = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60);
        return " " + years + ":" + months + ":" + days + ":" + hours + ":" + minutes + ":" + seconds;

    }

    public static void animateViewVisibility(final View view, final int visibility) {
        // cancel runnning animations and remove and listeners
        view.animate().cancel();
        view.animate().setListener(null);

        // animate making view visible
        if (visibility == View.VISIBLE) {
            view.animate().alpha(1f).start();
            view.setVisibility(View.VISIBLE);
        }
        // animate making view hidden (HIDDEN or INVISIBLE)
        else {
            view.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(visibility);
                }
            }).alpha(0f).start();
        }
    }

    public static String getResponseofPost(String URL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("URL - ResponseCode", URL + " - " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getResponseofGet(String URL) {
        URL url;
        String response = "";
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("URL - ResponseCode", URL + " - " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public void setSharedPrefrenceIsFirstTime(Boolean firstTime) {
        sp.edit().putBoolean(Constant.ISFIRSTTIMEREG, firstTime).apply();
        Log.d("reg_put", "key" + Constant.ISFIRSTTIMEREG + "value" + firstTime);
    }

    public Boolean getReadSharedPrefrenceIsFirstTime(Context context, String key) {
        Boolean response = sp.getBoolean(Constant.ISFIRSTTIMEREG, false);
        Log.d("reg_get", "key" + Constant.ISFIRSTTIMEREG);
        return response;
    }

    public void clearSharedPreferenceData() {
        sp.edit().clear().apply();

    }


    public void getSelectedInterest(ArrayList<String> interestList, String interest) {
        interest = "";
        if (interestList.size() > 0) {
            for (int i = 0; i < interestList.size(); i++) {
                if (interest.length() > 0) {
                    interest = interest + "," + interestList.get(i);
                } else {
                    interest = interestList.get(i);
                }
            }
            Log.d("interest_utils", interest);
        }

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_contain_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setAdapterForList(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setPostImageVideo(UserProfileDetails details, ImageView imgQuestionPic, VideoView vdProfile, LinearLayout layoutMedia) {
        if (details.getQueImageName().length() > 0) {

            if (details.getQueImage() != null) {
                imgQuestionPic.setVisibility(View.VISIBLE);
                vdProfile.setVisibility(View.VISIBLE);
                layoutMedia.setVisibility(View.VISIBLE);
                if (details.getQueType().equalsIgnoreCase("0")) {
                    layoutMedia.setVisibility(View.GONE);
                } else if (details.getQueType().equalsIgnoreCase("1")) {
                    vdProfile.setVisibility(View.GONE);
                    Picasso.with(context).load(details.getQueImage()).placeholder(R.drawable.ic_placeholder).into(imgQuestionPic);
                } else if (details.getQueType().equalsIgnoreCase("2")) {
                    imgQuestionPic.setVisibility(View.GONE);
                    vdProfile.setVideoURI(Uri.parse(details.getQueImage()));
                    vdProfile.setMediaController(new MediaController(context));
                    vdProfile.requestFocus();
                    vdProfile.start();
                }
            }
        } else {
            layoutMedia.setVisibility(View.GONE);
        }
    }

    public void setCommentUserImageInPicasso(UserProfileDetails details, ImageView imgCommentUserProfilePic) {
        if (details.getQueCommentUserProfilePic() != null) {
            if (details.getQueCommentUserProfilePic().length() > 0) {
                Picasso.with(context).load(details.getQueCommentUserProfilePic()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgCommentUserProfilePic);
            } else {
                Picasso.with(context).load(R.drawable.ic_placeholder).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgCommentUserProfilePic);
            }
        }
    }
    public void getSelectedFriendsId(boolean[] selected,
                                     ArrayList<String> arrayfollowingUserIdList, String followingPeopleId){
        ArrayList<String> newGetId = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                newGetId.add(arrayfollowingUserIdList.get(i));
            }
            followingPeopleId = "";
            for (int j = 0; j < newGetId.size(); j++) {
                if (followingPeopleId.length() > 0) {
                    followingPeopleId = followingPeopleId + "," + newGetId.get(j);
                } else {
                    followingPeopleId = newGetId.get(j);
                }
            }
        }
    }
    public void setPostUserImageInPicasso(UserProfileDetails details, ImageView imgProfilePic) {
        if (details.getUserImage() != null) {
            if (details.getUserImage().length() > 0) {
                Picasso.with(context).load(details.getUserImage()).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).
                        transform(new RoundedTransformation(120, 2)).
                        placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
            }
        }
    }

    public void dynamicVoting(UserProfileDetails details, int position, ArrayList<UserProfileDetails> arrayList, TextView txtVote) {
        String isVoteStatus = details.getQueVoteStatus();
        String isCanVote = details.getUserCanVote();
        if (isVoteStatus.equalsIgnoreCase("1")) {
            if (isCanVote.equalsIgnoreCase("1")) {
                arrayList.get(position).setUserCanVote("0");
                arrayList.get(position).setQueVoteTotalCount(arrayList.get(position).getQueVoteTotalCount() + 1);
                txtVote.setVisibility(View.GONE);
            }
        }
    }

    public void setIdOfQuestion(UserProfileDetails details, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("queid", details.getQueId());
        fragment.setArguments(bundle);
    }

    public void setIdOfCommentUser(UserProfileDetails details, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("id", details.getQueCommentUserId());
        fragment.setArguments(bundle);
    }

    public void setIdOfPostUser(UserProfileDetails details, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("id", details.getUserId());
        fragment.setArguments(bundle);
    }

    public void setDynamicLikeUnLike(UserProfileDetails details, int position, ArrayList<UserProfileDetails> arrayList, ImageView imgLike, String liketask) {
        String isLiked = details.getQueLikeStatus();
        liketask = isLiked.equalsIgnoreCase("1") ? "remove" : "add";
        if (liketask.equalsIgnoreCase("add")) {
            arrayList.get(position).setQueLikeStatus("1");
            imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_like_icon));
            arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() + 1);
        } else {
            arrayList.get(position).setQueLikeStatus("0");
            imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_unlike_icon));
            arrayList.get(position).setQueLikeTotalCount(arrayList.get(position).getQueLikeTotalCount() - 1);
        }
    }

    public void setRadioButtonAsPerVote(RadioButton rdAnswer1, RadioButton rdAnswer2, RadioButton
            rdAnswer3, RadioButton rdAnswer4) {
        rdAnswer1.setButtonDrawable(new ColorDrawable(0xFFFFFF));
        rdAnswer2.setButtonDrawable(new ColorDrawable(0xFFFFFF));
        rdAnswer3.setButtonDrawable(new ColorDrawable(0xFFFFFF));
        rdAnswer4.setButtonDrawable(new ColorDrawable(0xFFFFFF));
    }

    public void setTextOfInboxList(UserProfileDetails userdetails, TextView txtMessage) {
        if (userdetails.getUserMsgStatus().equalsIgnoreCase("1")) {
            txtMessage.setTypeface(null, Typeface.BOLD);
        } else {
            txtMessage.setTypeface(null, Typeface.NORMAL);
        }
    }

    public String MakeServiceCall(String URLSTR) {
        StringBuilder response = null;
        try {
            response = new StringBuilder();
            URL url = new URL(URLSTR);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine = null;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }
        } catch (Exception e) {

        }
        return response.toString();
    }

    private boolean appInstalledOrNot(String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


}

