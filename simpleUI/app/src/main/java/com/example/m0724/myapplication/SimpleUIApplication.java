package com.example.m0724.myapplication;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;

/**
 * Created by user on 2016/5/5.
 */
public class SimpleUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("rgkv9ziCvVI289EN5e5mVX2VvjxFokd0v1gkijyX") //App Id
                        .clientKey("CJkQ2d9Qvdv3fAIMiZjkNy3gAXWxXqlKHrHvi41x") //Client Key
                        //.applicationId("1IIbdKr6rgMlclbPtrTcibogTq4wst4GVJC2dOX2") //老師的
                        //.clientKey("lYpFFD6Bz8mendveOW91UvjypoGruuaaQPc4EUyR") //老師的
                        .server("https://parseapi.back4app.com/") //Parse API Address
                        .build()
        );
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
