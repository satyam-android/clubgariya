package com.satyam.clubgariya.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.satyam.clubgariya.callbacks.IMyBlogRepository;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Blog;

import java.util.ArrayList;
import java.util.List;

public class MyBlogRepository {
    private static final String TAG = "MyBlogRepository";

    private static MyBlogRepository repository;
    private List<Blog> blogList = new ArrayList<>();
   private CollectionReference collectionReferenceBlog;
    private FirebaseAuth firebaseAuth;
    private static IMyBlogRepository iMyBlogListner;

    private MyBlogRepository() {
        collectionReferenceBlog= FirebaseObjectHandler.getInstance().getBlogCollection();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static MyBlogRepository getInstance(IMyBlogRepository callBack) {
        iMyBlogListner = callBack;
        if (repository == null)
            repository = new MyBlogRepository();
        return repository;
    }




    public void updateComment(Blog blogData) {

    }





}
