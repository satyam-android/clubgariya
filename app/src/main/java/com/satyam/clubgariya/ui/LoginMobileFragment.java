package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ILoginMobileCallback;
import com.satyam.clubgariya.databinding.LoginMobileFragmentBinding;
import com.satyam.clubgariya.viewmodels.LoginMobileViewModel;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.utils.ViewUtils;

public class LoginMobileFragment extends BaseFragment implements ILoginMobileCallback {

    private LoginMobileViewModel mViewModel;
    private View fragmentView;
    private LoginMobileFragmentBinding binding;

    public static LoginMobileFragment newInstance() {
        return new LoginMobileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_mobile_fragment, container, false);
        fragmentView = binding.getRoot();
        enableRegistration();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginMobileViewModel.class);
        binding.setMobileLogin(mViewModel);
        mViewModel.setViewCallBack(this);
        mViewModel.setActivity(getActivity());
        // TODO: Use the ViewModel
    }


    @Override
    public void onAuthenticationSuccessful() {
        ((MainActivity) getActivity()).implementUserProfileChange();
        UtilFunction.getInstance().startContactSyncAdapter(getContext());
        addFragment(UserProfileFragment.newInstance(),true);

    }

    @Override
    public void onAuthenticationFail(String message) {
        ViewUtils.showToast(message, getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().setClickable(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.getRoot().setClickable(false);
    }

    private void enableRegistration() {
        binding.etUserName.setEnabled(true);
        binding.etMobileCode.setEnabled(true);
        binding.etMobileNumber.setEnabled(true);
        binding.btnRegister.setVisibility(View.VISIBLE);
        binding.tilCode.setVisibility(View.GONE);
        binding.btnVerify.setVisibility(View.GONE);
    }

    private void enableVerification() {
        binding.etUserName.setEnabled(false);
        binding.etMobileNumber.setEnabled(false);
        binding.etMobileCode.setEnabled(false);
        binding.btnRegister.setVisibility(View.GONE);
        binding.tilCode.setVisibility(View.VISIBLE);
        binding.btnVerify.setVisibility(View.VISIBLE);
    }

    private void showOtpPopup(){

    }

    @Override
    public void onCodeSend() {
        enableVerification();
    }

}