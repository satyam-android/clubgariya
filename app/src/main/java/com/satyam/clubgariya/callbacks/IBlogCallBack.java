package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

import com.satyam.clubgariya.modals.Blog;

public interface IBlogCallBack {
    void onLikeClick(Blog blogData);
    void onCommentClick(Blog blogData);
    void onShareClick(Blog blogData);
    void changeScreen(Fragment fragment);

}
