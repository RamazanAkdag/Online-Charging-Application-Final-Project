package com.ramobeko.ocsandroidapp.data.remote;

import com.ramobeko.ocsandroidapp.data.model.ApiResponse;
import com.ramobeko.ocsandroidapp.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register")
    Call<ApiResponse> register(@Body RegisterRequest request);

}
