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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BaseFragment extends Fragment {

    public static final String PROGRESS_UPDATE = "progress_update";


    public void hideGlobalProgressBar() {
        ((MainActivity) getActivity()).hideGlobalProgressBar();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
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

    public void closeCurrentFragment(){
        ((MainActivity) getActivity()).removeFragment();
    }

    public void addFragment(Fragment fragment) {
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        ((MainActivity) getActivity()).replaceFragment(fragment);
    }

    public void showLogoutButton(){
        ((MainActivity) getActivity()).showLogoutButton();
    }

    public void hideLogoutButton(){
        ((MainActivity) getActivity()).hideLogoutButton();
    }
}
