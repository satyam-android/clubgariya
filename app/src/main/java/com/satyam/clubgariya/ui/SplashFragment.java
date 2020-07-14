package com.satyam.clubgariya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ISplashModelCallback;
import com.satyam.clubgariya.databinding.FragmentSplashBinding;
import com.satyam.clubgariya.services.ContactSyncService;
import com.satyam.clubgariya.services.FileUploadService;
import com.satyam.clubgariya.viewmodels.SplashViewModel;

public class SplashFragment extends BaseFragment implements ISplashModelCallback {


    View fragmentView;
    SplashViewModel viewModel;
    FragmentSplashBinding binding;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        hideAppBar();
        fragmentView = binding.getRoot();
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        binding.setSplashModel(viewModel);
        viewModel.setListner(this);
        Intent intent = new Intent(getContext(), ContactSyncService.class);
        ContactSyncService.enqueueWork(getContext(), intent);
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                viewModel.getUserDetail();
            }
        });

 /*       new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewModel.getUserDetail();
            }
        }, 2000);*/
    }


    @Override
    public void onSuccess() {
        replaceFragment(HomeFragment.getInstance());
    }

    @Override
    public void onFailure(String message) {
        replaceFragment(LoginFragment.getInstance());

    }
}
