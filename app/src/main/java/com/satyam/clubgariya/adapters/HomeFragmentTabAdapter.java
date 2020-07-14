package com.satyam.clubgariya.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.satyam.clubgariya.modals.TabFragmentData;
import com.satyam.clubgariya.ui.ChatFragment;
import com.satyam.clubgariya.ui.ClubBlogFragment;
import com.satyam.clubgariya.ui.EventFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentTabAdapter  extends FragmentPagerAdapter {

    List<TabFragmentData> fragments;

    public HomeFragmentTabAdapter(@NonNull FragmentManager fm,List<TabFragmentData> fragments) {
        super(fm);
     this.fragments=fragments;

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
