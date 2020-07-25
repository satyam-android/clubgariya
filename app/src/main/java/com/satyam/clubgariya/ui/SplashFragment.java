package com.satyam.clubgariya.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ISplashModelCallback;
import com.satyam.clubgariya.databinding.FragmentSplashBinding;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.viewmodels.SplashViewModel;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashFragment extends BaseFragment implements ISplashModelCallback {

    private static final String TAG = "SplashFragment";
    private int PERMISSION_REQUEST_CODE = 001;
    private View fragmentView;
    private SplashViewModel viewModel;
    private FragmentSplashBinding binding;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        hideAppBar();
        setCurrentFragment(this);
        fragmentView = binding.getRoot();
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        binding.setSplashModel(viewModel);
        viewModel.setListner(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission()) {
            requestPermission();
        } else {
            checkForLoginUser();
        }
 /*       new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewModel.getUserDetail();
            }
        }, 2000);*/
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_CONTACTS);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_PHONE_STATE);
        int result3 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED   && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_CONTACTS, CAMERA,READ_PHONE_STATE,WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean contactAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean phoneStateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean writeAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (contactAccepted && cameraAccepted && phoneStateAccepted && writeAccepted)
                    checkForLoginUser();
                else {
//                    Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{READ_CONTACTS, CAMERA,READ_PHONE_STATE,WRITE_EXTERNAL_STORAGE},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void checkForLoginUser() {
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                viewModel.getUserDetail();
            }
        });
    }

    @Override
    public void onSuccess() {

        UtilFunction.getInstance().startContactSyncAdapter(getContext());
        replaceFragment(HomeFragment.getInstance());
    }

    @Override
    public void onFailure(String message) {
//        replaceFragment(LoginFragment.getInstance());
        replaceFragment(LoginMobileFragment.newInstance());

    }
}
