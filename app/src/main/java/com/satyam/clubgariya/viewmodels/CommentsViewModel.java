package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.satyam.clubgariya.callbacks.ICommentRepositoryCallback;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.modals.Comment;
import com.satyam.clubgariya.repositories.CommentRepository;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;

import java.util.ArrayList;

public class CommentsViewModel extends AndroidViewModel implements ICommentRepositoryCallback {

    private FirebaseAuth firebaseAuth;
    private Blog blogData;
    private CommentRepository commentRepository;
    private MutableLiveData<Blog> mutableBlogData;

    public String commentData;

    public CommentsViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = FirebaseAuth.getInstance();
        commentRepository = CommentRepository.getInstance(this);
    }

    public void setBlogData(Blog blogData) {
        this.blogData = blogData;
    }


    public LiveData<Blog> getUpdateBlogData() {
        if (mutableBlogData == null)
            mutableBlogData = new MutableLiveData<>();

        return mutableBlogData;
    }

    public void updateComment(View view) {
        if (!TextUtils.isEmpty(commentData)) {
            Comment comment=new Comment(blogData.documentID,firebaseAuth.getUid(), CurrentUserData.getInstance().getUserFullName(), commentData, CurrentUserData.getInstance().getUserImageUrl(),System.currentTimeMillis());
            commentData = "";
            commentRepository.updateBlog(comment);
        }
    }


    @Override
    public void onCommentUpdate(Blog blogData) {
        mutableBlogData.setValue(blogData);
    }
}