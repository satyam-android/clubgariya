package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.databinding.LoginMobileFragmentBinding;

public class LoginMobileFragment extends Fragment {

    private LoginMobileViewModel mViewModel;
    private View fragmentView;
    private LoginMobileFragmentBinding binding;

    public static LoginMobileFragment newInstance() {
        return new LoginMobileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.login_mobile_fragment,container,false);
        fragmentView=binding.getRoot();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginMobileViewModel.class);
        binding.setLoginMobile(mViewModel);
        // TODO: Use the ViewModel
    }



}