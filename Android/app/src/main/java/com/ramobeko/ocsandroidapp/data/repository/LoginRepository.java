package com.ramobeko.ocsandroidapp.data.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ramobeko.ocsandroidapp.data.model.auth.LoginRequest;
import com.ramobeko.ocsandroidapp.data.model.auth.AuthResponse;
import com.ramobeko.ocsandroidapp.data.remote.ApiClient;
import com.ramobeko.ocsandroidapp.data.remote.ApiService;
import com.ramobeko.ocsandroidapp.utils.SecurePrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final ApiService apiService;

    public LoginRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public void loginUser(Context context, LoginRequest request, Runnable onSuccess, Runnable onFailure) {
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show();
                    String token = response.body().getToken();
                    //Save token to local storage secure way.
                    SecurePrefs.saveToken(context, token);


                    Log.i("LoginRepository","token : " + SecurePrefs.getToken(context));
                    onSuccess.run();
                } else {
                    Toast.makeText(context, "Giriş başarısız", Toast.LENGTH_SHORT).show();
                    onFailure.run();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
                onFailure.run();
            }
        });
    }
}

