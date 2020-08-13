package com.satyam.clubgariya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.satyam.clubgariya.callbacks.UserListListner;

public class ChatListViewModel extends AndroidViewModel {

    private UserListListner listner;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
    }


}