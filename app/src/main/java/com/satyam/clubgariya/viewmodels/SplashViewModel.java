package com.satyam.clubgariya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.satyam.clubgariya.callbacks.ISplashModelCallback;
import com.satyam.clubgariya.callbacks.ISplashRepoCallback;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.repositories.SplashRepository;

public class SplashViewModel extends AndroidViewModel implements ISplashRepoCallback {
    FirebaseAuth firebaseAuth;
    private ISplashModelCallback callback;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void setListner(ISplashModelCallback callback) {
        this.callback = callback;
    }

    public void getUserDetail() {
        if (firebaseAuth.getCurrentUser() == null) {
            onFailure("");
        } else {
            SplashRepository.getInstance(this).getUserData(firebaseAuth.getUid());
        }
    }


    @Override
    public void onSuccess(User user) {
        callback.onSuccess(user);
    }

    @Override
    public void onFailure(String message) {
        callback.onFailure(message);
    }
}
