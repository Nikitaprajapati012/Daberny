package com.example.raviarchi.daberny.Activity.Retrofit;


import com.example.raviarchi.daberny.Activity.Utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClientWithHeader {

    private static Retrofit retrofit = null;
 
    public static Retrofit getClient(final ArrayList<String> headers, final ArrayList<String> values) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                for (int i=0;i<headers.size();i++) {
                    requestBuilder.header(headers.get(i), values.get(i));
                }

                Request request = requestBuilder.method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        }).readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

        final OkHttpClient okHttpClient = builder.build();

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