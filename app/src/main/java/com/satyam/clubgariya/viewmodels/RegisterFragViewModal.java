package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.satyam.clubgariya.callbacks.RegisterViewModelListner;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.UserRegister;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;

public class RegisterFragViewModal extends AndroidViewModel {
    private static final String TAG = "RegisterFragViewModal";

    public String fName;
    public String lName;
    public String email;
    public String password;
    public String confPassword;
    public String mobileNumber;
    public RegisterViewModelListner listner;
    public String fNameError;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore rootNode;
    private CollectionReference collectionReferenceUsers;


    public RegisterFragViewModal(@NonNull Application application) {
        super(application);
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
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
        if (TextUtils.isEmpty(fName)) {
            listner.onRegisterFail("First Name Required");
        } else if (TextUtils.isEmpty(lName)) {
            listner.onRegisterFail("Last Name Required");

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

    public void saveUserData(String uid){
        UserRegister userRegister=new UserRegister(fName,lName,email,password,mobileNumber,uid,"");
        CurrentUserData.getInstance().setUserRegister(userRegister);
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).set(userRegister).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listner.onRegisterSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listner.onRegisterFail(e.getLocalizedMessage());
            }
        });
/*        rootNode=FirebaseFirestore.getInstance();
        collectionReferenceUsers= FirebaseObjectHandler.getInstance().getUserCollection();
        collectionReferenceUsers.document(uid).set(userRegister).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listner.onRegisterSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listner.onRegisterFail(e.getLocalizedMessage());
            }
        });*/

    }
}
