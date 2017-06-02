package com.example.raviarchi.daberny.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Utils.BitUtility;
import com.example.raviarchi.daberny.Activity.Utils.Constant;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtLogin, txtSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean result = BitUtility.checkPermission(SplashActivity.this);
        Utils.WriteSharePre(SplashActivity.this, Constant.PERMISSION, String.valueOf(result));
        findById();
        click();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    // TODO: 5/16/2017 peform click event
    private void click() {
        txtLogin.setOnClickListener(this);
        txtSignup.setOnClickListener(this);
    }


    // TODO: 5/16/2017 bind the object
    private void findById() {
        txtLogin = (TextView) findViewById(R.id.activity_splash_txtlogin);
        txtSignup = (TextView) findViewById(R.id.activity_splash_txtsignup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_splash_txtlogin:
                Intent ilogin = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(ilogin);
                break;

            case R.id.activity_splash_txtsignup:
                Intent ireg = new Intent(SplashActivity.this, RegistrationActivity.class);
                startActivity(ireg);
                break;
        }

    }
}
