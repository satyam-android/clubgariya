package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.Blog;

import java.util.List;

public interface IClubBlogRepository {

    void onSuccess(List<Blog> blogList);
    void onFailure(String message);
}
