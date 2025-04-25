package com.ramobeko.ocsandroidapp.data.repository;

import android.content.Context;
import android.widget.Toast;

import com.ramobeko.ocsandroidapp.data.model.ApiResponse;
import com.ramobeko.ocsandroidapp.data.model.ForgotPasswordRequest;
import com.ramobeko.ocsandroidapp.data.remote.ApiClient;
import com.ramobeko.ocsandroidapp.data.remote.ApiService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordRepository {

    private final ApiService apiService;

    public ForgotPasswordRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public void sendForgotPasswordRequest(Context context, String email, Runnable onSuccess, Runnable onFailure) {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        apiService.forgotPassword(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else if (response.code() == 400) {
                    Toast.makeText(context, "E-posta adresi sistemde bulunamadı.", Toast.LENGTH_SHORT).show();
                    onFailure.run();
                } else {
                    Toast.makeText(context, "Beklenmeyen bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    onFailure.run();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Ağ hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
                onFailure.run();
            }
        });
    }
}
