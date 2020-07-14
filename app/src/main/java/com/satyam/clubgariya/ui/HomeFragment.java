package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.HomeFragmentTabAdapter;
import com.satyam.clubgariya.databinding.FragmentHomeBinding;
import com.satyam.clubgariya.modals.TabFragmentData;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.viewmodels.HomeFragViewModal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private View fragmentView;
    private HomeFragViewModal viewModal;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabFragmentData> fragmentList;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModal = ViewModelProviders.of(this).get(HomeFragViewModal.class);
        binding.setHomemodel(viewModal);
        shoAppBar("Home");
        fragmentView = binding.getRoot();
        viewPager=fragmentView.findViewById(R.id.tab_pager);
        tabLayout=fragmentView.findViewById(R.id.tab_layout);
        createFragmentStack();
        viewPager.setAdapter(new HomeFragmentTabAdapter(getChildFragmentManager(),fragmentList));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCurrentFragment(position);
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return fragmentView;
    }

    public void setCurrentFragment(int position){
        setCurrentFragment(fragmentList.get(position).getFragment());
    }

    public void createFragmentStack(){
//        String stack= new AppSharedPreference(getContext()).getStringData(AppConstants.VIEW_STACK);
        String stack= "";
        fragmentList=new ArrayList<>();
        switch (stack){
            case AppConstants.VIEW_STACK_EVENT_BLOG_MESSAGE:
                fragmentList.add(new TabFragmentData(AppConstants.EVENT_FRAGMENT_TITLE, EventFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.BLOG_FRAGMENT_TITLE,ClubBlogFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatFragment.newInstance()));
                break;

            case AppConstants.VIEW_STACK_EVENT_MESSAGE_BLOG:
                fragmentList.add(new TabFragmentData(AppConstants.EVENT_FRAGMENT_TITLE, EventFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.BLOG_FRAGMENT_TITLE,ClubBlogFragment.newInstance()));

                break;
            case AppConstants.VIEW_STACK_MESSAGE_BLOG_EVENT:
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.BLOG_FRAGMENT_TITLE,ClubBlogFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.EVENT_FRAGMENT_TITLE, EventFragment.newInstance()));
                break;
            case AppConstants.VIEW_STACK_BLOG_MESSAGE_EVENT:
            default:
                fragmentList.add(new TabFragmentData(AppConstants.BLOG_FRAGMENT_TITLE,ClubBlogFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.EVENT_FRAGMENT_TITLE, EventFragment.newInstance()));
                break;
        }
    }

}
