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
                    "Authorization:key=AAAAQ9JOwuI:APA91bGgIoXQsn58CtsC7uWjDLGnk9FENd3BWFgtZZIUWPZG-G21Ox7-yS4JbfQN-vkGO-2RR1wKcx2rvHjTOMGui0dEXmg6MpxHFnRCJDtxEqVf0jHXbxRq3sp96VgaQ-UVQNJBqMKt"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
