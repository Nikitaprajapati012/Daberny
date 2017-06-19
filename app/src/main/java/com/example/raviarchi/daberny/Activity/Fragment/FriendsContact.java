package com.example.raviarchi.daberny.Activity.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.raviarchi.daberny.Activity.Adapter.FriendsContactAdapter;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Model.dataPojo;
import com.example.raviarchi.daberny.Activity.Model.getContactListPojo;
import com.example.raviarchi.daberny.Activity.Retrofit.ApiClient;
import com.example.raviarchi.daberny.Activity.Retrofit.ApiInterface;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/*** Created by Ravi archi on 1/10/2017.
 */

public class FriendsContact extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public RecyclerView recyclerViewPeople;
    public Utils utils;
    public UserProfileDetails details;
    public String userId;
    public FriendsContactAdapter adapter;
    private ArrayList<UserProfileDetails> arrayUserList;
    ArrayList<UserProfileDetails> contacts;

    private ArrayList<dataPojo> contactName;
    private ArrayList<String> ContactNumber;
    private ArrayList<String> ContactNumberPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_people, container, false);
        init();
        findViewId(view);
        setContacts();
        return view;
    }

    private void setContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
//            ArrayList<UserProfileDetails> contacts = getFriendsFromContacts();
//            if (arrayUserList.size() > 0) {
//
//                adapter = new FriendsContactAdapter(getActivity(), contacts);
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                recyclerViewPeople.setLayoutManager(mLayoutManager);
//                recyclerViewPeople.setItemAnimator(new DefaultItemAnimator());
//                recyclerViewPeople.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<UserProfileDetails> getFriendsFromContacts() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        details = new UserProfileDetails();
                        details.setUserContactName(name);
                        details.setUserContactNumber(phoneNo);
                        arrayUserList.add(details);
                    }
                    pCur.close();
                }
            }
        }
        return arrayUserList;
    }

    // TODO: 2/22/2017 bind data with field
    private void findViewId(View view) {
        recyclerViewPeople = (RecyclerView) view.findViewById(R.id.fragment_search_recyclerpeoplelist);
    }

    // TODO: 2/21/2017 initialization
    private void init() {
        utils = new Utils(getActivity());
        arrayUserList = new ArrayList<>();
        contactName = new ArrayList<>();
        ContactNumber = new ArrayList<>();
        callGetContactList();
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USERID);
    }

    public void callGetContactList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<getContactListPojo> call = apiInterface.getContactList(Utils.ReadSharePrefrence(getActivity(), Constant.USERID));

        call.enqueue(new Callback<getContactListPojo>() {
            @Override
            public void onResponse(Call<getContactListPojo> call, Response<getContactListPojo> response) {

                Toast.makeText(getActivity(), "hello"+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                if (response.body() != null && response.body().getStatus().equals("true")) {
                    contactName = response.body().getData();
                    for (int i = 0; i < contactName.size(); i++) {
                        ContactNumber.add(contactName.get(i).getFullname());
                    }
                    Log.d("contact number array", "&&"+ContactNumber);
                    contacts = getFriendsFromContacts();
                    Log.d("contact number phone", "&&" +contacts);

                    for(int j=0; j<contacts.size(); j++){
                        ContactNumberPhone.add(contacts.get(j).getUserContactName());
                    }

                    if(contacts.equals(ContactNumber)){
                        Toast.makeText(getActivity(), "all contact get", Toast.LENGTH_SHORT).show();
                    }

                    if (arrayUserList.size() > 0) {

                        adapter = new FriendsContactAdapter(getActivity(), contacts);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerViewPeople.setLayoutManager(mLayoutManager);
                        recyclerViewPeople.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewPeople.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getContactListPojo> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
