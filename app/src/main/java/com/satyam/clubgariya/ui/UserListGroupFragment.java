package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.UsersListAdapter;
import com.satyam.clubgariya.adapters.UsersListGroupAdapter;
import com.satyam.clubgariya.callbacks.CreateChatGroup;
import com.satyam.clubgariya.callbacks.UserGroupListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.UserListFragmentBinding;
import com.satyam.clubgariya.databinding.UserListGroupFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.UserListGroupViewModel;
import com.satyam.clubgariya.viewmodels.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserListGroupFragment extends BaseFragment implements UserGroupListner, CreateChatGroup {

    private static final String TAG = "UserListFragment";
    private UserListGroupViewModel mViewModel;
    private List<User> clubUsers;
    private List<User> clubUsersForGroup;
    private UsersListGroupAdapter usersListAdapter;
    private UserListGroupFragmentBinding binding;
    private static String calledBy;
    private int groupUserCount;


    public static UserListGroupFragment newInstance(String source) {
        calledBy = source;
        return new UserListGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_group_fragment, container, false);

        clubUsers = new ArrayList<>();
        clubUsersForGroup = new ArrayList<>();
        binding.rvUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListAdapter = new UsersListGroupAdapter(getContext(), clubUsers, this);
        getAllClubContacts();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserListGroupViewModel.class);
        mViewModel.setGroupListner(this);
        binding.rvUserList.setAdapter(usersListAdapter);
        binding.btnCreateGroup.setOnClickListener(view -> {
            addFragment(CreateGroupFragment.newInstance(clubUsersForGroup,calledBy),true);
//            mViewModel.createGroup(clubUsersForGroup, "OneClick Users", "OneClick User Group", "");
        });

        // TODO: Use the ViewModel
    }


    public void getAllClubContacts() {

        AppDatabase.getInstance(getContext()).userDao().getClubContactListExcludeNumber(true, CurrentUserData.getInstance().getUserMobile()).observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> appContacts) {
                clubUsers = appContacts;
                for (User con : clubUsers) {
//                    Log.e(TAG, "onChanged: Name  " + con.getDisplayName() + "  Numnet  " + con.mobileNumber);
                    usersListAdapter.updateListValue(clubUsers);
                }
                //               syncContactWithClubMember();
            }
        });

    }

    public void onCreateButton() {

    }


    @Override
    public void onCHatUserChecked(User user) {
        if (groupUserCount < 99) {

        }
        groupUserCount++;
        clubUsersForGroup.add(user);
        if (groupUserCount > 1)
            binding.btnCreateGroup.setEnabled(true);
    }

    @Override
    public void onCHatUserUnChecked(User user) {
        if (clubUsersForGroup.contains(user)) {
            groupUserCount--;
            clubUsersForGroup.remove(user);
            Log.e(TAG, "onCHatUserUnChecked: ");
        }
        if (groupUserCount < 2)
            binding.btnCreateGroup.setEnabled(false);
    }

    @Override
    public void onGroupCreatedSuccess(ChatReference reference, TransactionReference transactionReference) {
        switch (calledBy) {
            case AppConstants.USER_LIST_FOR_CHAT:
                addFragment(ChatFragment.newInstance(null,reference), true);
                break;
            case AppConstants.USER_LIST_FOR_TRANSACTION:
                addFragment(TransactionFragment.newInstance(null,transactionReference), true);
                break;

        }
    }

    @Override
    public void onGroupCreatedFail() {
        Log.e(TAG, "onGroupCreatedFail: ");
    }
}