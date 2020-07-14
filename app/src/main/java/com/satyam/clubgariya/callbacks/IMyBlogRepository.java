package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.Blog;

import java.util.List;

public interface IMyBlogRepository {
    void onSuccess(List<Blog> blogList);
    void onFailure(String message);
}
