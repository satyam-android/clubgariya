package com.satyam.clubgariya.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.satyam.clubgariya.callbacks.IClubBlogRepository;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Blog;

import java.util.ArrayList;
import java.util.List;

public class ClubBlogRepository {
    private static final String TAG = "ClubBlogRepository";
    private static ClubBlogRepository repository;
    private List<Blog> blogList = new ArrayList<>();
    private static IClubBlogRepository callBack;
    private CollectionReference blogCollectionReference;

    private ClubBlogRepository() {
        blogCollectionReference = FirebaseObjectHandler.getInstance().getBlogCollection();
    }

    public static ClubBlogRepository getInstance(IClubBlogRepository cb) {
        callBack = cb;
        if (repository == null) repository = new ClubBlogRepository();
        return repository;
    }


    public void getClubBlogList() {
//         databaseReference.addValueEventListener(new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot snapshot) {
//                 blogList.clear();
//                 for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                     Blog blog= dataSnapshot.getValue(Blog.class);
//                     BlogData blogData=new BlogData(dataSnapshot.getKey(),blog);
//                         blogList.add(blogData);
//                 }
//                 Collections.reverse(blogList);
//                 callBack.onSuccess(blogList);
//             }
//
//             @Override
//             public void onCancelled(@NonNull DatabaseError error) {
//                 callBack.onFailure(error.getMessage());
//             }
//         });
    }

    public void updateBlog(Blog blogData) {
        FirebaseObjectHandler.getInstance().getBlogDocumentReference(blogData.getDocumentID()).set(blogData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: Like" );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: like" );

            }
        });
    }
}
