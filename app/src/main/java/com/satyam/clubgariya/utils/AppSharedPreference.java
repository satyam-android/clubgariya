package com.satyam.clubgariya.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {

    Context context;
    SharedPreferences preferences;

    public AppSharedPreference(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("ClubPreference", Context.MODE_PRIVATE);
    }

    public void setIntegerData(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setStringData(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key) {
        return preferences.getString(key, null);
    }

    public int getIntegerData(String key) {
        return preferences.getInt(key, 0);
    }

}
