package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.databinding.FragmentAppSettingsBinding;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.viewmodels.AppSettingsViewModel;

public class AppSettingsFragment extends BaseFragment {

    AppSettingsViewModel viewModel;
    View viewFragment;
    FragmentAppSettingsBinding binding;
    private AppSharedPreference appSharedPreference;
    public static AppSettingsFragment getInstance(){
        return new AppSettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_settings, container, false);
        viewFragment = binding.getRoot();
        appSharedPreference=new AppSharedPreference(getContext());
        binding.btnSettingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(HomeFragment.getInstance(),false);
            }
        });
        return viewFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(AppSettingsViewModel.class);
        switch (appSharedPreference.getStringData(AppConstants.VIEW_STACK)){
            case AppConstants.VIEW_STACK_MESSAGE_TRANSACTION:
                binding.rbMessagePayment.setChecked(true);
                break;
            case AppConstants.VIEW_STACK_TRANSACTION_MESSAGE:
                binding.rbPaymentMessage.setChecked(true);
                break;
            case AppConstants.VIEW_STACK_MESSAGE:
                binding.rbMessage.setChecked(true);
                break;
            case AppConstants.VIEW_STACK_TRANSACTION:
                binding.rbPayment.setChecked(true);
                break;
        }
        binding.rgViewPreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
                    case R.id.rb_message_payment:
                        appSharedPreference.setStringData(AppConstants.VIEW_STACK,AppConstants.VIEW_STACK_MESSAGE_TRANSACTION);
                        break;
                    case R.id.rb_payment_message:
                        appSharedPreference.setStringData(AppConstants.VIEW_STACK,AppConstants.VIEW_STACK_TRANSACTION_MESSAGE);
                        break;
                    case R.id.rb_message:
                        appSharedPreference.setStringData(AppConstants.VIEW_STACK,AppConstants.VIEW_STACK_MESSAGE);
                        break;
                    case R.id.rb_payment:
                        appSharedPreference.setStringData(AppConstants.VIEW_STACK,AppConstants.VIEW_STACK_TRANSACTION);
                        break;
                }
            }
        });
    }
}
