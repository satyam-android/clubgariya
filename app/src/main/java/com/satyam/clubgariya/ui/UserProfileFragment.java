package com.satyam.clubgariya.ui;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserProfileCallback;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.UserProfileFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.utils.ViewUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends BaseFragment implements UserProfileCallback {
    private static final String TAG = "UserProfileFragment";
    private UserProfileViewModel mViewModel;
    private UserProfileFragmentBinding binding;
    private String profileImageLocalPath;
    private Map<String, Object> objectMap;
    private List<String> listRawPath;
    private List<Uri> mediaLocalUri;
    private EditImageFragment editImageFragment;
    private static User user;


    public static UserProfileFragment newInstance(User userData) {
        user=userData;
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false);
        setCurrentFragment(this);
        listRawPath = new ArrayList<>();
        mediaLocalUri = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        mViewModel.setProfileCallback(this);
        if(user!=null)
            displayProfileData();



        binding.ivProfileImage.setOnClickListener((view -> {
            openGallery();
        }));
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileDetails();
            }
        });
    }

    private void displayProfileData(){
        if(!TextUtils.isEmpty(user.getName()))
            binding.etProfileName.setText(user.getName());
        if(!TextUtils.isEmpty(user.getUserStatus()))
            binding.etProfileStatus.setText(user.getUserStatus());
        if(!TextUtils.isEmpty(user.getImageUrl()))
            binding.ivProfileImage.setImageURI(user.getImageUrl());
        binding.tvProfileCredit.setText(String.valueOf(CurrentUserData.getInstance().getProfileCredit()));
        binding.tvProfileDebit.setText(String.valueOf(CurrentUserData.getInstance().getProfileDebit()));
        binding.tvProfileBalance.setText(String.valueOf(CurrentUserData.getInstance().getNet_Worth()));
    }

    public void setProfileDetails() {
        if (!TextUtils.isEmpty(profileImageLocalPath)) {
            uploadProfilePicture(profileImageLocalPath);
        }else if(!TextUtils.isEmpty(binding.etProfileStatus.getText().toString())){
            objectMap=new HashMap<>();
            objectMap.put("userStatus",binding.etProfileStatus.getText().toString());
            updateUserProfile(objectMap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().setClickable(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.getRoot().setClickable(false);
    }

    public void openGallery() {
        CropImage.activity()
        .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
/*
        Options options = Options.init()
                .setRequestCode(AppConstants.REQUEST_CODE_SELECT_MEDIA)
                .setCount(1) //Number of images to restict selection count
                .setExcludeVideos(true)
                .setFrontfacing(true)                                         //Front Facing camera on start
//                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/OneClick/media");

        Pix.start(getActivity(), options);
*/

       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Media(s)"), AppConstants.REQUEST_CODE_SELECT_MEDIA);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageLocalPath = result.getUri().toString();
                binding.ivProfileImage.setImageURI(result.getUri());

//                editImageFragment=EditImageFragment.newInstance(resultUri,this);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(),"Error, please try again",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void uploadProfilePicture(String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if (uri != null) {
            Uri localUri=Uri.parse(uri);
            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + UtilFunction.getInstance().getFileExtension(getContext(),localUri));
            ref.putFile(localUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            binding.ivProfileImage.setImageURI(uri);
                            objectMap=new HashMap<>();
                            objectMap.put("name",binding.etProfileName.getText().toString());
                            objectMap.put("userStatus",binding.etProfileStatus.getText().toString());
                            objectMap.put("imageUrl",uri.toString());
                            updateUserProfile(objectMap);
                        }
                    });

                }
            });
        }
    }

    public void updateUserProfile(Map<String, Object> map){
        FirebaseObjectHandler.getInstance().getUserDocumentReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onProfileSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ViewUtils.showToast(e.getMessage(),getContext());
            }
        });
    }

    @Override
    public void onProfileSuccess() {
        replaceFragment(HomeFragment.getInstance(), false);
    }

    @Override
    public void oProfileFailure() {

    }

}