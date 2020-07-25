package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatUsersListAdapter;
import com.satyam.clubgariya.callbacks.UserChatListListner;
import com.satyam.clubgariya.database.AppContactDB;
import com.satyam.clubgariya.database.tables.AppContact;
import com.satyam.clubgariya.databinding.ChatListFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatUserListFragment extends BaseFragment implements UserChatListListner {
    private static final String TAG = "ChatUserListFragment";
    private ChatListViewModel mViewModel;
    private ChatListFragmentBinding binding;
    private View fragView;
    private List<AppContact> clubUsers;
    private ChatUsersListAdapter usersListAdapter;


    public static ChatUserListFragment newInstance() {
        return new ChatUserListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_list_fragment, container, false);
        fragView = binding.getRoot();
        clubUsers = new ArrayList<>();
        binding.rcvUserChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListAdapter = new ChatUsersListAdapter(getContext(), clubUsers,this);
        getAllClubContacts();
        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatListViewModel.class);
        binding.rcvUserChatList.setAdapter(usersListAdapter);
        // TODO: Use the ViewModel
    }

    public void getAllClubContacts() {

        AppContactDB.getInstance(getContext()).appContactDao().getClubContactList(true).observe(getActivity(), new Observer<List<AppContact>>() {
            @Override
            public void onChanged(List<AppContact> appContacts) {
                clubUsers = appContacts;
                for (AppContact con : clubUsers) {
                    Log.e(TAG, "onChanged: Name  " + con.getDisplayName() + "  Numnet  " + con.mobileNumber);
                    usersListAdapter.updateListValue(clubUsers);
                }
                //               syncContactWithClubMember();
            }
        });

    }

    @Override
    public void onUserClick(AppContact appContact) {
        replaceFragment(ChatFragment.newInstance(appContact));
    }
}