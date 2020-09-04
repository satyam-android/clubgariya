package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
    public ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabFragmentData> fragmentList;
    public HomeFragmentTabAdapter homeFragmentTabAdapter;
    private String currentViewStack;
    private AppSharedPreference appSharedPreference;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModal = ViewModelProviders.of(this).get(HomeFragViewModal.class);
//        binding.setHomemodel(viewModal);
        shoAppBar(getString(R.string.app_name));
        appSharedPreference=new AppSharedPreference(getContext());
        currentViewStack = appSharedPreference.getStringData(AppConstants.VIEW_STACK);
        if(currentViewStack==null){
            currentViewStack=AppConstants.VIEW_STACK_MESSAGE_TRANSACTION;
            appSharedPreference.setStringData(AppConstants.VIEW_STACK,currentViewStack);
        }
        fragmentView = binding.getRoot();
        viewPager = fragmentView.findViewById(R.id.tab_pager);
        tabLayout = fragmentView.findViewById(R.id.tab_layout);
        createFragmentStack();
        homeFragmentTabAdapter = new HomeFragmentTabAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(homeFragmentTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if(fragmentList.size()==1){
            tabLayout.setVisibility(View.GONE);
            shoAppBar(fragmentList.get(0).getTitle());
        }else{
            tabLayout.setVisibility(View.VISIBLE);
            shoAppBar("OneClick");
        }
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


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setCurrentFragment(int position) {
        setCurrentFragment(fragmentList.get(position).getFragment());
    }

    public void createFragmentStack() {
        fragmentList = new ArrayList<>();
        switch (currentViewStack) {
                case AppConstants.VIEW_STACK_TRANSACTION_MESSAGE:
                fragmentList.add(new TabFragmentData(AppConstants.PAYMENTS_FRAGMENT_TITLE, TransactionUserListFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatUserListFragment.newInstance()));

                break;
            case AppConstants.VIEW_STACK_MESSAGE:
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatUserListFragment.newInstance()));
                break;
            case AppConstants.VIEW_STACK_TRANSACTION:
                fragmentList.add(new TabFragmentData(AppConstants.PAYMENTS_FRAGMENT_TITLE, TransactionUserListFragment.newInstance()));
                break;
            case AppConstants.VIEW_STACK_MESSAGE_TRANSACTION:
            default:
                fragmentList.add(new TabFragmentData(AppConstants.MESSAGE_FRAGMENT_TITLE, ChatUserListFragment.newInstance()));
                fragmentList.add(new TabFragmentData(AppConstants.PAYMENTS_FRAGMENT_TITLE, TransactionUserListFragment.newInstance()));
                break;
        }
    }

}
