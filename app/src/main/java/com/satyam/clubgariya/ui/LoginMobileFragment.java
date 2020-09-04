package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.hbb20.CountryCodePicker;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ILoginMobileCallback;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.LoginMobileFragmentBinding;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
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
        binding.btnChangeNumber.setOnClickListener(view->{
            enableRegistration();
        });
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                mViewModel.setCountryCode(binding.ccp.getSelectedCountryCodeWithPlus());
                binding.etMobileCode.setText(binding.ccp.getSelectedCountryCodeWithPlus());
                new AppSharedPreference(getContext()).setStringData(AppConstants.USER_COUNTRY_CODE,binding.ccp.getSelectedCountryNameCode());
            }
        });
        // TODO: Use the ViewModel
    }


    @Override
    public void onAuthenticationSuccessful(User user) {
        ((MainActivity) getActivity()).implementUserProfileChange();
//        setUserData(user);
        UtilFunction.getInstance().startContactSyncAdapter(getContext());
        addFragment(UserProfileFragment.newInstance(user),true);

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
        binding.btnChangeNumber.setVisibility(View.GONE);
//        binding.ccp.setFocusable(true);
//        binding.ccp.setClickable(true);
        binding.ccp.setEnabled(true);
    }

    private void enableVerification() {
        binding.etUserName.setEnabled(false);
        binding.etMobileNumber.setEnabled(false);
        binding.etMobileCode.setEnabled(false);
        binding.btnRegister.setVisibility(View.GONE);
        binding.tilCode.setVisibility(View.VISIBLE);
        binding.btnVerify.setVisibility(View.VISIBLE);
        binding.btnChangeNumber.setVisibility(View.VISIBLE);
//        binding.ccp.setFocusable(false);
//        binding.ccp.setClickable(false);
        binding.ccp.setEnabled(false);



    }

    private void showOtpPopup(){

    }

    @Override
    public void onCodeSend() {
        enableVerification();
    }

}