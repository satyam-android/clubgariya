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
import com.satyam.clubgariya.callbacks.RegisterViewModelListner;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.FragmentRegisterBinding;
import com.satyam.clubgariya.utils.ViewUtils;
import com.satyam.clubgariya.viewmodels.RegisterFragViewModal;

public class RegisterFragment extends BaseFragment implements RegisterViewModelListner {

    View fragmentView;
    FragmentRegisterBinding binding;

    RegisterFragViewModal fragViewModal;

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        fragmentView = binding.getRoot();
        fragViewModal= ViewModelProviders.of(this).get(RegisterFragViewModal.class);
        fragViewModal.listner=this;
        binding.setRegister(fragViewModal);
        shoAppBar("Register");
        hideLogoutButton();

        return fragmentView;
    }

    @Override
    public void onRegisterSuccess(User user) {
        setUserData(user);
        replaceFragment(HomeFragment.getInstance(),false);
    }

    @Override
    public void onRegisterStart() {

    }

    @Override
    public void onRegisterFail(String message) {
        ViewUtils.showToast(message,getContext());
    }
}
