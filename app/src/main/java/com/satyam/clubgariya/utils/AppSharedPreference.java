package com.satyam.clubgariya.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {

    Context context;
    SharedPreferences preferences;
    public AppSharedPreference(Context context){
        this.context=context;
        preferences=context.getSharedPreferences("ClubPreference",Context.MODE_PRIVATE);
    }


    public void setStringData(String key,String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key){
        return preferences.getString(key,null);
    }

}
