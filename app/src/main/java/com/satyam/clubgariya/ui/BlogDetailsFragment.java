package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.databinding.FragmentBlogDetailBinding;
import com.satyam.clubgariya.viewmodels.BlogDetailViewModel;

public class BlogDetailsFragment extends BaseFragment {

private View fragmentView;
private FragmentBlogDetailBinding binding;
private BlogDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_blog_detail,container,false);
        fragmentView=binding.getRoot();

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(getActivity()).get(BlogDetailViewModel.class);
        binding.setBlogDetail(viewModel);
        addListners();
    }


    public void addListners(){

    }
}
