package com.satyam.clubgariya.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;

import java.util.HashMap;
import java.util.Map;

public class AppDatabaseHelper {
    private static final String TAG = "AppDatabaseHelper";
    private static AppDatabaseHelper instance;
    private static Context context;
    private Map<String, User> tempUserHolder;
    private User user;


    private AppDatabaseHelper() {
        tempUserHolder = new HashMap<>();
    }

    public static AppDatabaseHelper getInstance(Context ctx) {
        context = ctx;
        if(instance==null) instance=new AppDatabaseHelper();
        return instance;
    }

    public void getNameByNumber(String mobile, GetNameByNumber getNameByNumber) {
        String name = "";
        user=null;
        if (tempUserHolder.containsKey(mobile)) {
            name = tempUserHolder.get(mobile).getName();
        } else {
            user = AppDatabase.getInstance(context).userDao().getUserDetailByNumber(mobile).getValue();
            if (user != null) {
                name = user.getName();
                tempUserHolder.put(mobile, user);
            }
            if (TextUtils.isEmpty(name))
                name = mobile;
        }
        getNameByNumber.onNameSuccess(name);
    }

/*    public void getUserByMobile(String mobile, GetUserDetail userDetail) {
        user=null;
        if (tempUserHolder.containsKey(mobile))
            user = tempUserHolder.get(mobile);
        else {
            user = AppDatabase.getInstance(context).userDao().getUserDetailByNumber(mobile).getValue();
            if (user != null)
                tempUserHolder.put(mobile, user);
        }
        if (user == null) {
            user = new User();
            user.setMobile(mobile);
        }
        userDetail.onUserSuccess(user);
    }*/

    public void getUserByUid(String uid, GetUserDetail userDetail) {
        user=null;
        if (tempUserHolder.containsKey(uid)){
            Log.e(TAG, "getUserByUid: Cache Success" );
            user = tempUserHolder.get(uid);
            userDetail.onUserSuccess(user);
        }
        else {
            new Thread(() -> {
                user = AppDatabase.getInstance(context).userDao().getUserDetailByUid(uid);
                if (user != null){
                    Log.e(TAG, "getUserByUid: Success Database" );
                    tempUserHolder.put(uid, user);
                    userDetail.onUserSuccess(user);
                }
                else{
                    FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Log.e(TAG, "getUserByUid: Success Server" );
                                user=documentSnapshot.toObject(User.class);
                                user.setUid(documentSnapshot.getId());
                                tempUserHolder.put(user.getUid(),user);
                                userDetail.onUserSuccess(user);
                            }
                        }
                    });
                }
            }).start();

        }


    }


    public interface GetUserDetail {
        void onUserSuccess(User user);
    }

    public interface GetNameByNumber {
        void onNameSuccess(String name);
    }

}
