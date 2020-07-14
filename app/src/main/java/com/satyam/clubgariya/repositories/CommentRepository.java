package com.satyam.clubgariya.repositories;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.satyam.clubgariya.callbacks.ICommentRepositoryCallback;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.modals.Comment;
import com.satyam.clubgariya.utils.AppConstants;

import java.util.HashMap;

public class CommentRepository {
    private static final String TAG = "CommentRepository";
    private static CommentRepository repository;
  private CollectionReference collectionReferenceBlog;
    private static ICommentRepositoryCallback callback;
    private CommentRepository(){
        collectionReferenceBlog= FirebaseObjectHandler.getInstance().getBlogCollection();
    }
    
    public static CommentRepository getInstance(ICommentRepositoryCallback listner){
        callback=listner;
        if(repository==null) repository=new CommentRepository();
        return repository;
    }
    


    public void updateBlog(final Comment comment) {

        HashMap<String,Comment> commentHashMap=new HashMap<>();
        commentHashMap.put(comment.getBlogId(),comment);

        FirebaseObjectHandler.getInstance().getCommentCollection().document(comment.getBlogId()).collection(AppConstants.COMMENT_ALL_BLOG_NODE).add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e(TAG, "onSuccess: " );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " );
            }
        });
  /*      FirebaseObjectHandler.getInstance().getBlogDocumentReference(blogData.getDocumentID()).set(blogData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: " );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " );
            }
        });*/
    }
}
