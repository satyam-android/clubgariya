package com.satyam.clubgariya.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.satyam.clubgariya.callbacks.UserProfileCallback;


public class UserProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private String imageUrl;
    private String Name;
    private String status;
    private UserProfileCallback profileCallback;
    private String profileImage;


    public UserProfileViewModel(@NonNull Application application) {
        super(application);
//        profileImage=CurrentUserData.getInstance().getUserImageUrl();
        setProfileData();
    }

    public void setProfileCallback(UserProfileCallback profileCallback){
        this.profileCallback=profileCallback;
    }

    public void setProfileData(){

    }

    public void uploadProfilePicture() {

    }

    public void saveUserData() {
/*        FirebaseObjectHandler.getInstance().getUserCollection().document().set(userRegister).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                replaceFragment(HomeFragment.getInstance());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/
    }

}