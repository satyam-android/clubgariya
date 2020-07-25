package com.satyam.clubgariya.utils;

import android.content.Context;
import android.content.Intent;

import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.services.ContactSyncService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFunction {

    private static UtilFunction instance;
    private UtilFunction(){

    }

    public static UtilFunction getInstance(){
        if(instance==null)instance=new UtilFunction();
        return instance;
    }

    public String getCurrentTime(){
        return String.valueOf(System.currentTimeMillis());
    }

    public String getDateTime(String dateTimeFormat){
        // Format i:e  "dd/MM/yyyy HH:mm:ss"
        String value=null;
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date = new Date();
        value=formatter.format(date);
        return value;
    }


    public void startContactSyncAdapter(Context context){
        if(!AppConstants.IS_CONTACT_SERVICE_RUNNING && FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null) {
            Intent intent = new Intent(context, ContactSyncService.class);
            ContactSyncService.enqueueWork(context, intent);
        }
    }
}
