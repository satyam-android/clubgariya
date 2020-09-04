package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatReferenceAdapterNew;
import com.satyam.clubgariya.adapters.ChatReferenceUserAdapter;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.ReferenceDetailFragmentBinding;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;
import com.satyam.clubgariya.utils.UtilFunction;

public class ChatReferenceDetailFragment extends BaseFragment {
    private static final String TAG = "ChatReferenceDetailFrag";
    private ReferenceDetailViewModel mViewModel;
    private ReferenceDetailFragmentBinding binding;
    private static User userDetail;
    private static ChatReference chatReference;
    private ChatReferenceUserAdapter referenceAdapter;

    public static ChatReferenceDetailFragment newInstance(User user, ChatReference reference) {
        userDetail = user;
        chatReference = reference;
        return new ChatReferenceDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reference_detail_fragment, container, false);
//shoAppBar("OnClick");
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReferenceDetailViewModel.class);
        setHeaderDetail(userDetail.getName(), userDetail.getUserStatus(), userDetail.getImageUrl());
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        referenceAdapter = new ChatReferenceUserAdapter(getContext(), chatReference.getUsers(), null);
        binding.rvMembers.setAdapter(referenceAdapter);
        // TODO: Use the ViewModel
    }

    public void enableEdit(){

    }

    private void dissabelEdit(){

    }


    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().setClickable(true);
    }

    @Override
    public void onStop() {
        super.onStop();
//        hideAppBar();
        binding.getRoot().setClickable(false);
    }

    public void setHeaderDetail(String name, String status, String profileImage) {
        if (!TextUtils.isEmpty(name))
            binding.etName.setText(name);
        if (!TextUtils.isEmpty(status))
            binding.etDetail.setText(status);
        if (!TextUtils.isEmpty(profileImage)) {
            binding.ivProfileImage.setImageURI(profileImage);
            Log.e(TAG, "setHeaderDetail: " + profileImage);
            binding.tvCreatedAt.setText(DateTimeUtilityFunctions.getInstance().timeStampToDateTime("dd/MM/yyyy HH:mm", userDetail.getCreationTime()));
        }
    }

}