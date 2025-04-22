package com.ramobeko.ocsandroidapp.data.repository;

import android.content.Context;
import android.widget.Toast;

import com.ramobeko.ocsandroidapp.data.model.ApiResponse;
import com.ramobeko.ocsandroidapp.data.model.auth.RegisterRequest;
import com.ramobeko.ocsandroidapp.data.remote.ApiClient;
import com.ramobeko.ocsandroidapp.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    private final ApiService apiService;

    public RegisterRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public void registerUser(Context context, RegisterRequest request, Runnable onSuccess, Runnable onFailure) {
        apiService.register(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    onSuccess.run();
                } else {
                    Toast.makeText(context, "Kayıt başarısız", Toast.LENGTH_SHORT).show();
                    onFailure.run();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
                onFailure.run();
            }
        });
    }
}
