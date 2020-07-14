package com.satyam.clubgariya.ui;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.databinding.MyBlogFragmentBinding;
import com.satyam.clubgariya.viewmodels.MyBlogViewModel;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class WriteBlogFragment extends BaseFragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private MyBlogViewModel mViewModel;
    private MyBlogFragmentBinding binding;
    private View fragmentView;
//    List<BlogData> blogList;


    // request code
    private final int PICK_IMAGE_REQUEST = 22;


    public static WriteBlogFragment newInstance() {
        return new WriteBlogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_blog_fragment, container, false);
        fragmentView = binding.getRoot();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyBlogViewModel.class);
        binding.setMyBlogScreen(mViewModel);
        registerReceiver();
        binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.btnCancelNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCurrentFragment();
            }
        });

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startFileUpload();
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void startFileUpload() {

        mViewModel.uploadImage();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    startFileUpload();
                } else {

                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }




    public void changeFragment(){
        closeCurrentFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    // Select Image method
    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            mViewModel.filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                mViewModel.filePath);
                binding.ivPostImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}