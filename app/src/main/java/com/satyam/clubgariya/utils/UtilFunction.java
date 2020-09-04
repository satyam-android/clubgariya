package com.satyam.clubgariya.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.services.ContactSyncService;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class UtilFunction {

    private static UtilFunction instance;
    private String localCurrencySymbol = "";

    private UtilFunction() {

    }

    public static UtilFunction getInstance() {
        if (instance == null) instance = new UtilFunction();
        return instance;
    }


    public String getLocalCurrencySymbol(Context context) {
        if (TextUtils.isEmpty(localCurrencySymbol)) {
            Currency currency;
            Locale locale;
            String countryNameCode=new AppSharedPreference(context).getStringData(AppConstants.USER_COUNTRY_CODE);
            if(!TextUtils.isEmpty(countryNameCode)){
                 locale = new Locale("EN",countryNameCode);
                currency=Currency.getInstance(locale);
            }else{
                 currency = Currency.getInstance(Locale.getDefault());
            }
            localCurrencySymbol = currency.getSymbol();
        }
        return localCurrencySymbol;
    }

    public void getTransactionStatus(TransactionReference transactionReference, TextView textView) {
        String status = "";
        double currentAmount = 0;
        for (int i = 0; i < transactionReference.getUsers().size(); i++) {
            if (transactionReference.getUsers().get(i).getUserUid().equals(CurrentUserData.getInstance().getUid())) {
                currentAmount = transactionReference.getUsers().get(i).getUserCredit();
            }
        }
        if (currentAmount==0) {
            status = "No transaction required";
            textView.setTextColor(ContextCompat.getColor(textView.getContext(),R.color.text_color_balance_even));
        }else if(currentAmount>0){
            status ="You need to Collect "+ UtilFunction.getInstance().getLocalCurrencySymbol(textView.getContext())+" "+currentAmount;
            textView.setTextColor(ContextCompat.getColor(textView.getContext(),R.color.text_color_balance_profit));
        }else if(currentAmount<0){
            status ="You need to pay "+ UtilFunction.getInstance().getLocalCurrencySymbol(textView.getContext())+" "+Math.abs(currentAmount);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(),R.color.text_color_balance_loss));
        }
        textView.setText(status);
    }

    public String getCountryIso(Context context) {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        iso = telephonyManager.getNetworkCountryIso();
        if (TextUtils.isEmpty(iso))
            iso = telephonyManager.getNetworkCountryIso(1);
        if (TextUtils.isEmpty(iso))
            iso = telephonyManager.getNetworkCountryIso(2);
        if (telephonyManager.getNetworkCountryIso() != null &&
                !TextUtils.isEmpty(telephonyManager.getNetworkCountryIso()))
            iso = telephonyManager.getNetworkCountryIso();
        return iso;
    }

    public void startContactSyncAdapter(Context context) {
//        if(!AppConstants.IS_CONTACT_SERVICE_RUNNING && FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null) {
        Intent intent = new Intent(context, ContactSyncService.class);
        ContactSyncService.enqueueWork(context, intent);
//        }
    }

    public String getFileExtension(Context context, Uri fileUri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return "." + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public String getUniqueGroupName(String groupName){
       return  CurrentUserData.getInstance().getUid() + "__" + groupName;
    }


}
