package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserProfileCallback;
import com.satyam.clubgariya.databinding.UserProfileFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends BaseFragment implements UserProfileCallback {

    private UserProfileViewModel mViewModel;
    private UserProfileFragmentBinding binding;
    private String profileImageLocalPath;
    private Map<String,Object> objectMap;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false);
        setCurrentFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        mViewModel.setProfileCallback(this);
        if(!TextUtils.isEmpty(CurrentUserData.getInstance().getUserImageUrl()))
            FirebaseObjectHandler.getInstance().setImageFromUrl(CurrentUserData.getInstance().getUserImageUrl(),binding.ivProfileImage);
        if(!TextUtils.isEmpty(CurrentUserData.getInstance().getUserStatus()))
            binding.etProfileStatus.setText(CurrentUserData.getInstance().getUserStatus());
        binding.tvProfileCredit.setText(String.valueOf(CurrentUserData.getInstance().getProfileCredit()));
        binding.tvProfileDebit.setText(String.valueOf(CurrentUserData.getInstance().getProfileDebit()));
        binding.tvProfileBalance.setText(String.valueOf(CurrentUserData.getInstance().getNet_Worth()));
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

    public void setProfileDetails() {
        if (!TextUtils.isEmpty(profileImageLocalPath)) {
            uploadProfilePicture(profileImageLocalPath);
        }else if(!TextUtils.isEmpty(binding.etProfileStatus.getText().toString())){
            objectMap=new HashMap<>();
            objectMap.put("status",binding.etProfileStatus.getText().toString());
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Media(s)"), AppConstants.REQUEST_CODE_SELECT_MEDIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_SELECT_MEDIA) {
                profileImageLocalPath = data.getData().toString();
                binding.ivProfileImage.setImageURI(Uri.parse(profileImageLocalPath));
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
                            objectMap.put("status",binding.etProfileStatus.getText().toString());
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