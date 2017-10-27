package com.example.android.testing.notification;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static final String TAG_MAIL = "tagmail";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, null);
    }

    //this method save email
    public boolean saveEmail(String mail) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_MAIL, mail);
        editor.apply();
        return true;
    }

    // fetch email
    public String getEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_MAIL, null);
    }

    //save notification
    public void saveCounter(int i) {

    }

}