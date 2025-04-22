package com.ramobeko.ocsandroidapp.data.remote;

import com.ramobeko.ocsandroidapp.data.model.ApiResponse;
import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.data.model.auth.AuthResponse;
import com.ramobeko.ocsandroidapp.data.model.auth.LoginRequest;
import com.ramobeko.ocsandroidapp.data.model.auth.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register")
    Call<ApiResponse> register(@Body RegisterRequest request);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("/api/subscriber/customer-subscribers")
    Call<List<Subscriber>> getSubscribers(@Header("Authorization") String token);

}
