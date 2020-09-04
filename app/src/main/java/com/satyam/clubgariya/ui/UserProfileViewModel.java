package com.satyam.clubgariya.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.satyam.clubgariya.callbacks.UserProfileCallback;
import com.satyam.clubgariya.database.tables.User;


public class UserProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public String imageUrl;
    public String Name;
    public String status;
    private UserProfileCallback profileCallback;
    private String profileImage;


    public UserProfileViewModel(@NonNull Application application) {
        super(application);
//        profileImage=CurrentUserData.getInstance().getUserImageUrl();
    }

    public void setProfileCallback(UserProfileCallback profileCallback) {
        this.profileCallback = profileCallback;
    }

    public void setUserData(User user) {
        if (user != null) {
            status=user.getUserStatus();
            imageUrl=user.getImageUrl();
        }
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