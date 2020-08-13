package com.satyam.clubgariya.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.onesignal.OneSignal;
import com.satyam.clubgariya.callbacks.IBaseFragmentListner;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.viewmodels.BaseFragmentViewModel;

import java.util.List;

public abstract class BaseFragment extends Fragment implements IBaseFragmentListner {

    private static final String TAG = "BaseFragment";
    public static final String PROGRESS_UPDATE = "progress_update";
    private MutableLiveData<List<ChatReference>> chatReferences;
    private MutableLiveData<List<TransactionReference>> transactionReferences;

    private BaseFragmentViewModel viewModel;

    public void hideGlobalProgressBar() {
        ((MainActivity) getActivity()).hideGlobalProgressBar();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BaseFragmentViewModel.class);
        viewModel.setFragmentListner(this);
        chatReferences = new MutableLiveData<>();
        transactionReferences = new MutableLiveData<>();
        OneSignal.setSubscription(true);
        registerReceiver();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void registerReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PROGRESS_UPDATE);
        bManager.registerReceiver(mBroadcastReceiver, intentFilter);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null)
            getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onChatReferenceChange(List<ChatReference> options) {
        chatReferences.setValue(options);

    }

    public MutableLiveData<List<ChatReference>> getChatReferences() {

        return chatReferences;
    }

    @Override
    public void onTransactionReferenceChange(List<TransactionReference> transactionReferences) {
        this.transactionReferences.setValue(transactionReferences);
    }

    public MutableLiveData<List<TransactionReference>> getTransactionReferences() {

        return transactionReferences;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(PROGRESS_UPDATE)) {

                boolean downloadComplete = intent.getBooleanExtra("downloadComplete", false);
                //Log.d("API123", download.getProgress() + " current progress");

                if (downloadComplete) {

//                    Toast.makeText(getContext(), "File download completed", Toast.LENGTH_SHORT).show();

//                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +
//                            "journaldev-image-downloaded.jpg");
//
//                    Picasso.get().load(file).into(imageView);

                }
            }
        }
    };


    public void showGlobalProgressBar(String message) {
        ((MainActivity) getActivity()).showGlobalProgressBar(message);
    }

    public void setCurrentFragment(Fragment currentFragment) {
        ((MainActivity) getActivity()).setCurrentFragment(currentFragment);

    }

    public void hideAppBar() {
        ((MainActivity) getActivity()).hideAppBar();
    }

    public void shoAppBar(String title) {
        ((MainActivity) getActivity()).showAppBar(title);

    }

    public void closeCurrentFragment() {
        ((MainActivity) getActivity()).removeFragment();
    }

    public void addFragment(Fragment fragment,boolean addToBackStack) {
        if(getActivity()!=null)
        ((MainActivity) getActivity()).addFragment(fragment,addToBackStack);
    }

    public void replaceFragment(Fragment fragment,boolean addToBackStack) {
        if (getActivity() != null)
            ((MainActivity) getActivity()).replaceFragment(fragment);
    }

    public void showLogoutButton() {
        ((MainActivity) getActivity()).showLogoutButton();
    }

    public void hideLogoutButton() {
        ((MainActivity) getActivity()).hideLogoutButton();
    }


    @Override
    public void onProfileUpdate() {
        if(getActivity()!=null)
        ((MainActivity) getActivity()).updateUserNetWorth();
    }

}
