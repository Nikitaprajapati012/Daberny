package com.example.raviarchi.daberny.Activity.Retrofit;

import com.example.raviarchi.daberny.Activity.Model.ChatMessage;
import com.example.raviarchi.daberny.Activity.Model.Message;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Model.getContactListPojo;
import com.example.raviarchi.daberny.Activity.Utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Archirayan on 16-Jun-17.
 * Archirayan Infotech pvt Ltd
 * dilip.bakotiya@gmail.com || info@archirayan.com
 */

public interface ApiInterface {

    @GET("searched_user/752")
    Call<getContactListPojo> getContactList(@Query(Constant.USERID) String userid);

    @Multipart
    @POST("insert_chat_msg/")
    Call<ChatMessage> getChatList(@Query(Constant.USERID) String userid,
                                  @Query("receiver_id") String reciverid,
                                  @Part("chat_picture") String image,
                                  @Query("msg")String msg);

}
