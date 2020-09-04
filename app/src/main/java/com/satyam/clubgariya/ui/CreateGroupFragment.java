package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.CreateChatGroup;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.CreateGroupFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.UtilFunction;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateGroupFragment extends BaseFragment implements CreateChatGroup {

    private CreateGroupViewModel mViewModel;
    private static List<User> clubUsersForGroup;
    private CreateGroupFragmentBinding binding;
    private String profileImageLocalPath;
    private static String calledBy;

    public static CreateGroupFragment newInstance(List<User> listUsersGroup, String called_by) {
        calledBy = called_by;
        clubUsersForGroup = listUsersGroup;
        return new CreateGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.create_group_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateGroupViewModel.class);
        mViewModel.setGroupListner(this);
        binding.btnCreate.setOnClickListener(view -> {
            if (isValidGroupName())
                if (!TextUtils.isEmpty(profileImageLocalPath))
                    uploadProfilePicture(profileImageLocalPath);
                else
                    mViewModel.createGroup(clubUsersForGroup, binding.etGroupName.getText().toString(), binding.etGroupDescription.getText().toString(), profileImageLocalPath,calledBy);
            else Toast.makeText(getContext(), "Group Already Exist", Toast.LENGTH_LONG).show();
        });
        binding.ivGroupImage.setOnClickListener(view -> {
            openGallery();
        });
    }

    public boolean isValidGroupName() {
        boolean isValid = true;
        List<String> groupName;
        if (calledBy.equalsIgnoreCase(AppConstants.USER_LIST_FOR_TRANSACTION))
            groupName = CurrentUserData.getInstance().getTransactionGroup();
        else groupName = CurrentUserData.getInstance().getChatGroups();

        if (groupName == null) {
            return isValid;
        } else {
            for (String name : groupName) {
                if (name.equalsIgnoreCase(binding.etGroupName.getText().toString()))
                    isValid = false;
            }
        }

        return isValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageLocalPath = result.getUri().toString();
                binding.ivGroupImage.setImageURI(result.getUri());

//                editImageFragment=EditImageFragment.newInstance(resultUri,this);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
    }

    public void uploadProfilePicture(String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if (uri != null) {
            Uri localUri = Uri.parse(uri);
            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + UtilFunction.getInstance().getFileExtension(getContext(), localUri));
            ref.putFile(localUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mViewModel.createGroup(clubUsersForGroup, binding.etGroupName.getText().toString(), binding.etGroupDescription.getText().toString(), uri.toString(),calledBy);

//                            binding.ivProfileImage.setImageURI(uri);
//                            objectMap = new HashMap<>();
//                            objectMap.put("userStatus", binding.etProfileStatus.getText().toString());
//                            objectMap.put("imageUrl", uri.toString());
//                            updateUserProfile(objectMap);
                        }
                    });

                }
            });
        }
    }


    @Override
    public void onGroupCreatedSuccess(ChatReference reference, TransactionReference transactionReference) {
        switch (calledBy) {
            case AppConstants.USER_LIST_FOR_CHAT:
                addFragment(ChatFragment.newInstance(null, reference), true);
                break;
            case AppConstants.USER_LIST_FOR_TRANSACTION:
                addFragment(TransactionFragment.newInstance("", transactionReference), true);
                break;

        }
    }

    @Override
    public void onGroupCreatedFail() {

    }
}