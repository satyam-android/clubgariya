package com.satyam.clubgariya.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ISplashModelCallback;
import com.satyam.clubgariya.databinding.FragmentSplashBinding;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
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
    private String splashHeader;
    private String splashDescription;
    private String splashClickText;
    private String click_url="https://www.bitlooper.com";
    private SpannableString stringProductName;

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
        stringProductName = new SpannableString("OneClick by BitLooper");
        stringProductName.setSpan(new RelativeSizeSpan(0.4f), 8, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvSplashHeader.setText(stringProductName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission()) {
            requestPermission();
        } else {
            viewModel.getUserDetail();
        }
        binding.layoutSplashContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse(click_url));
                startActivity(intent);
            }
        });
    }


    public void setupSplashAdvertisement(){
        FirebaseObjectHandler.getInstance().getAppSettingsDocumentReference().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e(TAG, "onSuccess: Response " );
                    String imagePath = documentSnapshot.getString("splash_logo");
                    splashHeader = documentSnapshot.getString("splash_header");
                    splashDescription = documentSnapshot.getString("splash_description");
                    splashClickText = documentSnapshot.getString("splash_click_text");
                    click_url = documentSnapshot.getString("splash_click_url");

                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imagePath);
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                setupSplashAdvertisement(task.getResult().toString());

                            } else {
                                setupSplashAdvertisement(null);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setupSplashAdvertisement(null);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setupSplashAdvertisement(null);
            }
        });
    }



    private void setupSplashAdvertisement(String imageUri) {
        binding.tvSplashHeader.setText(splashHeader);
        binding.tvSplashDescription.setText(splashDescription);
        if (TextUtils.isEmpty(imageUri))
            Glide.with(getContext()).load(getContext().getDrawable(R.drawable.madhupur_junction)).into(binding.ivSplashLogo);
        else {
            Glide.with(getContext()).load(imageUri).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivSplashLogo);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(HomeFragment.getInstance(),false);
            }
        }, 4000);
    }

    private void setSplashView() {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Get Firebase FCM token
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        viewModel.getUserDetail();
                    }
                });
            }
        }, 4000);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_CONTACTS);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_PHONE_STATE);
        int result3 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_CONTACTS, CAMERA, READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().setClickable(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.getRoot().setClickable(false);
    }

    @Override
    public void onStop() {
        super.onStop();
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

                if (contactAccepted && cameraAccepted && phoneStateAccepted && writeAccepted) {
                    viewModel.getUserDetail();
                }
                else {
//                    Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{READ_CONTACTS, CAMERA, READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE},
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


    @Override
    public void onSuccess() {
        UtilFunction.getInstance().startContactSyncAdapter(getContext());
        setupSplashAdvertisement();
    }

    @Override
    public void onFailure(String message) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilFunction.getInstance().startContactSyncAdapter(getContext());
                addFragment(LoginMobileFragment.newInstance(),false);
            }
        }, 3000);

    }
}
