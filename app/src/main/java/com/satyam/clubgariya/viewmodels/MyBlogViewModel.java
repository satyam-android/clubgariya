package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.callbacks.IMyBlogRepository;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.services.FileUploadService;
import com.satyam.clubgariya.helper.Temp;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.repositories.MyBlogRepository;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;

import java.util.List;
import java.util.UUID;

public class MyBlogViewModel extends AndroidViewModel implements IMyBlogRepository {
    // TODO: Implement the ViewModel\
    private static final String TAG = "MyBlogViewModel";

    public String description = "";
    private MyBlogRepository myBlogRepository;
    private FirebaseAuth firebaseAuth;

    // Uri indicates, where the image will be picked from
    public Uri filePath;


    public MyBlogViewModel(@NonNull Application application) {
        super(application);
        myBlogRepository = MyBlogRepository.getInstance(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }


//    public MyBlogViewModel(){
//        firebaseDatabase=FirebaseDatabase.getInstance();
//        databaseReference=firebaseDatabase.getReference("Blogs");
//    }




    public void onPostButtonClick(View view) {
        if (TextUtils.isEmpty(description)) {

        } else {
            uploadImage();
//            showProgress.postValue(true);
//            Blog blog = new Blog(FirebaseAuth.getInstance().getUid(), System.currentTimeMillis(), "www.google.com",description, new ArrayList<Like>(), new ArrayList<Comment>());
//            myBlogRepository.addBlog(blog);
        }
    }


    @Override
    public void onSuccess(List<Blog> blogList) {
    }

    @Override
    public void onFailure(String message) {

    }

    public void uploadImage() {


        if (filePath != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + ".jpeg");

            Blog blog = new Blog(FirebaseAuth.getInstance().getUid(), System.currentTimeMillis(), "", description,CurrentUserData.getInstance().getUserFullName(),AppConstants.BLOG_STATUS_ACTIVE);

            Temp.getInstance().setFileUriList(filePath);
            Temp.getInstance().setBlog(blog);
            Temp.getInstance().setStorageReference(ref);
            Temp.getInstance().setDatabaseReference(FirebaseObjectHandler.getInstance().getBlogCollection());

            Intent intent = new Intent(getApplication(), FileUploadService.class);
            FileUploadService.enqueueWork(getApplication(), intent);
//            getApplication().startService(intent);
        }
    }
}