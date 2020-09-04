package com.satyam.clubgariya.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.MainActivityListners;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.databinding.ActivityMainBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.viewmodels.MainActivityViewModel;

import static android.Manifest.permission.READ_CONTACTS;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, MainActivityListners {
    private static final String TAG = "MainActivity";
    ConstraintLayout rlAppBar;
    SimpleDraweeView ivProfilePicture;
    ImageView ivLogout;
    TextView tvTitleAppBar;
    ImageView ivOptionMenu;
    //    AppFragmentManager appFragmentManager;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    RelativeLayout progressBarContainer;
    Fragment currentFragment;
    private ActivityMainBinding binding;
    private int exitBackCount;
    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        Log.e(TAG, "onCreate: ");
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.setListner(this);
//        setContentView(R.layout.activity_main);
        rlAppBar = findViewById(R.id.app_bar);
        ivProfilePicture = findViewById(R.id.iv_profile_image);
        ivLogout = findViewById(R.id.iv_logout);
        tvTitleAppBar = findViewById(R.id.tv_app_title);
        progressBarContainer = findViewById(R.id.rl_progress_bar_container);
        ivOptionMenu = findViewById(R.id.iv_option_menu);
        ivOptionMenu.setOnClickListener(this);
        ivProfilePicture.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            addFragment(SplashFragment.getInstance(), false);
        }
    }

    public void setProgressbarProgress(int progress) {
        binding.progressbar.setProgress(progress);
    }

    public void startListeningToDataChange(String uid) {
        if (!TextUtils.isEmpty(uid))
            mViewModel.listenToUserDetailChange(uid);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment != null)
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null)
            currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUserNetWorth() {
        if (CurrentUserData.getInstance().getUser() != null) {
            binding.ivCurrencyIcon.setText(UtilFunction.getInstance().getLocalCurrencySymbol(getApplication()));
            double netWorth = CurrentUserData.getInstance().getNet_Worth();
            if (netWorth > 0) {
                binding.tvNetworth.setTextColor(Color.RED);
            } else {
                binding.tvNetworth.setTextColor(Color.GREEN);
            }
            binding.tvNetworth.setText(String.valueOf(Math.abs(netWorth)));
            if (currentFragment instanceof TransactionUserListFragment || currentFragment instanceof TransactionFragment) {
                binding.ivCurrencyIcon.setVisibility(View.VISIBLE);
                binding.tvNetworth.setVisibility(View.VISIBLE);
            } else {
                binding.ivCurrencyIcon.setVisibility(View.GONE);
                binding.tvNetworth.setVisibility(View.GONE);
            }
        } else {

        }
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
        updateUserNetWorth();
    }

    public void hideAppBar() {
        rlAppBar.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser() != null)
            implementUserProfileChange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    public void implementUserProfileChange() {
        FirebaseObjectHandler.getInstance().getUserDocumentReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        Log.e(TAG, "User Profile Changed: " );
                        User user=snapshot.toObject(User.class);
                        user.setUid(snapshot.getId());
                        onUserDataChange(user);

                    }catch (Exception e1){
                        e1.printStackTrace();
                        Log.e(TAG, "onEvent: "+e1.getLocalizedMessage() );
                    }
