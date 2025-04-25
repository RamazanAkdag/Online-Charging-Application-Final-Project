package com.ramobeko.ocsandroidapp.data.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.data.remote.ApiClient;
import com.ramobeko.ocsandroidapp.data.remote.ApiService;
import com.ramobeko.ocsandroidapp.ui.login.LoginActivity;
import com.ramobeko.ocsandroidapp.utils.SecurePrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriberRepository {

    private final ApiService apiService;

    public SubscriberRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public void getSubscriptions(Context context, final SubscriberCallback callback) {
        String token = SecurePrefs.getToken(context);
        if (token == null || token.isEmpty()) {
            callback.onFailure("Token is missing or invalid");
            return;
        }

        apiService.getSubscribers("Bearer " + token).enqueue(new Callback<List<Subscriber>>() {
            @Override
            public void onResponse(Call<List<Subscriber>> call, Response<List<Subscriber>> response) {
                if (response.code() == 401) {
                    SecurePrefs.saveToken(context, null);

                    callback.onFailure("Token invalid or expired. Login required.");

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    return;
                }

                if (response.code() == 404) {
                    callback.onFailure("Aboneliğiniz bulunmamaktadır.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    List<Subscriber> subscribers = response.body();
                    for (Subscriber subscriber : subscribers) {
                        Log.d("SubscriberRepository", "Subscriber ID: " + subscriber.getId());
                        Log.d("SubscriberRepository", "Customer Name: " + subscriber.getCustomer().getName());
                        Log.d("SubscriberRepository", "Phone Number: " + subscriber.getPhoneNumber());
                        Log.d("SubscriberRepository", "Package Plan: " + subscriber.getPackagePlan().getName());
                        Log.d("SubscriberRepository", "Start Date: " + subscriber.getStartDate());
                    }
                    callback.onSuccess(subscribers);
                } else {
                    callback.onFailure("Failed to get subscribers");
                }
            }

            @Override
            public void onFailure(Call<List<Subscriber>> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public interface SubscriberCallback {
        void onSuccess(List<Subscriber> subscribers);
        void onFailure(String errorMessage);
    }
}
