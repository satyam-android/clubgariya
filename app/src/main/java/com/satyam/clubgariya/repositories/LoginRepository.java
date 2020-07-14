package com.satyam.clubgariya.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.satyam.clubgariya.callbacks.ILoginRepository;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.UserRegister;

public class LoginRepository {
    private static ILoginRepository callBack;
    private static LoginRepository repository;
    FirebaseAuth firebaseAuth;

    private LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static LoginRepository getInstance(ILoginRepository cb) {
        callBack = cb;
        if (repository == null) repository = new LoginRepository();
        return repository;
    }


    public void authenticateUser(String email, String password) {
       firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
               getUserData(authResult.getUser().getUid());
//               callBack.onAuthSuccess();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               callBack.onAuthFailure(e.getLocalizedMessage());
           }
       });
    }

    public void getUserData(String uid) {
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserRegister userRegister = documentSnapshot.toObject(UserRegister.class);
                    callBack.onUserDataSuccess(userRegister);
                }else{
                    callBack.onAuthFailure("User Detail Not Found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onAuthFailure(e.getMessage());
            }
        });
    }
}
