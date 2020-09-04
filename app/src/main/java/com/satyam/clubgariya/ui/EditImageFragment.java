package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserProfileCallback;
import com.satyam.clubgariya.databinding.EditImageFragmentBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditImageFragment extends DialogFragment {
    private static final String TAG = "EditImageFragment";
    private EditImageViewModel mViewModel;
    private static Uri imageUri;
    public CropImageView cropImageView;
    private View fragView;
    private EditImageFragmentBinding binding;
    private static UserProfileCallback callback;

    public static EditImageFragment newInstance(Uri imageLink,UserProfileCallback userProfileCallback) {
        imageUri=imageLink;
        callback=userProfileCallback;
        return new EditImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.edit_image_fragment, container, false);
        cropImageView=binding.cropImageView;
        fragView=binding.cropImageView;
        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditImageViewModel.class);
        CropImage.activity()
                .start(getContext(), this);
        // TODO: Use the ViewModel
    }

}