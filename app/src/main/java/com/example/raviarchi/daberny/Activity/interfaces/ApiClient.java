package com.example.raviarchi.daberny.Activity.interfaces;


import com.example.raviarchi.daberny.Activity.Utils.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static Retrofit getRetrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.QUESTION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getnewClient() {
        if (getRetrofit == null) {
            getRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.QUESTION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return getRetrofit;
    }

}