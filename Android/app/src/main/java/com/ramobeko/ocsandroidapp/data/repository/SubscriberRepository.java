package com.ramobeko.ocsandroidapp.data.repository;

import android.content.Context;
import android.util.Log;

import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.data.remote.ApiClient;
import com.ramobeko.ocsandroidapp.data.remote.ApiService;
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
                if (response.isSuccessful() && response.body() != null) {
                    // Log the subscriber data to the console
                    List<Subscriber> subscribers = response.body();
                    for (Subscriber subscriber : subscribers) {
                        Log.d("SubscriberRepository", "Subscriber ID: " + subscriber.getId());
                        Log.d("SubscriberRepository", "Customer Name: " + subscriber.getCustomer().getName());
                        Log.d("SubscriberRepository", "Phone Number: " + subscriber.getPhoneNumber());
                        Log.d("SubscriberRepository", "Package Plan: " + subscriber.getPackagePlan().getName());
                        Log.d("SubscriberRepository", "Start Date: " + subscriber.getStartDate());
                        // Add more details as needed
                    }

                    // Pass the result to the callback
                    callback.onSuccess(subscribers);
                } else {
                    callback.onFailure("Failed to get subscribers");
                }
            }

            @Override
            public void onFailure(Call<List<Subscriber>> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    // Define the callback interface to handle success/failure
    public interface SubscriberCallback {
        void onSuccess(List<Subscriber> subscribers);
        void onFailure(String errorMessage);
    }
}
