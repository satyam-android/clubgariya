package com.satyam.clubgariya.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.viewmodels.BlogNewPostViewModel;

public class BlogNewPostFragment extends BaseFragment {

    private BlogNewPostViewModel mViewModel;

    public static BlogNewPostFragment newInstance() {
        return new BlogNewPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_new_post_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BlogNewPostViewModel.class);
        // TODO: Use the ViewModel
    }

}