//                    if(!TextUtils.isEmpty(CurrentUserData.getInstance().getUserImageUrl()))
//                    ivProfilePicture.setImageURI(Uri.parse(CurrentUserData.getInstance().getUserImageUrl()));
//                    FirebaseObjectHandler.getInstance().setImageFromUrl(CurrentUserData.getInstance().getUserImageUrl(), ivProfilePicture);
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });
    }

    public boolean checkReadContactPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void hideLogoutButton() {
        ivLogout.setVisibility(View.GONE);
    }

    public void showLogoutButton() {
        ivLogout.setVisibility(View.VISIBLE);
    }


    public void showGlobalProgressBar(String progressTitle) {
        progressBarContainer.setVisibility(View.VISIBLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideGlobalProgressBar() {
        progressBarContainer.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void showAppBar(String title) {

        rlAppBar.setVisibility(View.VISIBLE);
        tvTitleAppBar.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_option_menu:
                showOptionMenu(view);
                break;
            case R.id.iv_profile_image:
                addFragment(UserProfileFragment.newInstance(CurrentUserData.getInstance().getUser()), true);
                break;

            case R.id.iv_logout:
                FirebaseAuth.getInstance().signOut();
                replaceFragment(LoginFragment.getInstance());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(getApplicationContext()).userDao().deleteContactTable();
                    }
                }).start();
                break;
        }
    }

    public void showOptionMenu(View v) {
        PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.setOnMenuItemClickListener(this);
        if (currentFragment instanceof ClubBlogFragment) {
            popup.inflate(R.menu.popup_menu_blog);
        } else if (currentFragment instanceof ChatUserListFragment) {
            popup.inflate(R.menu.popup_menu_chat);
        } else if (currentFragment instanceof TransactionUserListFragment) {
            popup.inflate(R.menu.popup_menu_transaction);
        }
        popup.show();
    }


    public void addFragment(Fragment fragment, boolean addToBackStack) {
//        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
//            Log.e(TAG, "before add commit: "+fragmentManager.getFragments().get(i).getClass().getName() );
//        }
//        Log.e(TAG, "adding Fragment: "+fragment.getClass().getName() );

        ft = fragmentManager.beginTransaction();
        ft.add(R.id.container, fragment);
        if (addToBackStack)
            ft.addToBackStack(fragment.getClass().getName());
        else ft.addToBackStack(null);
        ft.commit();
//        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
//            Log.e(TAG, "after add commit: "+fragmentManager.getFragments().get(i).getClass().getName() );
//        }
    }

    public void replaceFragment(Fragment fragment) {
//        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
//            Log.e(TAG, "before replace commit: "+fragmentManager.getFragments().get(i).getClass().getName() );
//        }
//        Log.e(TAG, "replacing fragment  Fragment: "+fragment.getClass().getName() );
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment);
//        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
//        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
//            Log.e(TAG, "after replace commit: "+fragmentManager.getFragments().get(i).getClass().getName() );
//        }


    }

    @Override
    public void onBackPressed() {
        removeFragment();
    }

    public void removeFragment() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            exitBackCount = 0;
            fragmentManager.popBackStack();
            return;
        } else if (fragmentManager.getFragments().size() == 2 && fragmentManager.getFragments().get(1) instanceof HomeFragment && ((HomeFragment) fragmentManager.getFragments().get(1)).viewPager.getCurrentItem() > 0) {
            exitBackCount = 0;
            ViewPager viewPager = ((HomeFragment) fragmentManager.getFragments().get(1)).viewPager;
            if (viewPager != null) {
                viewPager.setCurrentItem(0);
            }
            return;
        }
        if (fragmentManager.getFragments().size() == 2) {
            exitBackCount++;
            if (exitBackCount > 1) {
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "Press back to exit", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onBackPressed();


   /*     if(fragmentManager.getBackStackEntryCount() > 0){
                fragmentManager.popBackStack();
                return;
        }
        if(fragmentManager.getFragments().get(1) instanceof HomeFragment){
            if(((HomeFragment) fragmentManager.getFragments().get(1)).viewPager.getCurrentItem()>0){
                ((HomeFragment) fragmentManager.getFragments().get(1)).viewPager.setCurrentItem(0);
            }else {
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }*/
     /*   if(currentFragment instanceof HomeFragment){

        }
        if(fragmentManager.getBackStackEntryCount()>1) {
            ft=fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            ft.commit();
        }else {
            ViewUtils.showToast("Last Fragment",this);
        }*/

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(getBaseContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.profile_settings:
                addFragment(UserProfileFragment.newInstance(CurrentUserData.getInstance().getUser()), true);
                return true;
            case R.id.app_settings:
                replaceFragment(AppSettingsFragment.getInstance());
                return true;
            case R.id.create_transaction_group:
                addFragment(UserListGroupFragment.newInstance(AppConstants.USER_LIST_FOR_TRANSACTION), true);
                return true;
            case R.id.create_chat_group:
                addFragment(UserListGroupFragment.newInstance(AppConstants.USER_LIST_FOR_CHAT), true);
                return true;
            case R.id.app_contact_sync:
                if (checkReadContactPermission()) {
                    new AppSharedPreference(getApplicationContext()).setStringData(AppConstants.DATABASE_CONTACT_SYNC_DATE,"");
                    UtilFunction.getInstance().startContactSyncAdapter(getBaseContext());
                }
                return true;
            default:

                return false;
        }
    }


    @Override
    public void onUserDataChange(User user) {
        if (user != null) {
            if(!TextUtils.isEmpty(user.getImageUrl()))
            binding.ivProfileImage.setImageURI(user.getImageUrl());
            AppDatabaseHelper.getInstance(getApplication()).getUserByUid(user.getUid(), new AppDatabaseHelper.GetUserDetail() {
                @Override
                public void onUserSuccess(User userLocal) {
                    if(userLocal!=null && !TextUtils.isEmpty(userLocal.getName()))
                        user.setName(userLocal.getName());
                    CurrentUserData.getInstance().setUser(user);
                    updateUserNetWorth();

                }
            });

        /*    AppDatabase.getInstance(getApplicationContext()).userDao().getNameByMobile(CurrentUserData.getInstance().getUserMobile()).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (!TextUtils.isEmpty(s))
                        user.setName(s);
                    Log.e(TAG, "onChanged: "+s );
                    CurrentUserData.getInstance().setUser(user);
                    updateUserNetWorth();
                }
            });*/
        }
    }
}