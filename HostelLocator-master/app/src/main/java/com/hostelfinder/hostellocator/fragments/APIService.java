package com.hostelfinder.hostellocator.fragments;

import com.hostelfinder.hostellocator.notification.MyResponse;
import com.hostelfinder.hostellocator.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAArYL6qCM:APA91bGKW3XUgvOH24MLu8SGL4RiTUdSg93ldUo8WZfu_uq8JAfcxIayMGRzdBKDUX79i756nvam_CJvmlmWLiI18Furjjc3i674pX_etq1uNwJ35D7kV2apjmZfkNBT5bMNvYyIxQog"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
