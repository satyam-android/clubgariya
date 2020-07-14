package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class BlogNewPostViewModel extends AndroidViewModel {


    private Uri fliList;

    public BlogNewPostViewModel(@NonNull Application application) {
        super(application);
    }

}