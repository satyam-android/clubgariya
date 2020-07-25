package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.databinding.FragmentAppSettingsBinding;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.AppSettingsViewModel;

public class AppSettingsFragment extends BaseFragment {

    AppSettingsViewModel viewModel;
    View viewFragment;
    FragmentAppSettingsBinding binding;
    public static AppSettingsFragment getInstance(){
        return new AppSettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_settings, container, false);
        viewFragment = binding.getRoot();
        return viewFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(AppSettingsViewModel.class);
        switch (AppConstants.VIEW_STACK){
            case AppConstants.VIEW_STACK_BLOG_MESSAGE_EVENT:
                binding.rbBlogMessage.setChecked(true);
                break;
            case AppConstants.VIEW_STACK_MESSAGE_BLOG_EVENT:
                binding.rbMessageBlog.setChecked(true);
                break;
        }
        binding.rgViewPreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
                    case R.id.rb_blog_message:
                        AppConstants.VIEW_STACK=AppConstants.VIEW_STACK_BLOG_MESSAGE_EVENT;
                        break;
                    case R.id.rb_message_blog:
                        AppConstants.VIEW_STACK=AppConstants.VIEW_STACK_MESSAGE_BLOG_EVENT;
                        break;
                }
            }
        });
    }
}
