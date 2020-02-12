package com.example.lab2_mobiledevelopment;

import com.example.lab2_mobiledevelopment.CloudNotifications.Client;
import com.example.lab2_mobiledevelopment.CloudNotifications.MyResponse;
import com.example.lab2_mobiledevelopment.CloudNotifications.Sender;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

public interface APINotification {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=Enter your Key"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
