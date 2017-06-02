package com.example.raviarchi.daberny.Activity.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.raviarchi.daberny.R;
import com.example.raviarchi.multiselectionsearchspinner.KeyPairBoolData;
import com.example.raviarchi.multiselectionsearchspinner.MultiSpinnerSearch;
import com.example.raviarchi.multiselectionsearchspinner.SpinnerListener;
import com.example.raviarchi.multiselectionsearchspinner.UserProfileDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainSpinner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainspinner);

        /**
         * Getting array of String to Bind in Spinner
         */
      //  final List<String> list = Arrays.asList(getResources().getStringArray(R.array.sports_array));

        // TODO: 3/10/2017 add in list
        final List<String> listArray = new ArrayList<>();
        final List<UserProfileDetails> list = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            UserProfileDetails h = new UserProfileDetails();
            h.setUserInterestId(String.valueOf(i + 1));
            h.setUserInterestName(String.valueOf(list.get(i)));
            h.setSelected(false);
            listArray.add(String.valueOf(h));
        }

       /* final List<KeyPairBoolData> listArray2 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray2.add(h);
        }
        final List<KeyPairBoolData> listArray3 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray3.add(h);
        }*/
        /**
         * Simple MultiSelection Spinner (Without Search/Filter Functionlity)
         *
         *  Using MultiSpinner class
         */
        /*MultiSpinner simpleSpinner = (MultiSpinner) findViewById(R.id.simpleMultiSpinner);

        simpleSpinner.setItems(listArray, -1, new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {
            }

        });*/

        /**
         * Search MultiSelection Spinner (With Search/Filter Functionality)
         *
         *  Using MultiSpinnerSearch class
         */
        MultiSpinnerSearch searchSpinner = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinner);
        // SingleSpinnerSearch searchSingleSpinner = (SingleSpinnerSearch) findViewById(R.id.searchSingleSpinner);
        // SingleSpinner singleSpinner = (SingleSpinner) findViewById(R.id.singleSpinner);

        /***
         * -1 is no by default selection
         * 0 to length will select corresponding values
         */
        /*searchSpinner.setItems(listArray, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<String> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).length() > 0) {
                    //    Log.i("TAG", i + " : " + items.get(i).getUserInterestName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });*/

       /* searchSpinner.setLimit(3, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(UserProfileDetails data) {
                Toast.makeText(getApplicationContext(),
                        "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });*/

       /* searchSingleSpinner.setItems(listArray2, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

        singleSpinner.setItems(listArray3, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });*/
    }
}
