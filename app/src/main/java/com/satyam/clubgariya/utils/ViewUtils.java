package com.satyam.clubgariya.utils;

import android.content.Context;
import android.widget.Toast;

public class ViewUtils {

    public static void showToast(String message, Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
