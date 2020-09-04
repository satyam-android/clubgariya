package com.satyam.clubgariya.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.satyam.clubgariya.callbacks.ILoginMobileCallback;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.CountryCodeToPhone;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginMobileViewModel extends AndroidViewModel {

    private static final String TAG = "LoginMobileViewModel";
    // TODO: Implement the ViewModel
    public String mobileNumber;
    public String countryCode = "+91";
    public String verificationCode;
    public String userName;
    private ILoginMobileCallback viewCallBack;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks authCallback;
    private String mVerificationId;
    private PhoneAuthCredential phoneAuthCredential;
    private Activity activity;
    private User user;


    public LoginMobileViewModel(@NonNull Application application) {
        super(application);
        createAuthListners();
        // countryCode=new CountryCodeToPhone().getPhone(UtilFunction.getInstance().getCountryIso(getApplication()));
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onRegisterBtnClick(View view) {
        Log.e(TAG, "onRegisterBtnClick: " + mobileNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCode + mobileNumber, 90, TimeUnit.SECONDS, activity, authCallback);
    }

    public void onVerifyBtnClick(View view) {
        Log.e(TAG, "onVerifyBtnClick: ");
        phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithAuthCredential(phoneAuthCredential);
    }

    public void setViewCallBack(ILoginMobileCallback viewCallBack) {
        this.viewCallBack = viewCallBack;
    }

    private void createAuthListners() {
        authCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithAuthCredential(phoneAuthCredential);
                Log.e(TAG, "onVerificationCompleted: ");
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
                viewCallBack.onCodeSend();
                Log.e(TAG, "onCodeSent: ");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Log.e(TAG, "onVerificationFailed: ((FirebaseAuthInvalidCredentialsException) e).getErrorCode()" );
                viewCallBack.onAuthenticationFail(e.getMessage());

            }
        };
    }

    private void signInWithAuthCredential(final PhoneAuthCredential phoneAuthCredential) {
        Log.e(TAG, "signInWithAuthCredential: ");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        final String token = task.getResult().getToken();
                        FirebaseObjectHandler.getInstance().getFirebaseAuth().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkIfUserExist(task.getResult().getUser().getUid(),token);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                viewCallBack.onAuthenticationFail(e.getMessage());
                                Log.e(TAG, "onFailure: ");
                            }
                        });
                    }
                });

    }

    private void checkIfUserExist(String uid,String token) {
//                                    CurrentUserData.getInstance().setUser(user);
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user=documentSnapshot.toObject(User.class);
                    user.setUid(uid);
                    viewCallBack.onAuthenticationSuccessful(user);
                }else{
                    user = new User("", "OneClick text for everything", AppConstants.DATABASE_CONTACT_STATUS_DEFAULT, "", AppConstants.USER_TYPE_INDIVIDUAL, AppConstants.USER_BUSINESS_TYPE_DEFAULT, countryCode + mobileNumber, uid, "", DateTimeUtilityFunctions.getInstance().getCurrentTime(), 0.0, 0.0, token, true, new ArrayList<>(),new ArrayList<>());
                    FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            viewCallBack.onAuthenticationSuccessful(user);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            viewCallBack.onAuthenticationFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }


}