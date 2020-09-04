package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.satyam.clubgariya.callbacks.RegisterViewModelListner;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;

import java.util.ArrayList;

public class RegisterFragViewModal extends AndroidViewModel {
    private static final String TAG = "RegisterFragViewModal";

    public String name;
    public String email;
    public String password;
    public String confPassword;
    public String mobileNumber;
    public RegisterViewModelListner listner;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore rootNode;
    private CollectionReference collectionReferenceUsers;


    public RegisterFragViewModal(@NonNull Application application) {
        super(application);
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void onRegisterButtonClick(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(name)) {
            listner.onRegisterFail("Name Required");
        } else if (TextUtils.isEmpty(mobileNumber)) {
            listner.onRegisterFail("Mobile Number Required");

        } else if (TextUtils.isEmpty(email)) {
            listner.onRegisterFail("Email Required");
        } else if (TextUtils.isEmpty(password)) {
            listner.onRegisterFail("Password Required");

        } else if (TextUtils.isEmpty(confPassword)) {
            listner.onRegisterFail("Confirm Password Required");

        } else if (!password.equals(confPassword)) {
            listner.onRegisterFail("Password and Confirm Password Should Be Same");
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    saveUserData(authResult.getUser().getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listner.onRegisterFail(e.getLocalizedMessage());
                }
            });
        }

    }

    public void saveUserData(final String uid){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        User user =new User(name,"Using OneCLick is such a fun", AppConstants.DATABASE_CONTACT_STATUS_DEFAULT,email,AppConstants.USER_TYPE_INDIVIDUAL,AppConstants.USER_BUSINESS_TYPE_DEFAULT,mobileNumber,uid,"", DateTimeUtilityFunctions.getInstance().getCurrentTime(),0.0,0.0,token,false,new ArrayList<>(),new ArrayList<>());
                        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listner.onRegisterSuccess(user);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listner.onRegisterFail(e.getLocalizedMessage());
                            }
                        });
                    }
                });


    }
}
