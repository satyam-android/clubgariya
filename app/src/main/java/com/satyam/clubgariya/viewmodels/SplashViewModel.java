package com.satyam.clubgariya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.satyam.clubgariya.callbacks.ISplashModelCallback;
import com.satyam.clubgariya.callbacks.ISplashRepoCallback;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.UserRegister;
import com.satyam.clubgariya.repositories.SplashRepository;
import com.satyam.clubgariya.ui.HomeFragment;
import com.satyam.clubgariya.ui.LoginFragment;
import com.satyam.clubgariya.utils.ViewUtils;

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
    public void onSuccess(UserRegister userRegister) {
        CurrentUserData.getInstance().setUserRegister(userRegister);
        callback.onSuccess();
    }

    @Override
    public void onFailure(String message) {
        callback.onFailure(message);
    }
}
