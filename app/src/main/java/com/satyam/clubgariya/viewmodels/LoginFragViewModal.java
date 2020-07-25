package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.satyam.clubgariya.callbacks.ILoginRepository;
import com.satyam.clubgariya.callbacks.LoginViewModalListner;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.Login;
import com.satyam.clubgariya.modals.LoginError;
import com.satyam.clubgariya.modals.UserRegister;
import com.satyam.clubgariya.repositories.LoginRepository;
import com.satyam.clubgariya.ui.RegisterFragment;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.utils.AppValidator;

public class LoginFragViewModal extends AndroidViewModel implements ILoginRepository {
    private static final String TAG = "LoginFragViewModal";

    public String welcomeString = "Hi ";
    public String email;
    public String password;
    public LoginViewModalListner loginViewModalListner;
    private MutableLiveData<Boolean> showProgress;
    private MutableLiveData<LoginError> loginErrorMutableLiveData;
    private String name;

    public LoginFragViewModal(@NonNull Application application) {
        super(application);
        name=CurrentUserData.getInstance().getUserName();
        if (name== null) {
            welcomeString = "Welcome To Club";
        } else {
            if(name.contains("null"))
                welcomeString ="Welcome, Please login";
            else welcomeString = welcomeString + " " + name + ",";
        }
    }

    public LiveData<Boolean> getShowProgress(){
        if(showProgress ==null)
            showProgress=new MutableLiveData<>();
        return showProgress;
    }

    public LiveData<LoginError> getLoginErrorData() {
        if (loginErrorMutableLiveData == null)
            loginErrorMutableLiveData = new MutableLiveData<>();

        return loginErrorMutableLiveData;
    }


    public boolean checkValidation() {
        boolean isValid = true;
        String emailError = "";
        String passwordError = "";
        if (TextUtils.isEmpty(email)) {
            emailError = "Email Required";
            isValid = false;
        } else if (!AppValidator.isValidEmail(email)) {
            emailError = "Email is Invalid";
            isValid = false;
        }else if (TextUtils.isEmpty(password)) {
            passwordError = "Password Required";
            isValid = false;
        } else if (password.length() < 6) {
            passwordError = "Password Should Have 6 Characters";
            isValid = false;
        }
        if (isValid) {
            loginErrorMutableLiveData.setValue(new LoginError());
        } else {
            loginErrorMutableLiveData.setValue(new LoginError(emailError, passwordError));
        }
        return isValid;
    }

    public void onSignInClicked(View view) {
        if (checkValidation()) {
            showProgress.setValue(true);
            LoginRepository.getInstance(this).authenticateUser(email,password);
        }
    }

    public void onSignUpClick(View view) {
        loginViewModalListner.changeFragment(RegisterFragment.getInstance());
    }

    @Override
    public void onAuthSuccess() {
        showProgress.setValue(false);
        loginViewModalListner.onAuthSuccess();
    }

    @Override
    public void onUserDataSuccess(UserRegister userRegister) {
        CurrentUserData.getInstance().setUserRegister(userRegister);
        showProgress.setValue(false);
        loginViewModalListner.onAuthSuccess();
    }

    @Override
    public void onAuthFailure(String message) {
        showProgress.setValue(false);
        loginViewModalListner.onAuthFailed(message);

    }
}
