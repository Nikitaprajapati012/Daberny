package com.example.raviarchi.daberny.Activity.Utils;

import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*** Created by Ravi archi on 6/2/2017.
 */

interface  Retrofit {
    @FormUrlEncoded
    @POST("api/login_new.php")
    Call<UserProfileDetails> getLogin(@FieldMap Map<String, String> map);

    @GET("/api/users?")
    Call<UserProfileDetails> doGetUserList(@Query("page") String page);

}
