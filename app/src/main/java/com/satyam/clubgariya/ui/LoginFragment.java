package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.LoginViewModalListner;
import com.satyam.clubgariya.databinding.FragmentLoginBinding;
import com.satyam.clubgariya.modals.LoginError;
import com.satyam.clubgariya.utils.ViewUtils;
import com.satyam.clubgariya.viewmodels.LoginFragViewModal;

public class LoginFragment extends BaseFragment implements LoginViewModalListner {

    View fragmentView;
    LoginFragViewModal fragViewModal;
    FragmentLoginBinding binding;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        fragmentView = binding.getRoot();
        fragViewModal = ViewModelProviders.of(this).get(LoginFragViewModal.class);
        fragViewModal.loginViewModalListner = this;
        hideAppBar();
        setCurrentFragment(this);
        binding.setViewmodel(fragViewModal);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     /*   if (fragViewModal.firebaseAuth.getCurrentUser() != null) {
            changeFragment(HomeFragment.getInstance());
        } else {
      attachDataChaneListners();
        }*/
        attachDataChaneListners();

    }

    public void attachDataChaneListners(){
        fragViewModal.getLoginErrorData().observe(getActivity(), new Observer<LoginError>() {
            @Override
            public void onChanged(LoginError loginError) {
                if (!TextUtils.isEmpty(loginError.getUserNameError())) {
                    binding.tilEmail.setErrorEnabled(true);
                    binding.tilEmail.setError(loginError.getUserNameError());
                }else{
                    binding.tilEmail.setErrorEnabled(false);
                }
                if(!TextUtils.isEmpty(loginError.getPasswordError())){
                    binding.tilPassword.setErrorEnabled(true);
                    binding.tilPassword.setError(loginError.getPasswordError());
                }else{
                    binding.tilPassword.setErrorEnabled(false);
                }
            }
        });


        fragViewModal.getShowProgress().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    showGlobalProgressBar("Loading please wait..");
                else hideGlobalProgressBar();
            }
        });
    }

    @Override
    public void onAuthSuccess() {
        replaceFragment(HomeFragment.getInstance(),false);

    }

    @Override
    public void onAuthFailed(String reason) {
        ViewUtils.showToast(reason, getContext());

    }


    @Override
    public void changeFragment(Fragment fragment) {
        replaceFragment(fragment,false);
    }
}
