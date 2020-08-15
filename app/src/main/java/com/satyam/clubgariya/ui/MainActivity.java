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
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.databinding.ActivityMainBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.UtilFunction;

import static android.Manifest.permission.READ_CONTACTS;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
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
    TextView tvProgressTitle;
    Fragment currentFragment;
    private ActivityMainBinding binding;
    private int exitBackCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        setContentView(R.layout.activity_main);
        rlAppBar = findViewById(R.id.app_bar);
        ivProfilePicture = findViewById(R.id.iv_profile_image);
        ivLogout = findViewById(R.id.iv_logout);
        tvTitleAppBar = findViewById(R.id.tv_app_title);
        progressBarContainer = findViewById(R.id.rl_progress_bar_container);
        tvProgressTitle = findViewById(R.id.tv_progress_title);
        ivOptionMenu = findViewById(R.id.iv_option_menu);
        ivOptionMenu.setOnClickListener(this);
        ivProfilePicture.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            addFragment(SplashFragment.getInstance(), false);
        }
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
        if (currentFragment instanceof TransactionUserListFragment || currentFragment instanceof TransactionFragment) {
            binding.ivCurrencyIcon.setVisibility(View.VISIBLE);
            binding.ivCurrencyIcon.setText(UtilFunction.getInstance().getLocalCurrencySymbol());
            binding.tvNetworth.setVisibility(View.VISIBLE);
            double netWorth = CurrentUserData.getInstance().getNet_Worth();
            if (netWorth > 0) {
                binding.tvNetworth.setTextColor(Color.GREEN);
            } else {
                binding.tvNetworth.setTextColor(Color.RED);
            }
            binding.tvNetworth.setText(String.valueOf(netWorth));
        } else {
            binding.ivCurrencyIcon.setVisibility(View.GONE);
            binding.tvNetworth.setVisibility(View.GONE);
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
        if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser() != null)
            implementUserProfileChange();
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
                    CurrentUserData.getInstance().setUser(snapshot.toObject(User.class));
                    ivProfilePicture.setImageURI(Uri.parse(CurrentUserData.getInstance().getUserImageUrl()));
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (TextUtils.isEmpty(progressTitle)) {
            tvProgressTitle.setVisibility(View.GONE);
        } else {
            tvProgressTitle.setVisibility(View.VISIBLE);
            tvProgressTitle.setText(progressTitle);
        }
    }

    public void hideGlobalProgressBar() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarContainer.setVisibility(View.INVISIBLE);
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
                addFragment(UserProfileFragment.newInstance(), true);
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
        } else if (currentFragment instanceof EventFragment) {
            popup.inflate(R.menu.popup_menu_chat);
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
        if(fragmentManager.getFragments().size()==2){
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
            case R.id.app_settings:
                replaceFragment(AppSettingsFragment.getInstance());
                return true;
            case R.id.message_search:
                replaceFragment(SearchMessageFragment.newInstance());
                return true;
            case R.id.app_contact_sync:
                if (checkReadContactPermission()) {
                    UtilFunction.getInstance().startContactSyncAdapter(getBaseContext());
                }
                return true;
            default:

                return false;
        }
    }


}