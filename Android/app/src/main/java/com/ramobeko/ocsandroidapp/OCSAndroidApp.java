package com.ramobeko.ocsandroidapp;

import android.app.Application;

import com.ramobeko.ocsandroidapp.di.AppContainer;

public class OCSAndroidApp extends Application {
    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer();
    }
}
