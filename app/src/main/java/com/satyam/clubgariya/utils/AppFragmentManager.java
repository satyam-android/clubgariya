package com.satyam.clubgariya.utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AppFragmentManager {
    private static final String TAG = "AppFragmentManager";
    private static AppFragmentManager appFragmentManager;
    FragmentManager fm;
    FragmentTransaction ft;
    Context context;
     private AppFragmentManager(Context context,FragmentManager fm){
         this.context=context;
         this.fm=fm;
         ft=fm.beginTransaction();
     }

     public static AppFragmentManager getInstance(Context context,FragmentManager fm){
         if(appFragmentManager==null)
             appFragmentManager=new AppFragmentManager(context,fm);
         return appFragmentManager;
     }
     
    public void addFragment(Fragment fragment){
        Log.d(TAG, "addFragment: ");
    }


    public void replaceFragment(Fragment fragment){

    }

    public void removeFragment(Fragment fragment){
        Log.d(TAG, "removeFragment: ");
    }
}
