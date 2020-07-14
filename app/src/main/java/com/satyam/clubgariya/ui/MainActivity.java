package com.satyam.clubgariya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.repositories.ClubBlogRepository;
import com.satyam.clubgariya.ui.HomeFragment;
import com.satyam.clubgariya.ui.LoginFragment;
import com.satyam.clubgariya.utils.AppFragmentManager;
import com.satyam.clubgariya.utils.ViewUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    RelativeLayout rlAppBar;
    ImageView ivBackArrow;
    ImageView ivLogout;
    TextView tvTitleAppBar;
    ImageView ivOptionMenu;
//    AppFragmentManager appFragmentManager;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    RelativeLayout progressBarContainer;
    TextView tvProgressTitle;
    Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        rlAppBar = findViewById(R.id.app_bar);
        ivBackArrow = findViewById(R.id.iv_back_arrow);
        ivLogout = findViewById(R.id.iv_logout);
        tvTitleAppBar = findViewById(R.id.tv_app_title);
        progressBarContainer = findViewById(R.id.rl_progress_bar_container);
        tvProgressTitle = findViewById(R.id.tv_progress_title);
        ivOptionMenu = findViewById(R.id.iv_option_menu);
        ivOptionMenu.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        fragmentManager=getSupportFragmentManager();
//        appFragmentManager = AppFragmentManager.getInstance(this,getSupportFragmentManager());
        if (savedInstanceState == null) {
            replaceFragment(SplashFragment.getInstance());
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, LoginFragment.getInstance())
//                    .commitNow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null)
            currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void hideAppBar() {
        rlAppBar.setVisibility(View.GONE);

    }

    public void hideLogoutButton(){
        ivLogout.setVisibility(View.GONE);
    }

    public void showLogoutButton(){
        ivLogout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        removeFragment();
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
            case R.id.iv_back_arrow:
               removeFragment();
                break;

            case R.id.iv_logout:
                FirebaseAuth.getInstance().signOut();
                replaceFragment(LoginFragment.getInstance());
                break;
        }
    }

    public void showOptionMenu(View v){
        PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.setOnMenuItemClickListener(this);
        if(currentFragment instanceof ClubBlogFragment){
            popup.inflate(R.menu.popup_menu_blog);
        }else if(currentFragment instanceof ChatFragment){
            popup.inflate(R.menu.popup_menu_chat);
        }
        popup.show();
    }



    public void addFragment(Fragment fragment) {
        ft=fragmentManager.beginTransaction();
        ft.add(R.id.container,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void replaceFragment(Fragment fragment) {
        ft=fragmentManager.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void removeFragment() {
        if(currentFragment instanceof HomeFragment){

        }
        if(fragmentManager.getBackStackEntryCount()>1) {
            ft=fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            ft.commit();
        }else {
            ViewUtils.showToast("Last Fragment",this);
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(getBaseContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.search_item:
                // do your code
                return true;
            case R.id.upload_item:
                // do your code
                return true;
            case R.id.copy_item:
                // do your code
                return true;
            case R.id.print_item:
                // do your code
                return true;
            case R.id.share_item:
                // do your code
                return true;
            case R.id.bookmark_item:
                // do your code
                return true;
            default:

                return false;
        }
    }
}