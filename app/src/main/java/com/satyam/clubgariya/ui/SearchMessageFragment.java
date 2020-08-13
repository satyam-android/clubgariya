package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.UsersListAdapter;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.UserDB;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.SearchMessageFragmentBinding;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.viewmodels.SearchMessageViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchMessageFragment extends BaseFragment implements UserListListner {

    private SearchMessageViewModel mViewModel;
    private SearchMessageFragmentBinding binding;
    private View fragView;
    private List<User> appContactList;
    private UsersListAdapter usersListAdapter;

    public static SearchMessageFragment newInstance() {
        return new SearchMessageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_message_fragment, container, false);
        fragView = binding.getRoot();
        appContactList = new ArrayList<>();
        setCurrentFragment(this);
        binding.rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListAdapter = new UsersListAdapter(getContext(), appContactList, this);
        binding.rvSearchResult.setAdapter(usersListAdapter);
        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchMessageViewModel.class);
        UserDB.getInstance(getContext()).userDao().getClubContactList(true).observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> appContacts) {
                appContactList = appContacts;
                usersListAdapter.updateListValue(appContactList);
            }
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                usersListAdapter.filter(binding.etSearch.getText().toString());
                usersListAdapter.getFilter().filter(binding.etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // TODO: Use the ViewModel

    }

    @Override
    public void onUserClick(String uid) {
        addFragment(ChatFragment.newInstance(uid),true);
    }

    @Override
    public void renderingBottomItem(int count) {

    }

    @Override
    public void onChatReferenceChange(List<ChatReference> options) {

    }
}