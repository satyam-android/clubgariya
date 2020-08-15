package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.UsersListAdapter;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.UserListFragmentBinding;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends BaseFragment implements UserListListner {

    private static final String TAG = "UserListFragment";
    private UserListViewModel mViewModel;
    private List<User> clubUsers;
    private UsersListAdapter usersListAdapter;
    private UserListFragmentBinding binding;
    private static String calledBy;


    public static UserListFragment newInstance(String source) {
        calledBy = source;
        return new UserListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_fragment, container, false);

        clubUsers = new ArrayList<>();
        binding.rvUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListAdapter = new UsersListAdapter(getContext(), clubUsers, this);
        getAllClubContacts();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        binding.rvUserList.setAdapter(usersListAdapter);

        // TODO: Use the ViewModel
    }


    public void getAllClubContacts() {

        AppDatabase.getInstance(getContext()).userDao().getClubContactList(true).observe(getActivity(), new Observer<List<User>>() {
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

    @Override
    public void onUserClick(String uid) {
        switch (calledBy) {
            case AppConstants.USER_LIST_FOR_CHAT:
              addFragment(ChatFragment.newInstance(uid),true);
            break;
            case AppConstants.USER_LIST_FOR_TRANSACTION:
                addFragment(TransactionFragment.newInstance(uid),true);
                break;

        }
    }

    @Override
    public void renderingBottomItem(int count) {

    }

    @Override
    public void onChatReferenceChange(List<ChatReference> options) {

    }
}