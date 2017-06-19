package com.example.raviarchi.daberny.Activity.Retrofit;

import com.example.raviarchi.daberny.Activity.Utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Archirayan on 16-Jun-17.
 * Archirayan Infotech pvt Ltd
 * dilip.bakotiya@gmail.com || info@archirayan.com
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        final OkHttpClient okHttpClient = builder.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.QUESTION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}
