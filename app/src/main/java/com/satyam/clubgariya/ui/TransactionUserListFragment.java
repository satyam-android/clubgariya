package com.satyam.clubgariya.ui;

import android.os.Bundle;
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
import com.satyam.clubgariya.adapters.FinancialUserListAdapter;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.databinding.FinancialUserListFragmentBinding;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.FinancialUserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionUserListFragment extends BaseFragment implements UserListListner {
    private static final String TAG = "FinancialUserListFragme";
    private FinancialUserListViewModel mViewModel;
    private FinancialUserListFragmentBinding binding;
    private FinancialUserListAdapter financialUserListAdapter;
    private List<TransactionReference> transactionReferences;


    public static TransactionUserListFragment newInstance() {
        return new TransactionUserListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.financial_user_list_fragment, container, false);
        transactionReferences = new ArrayList<>();
        setCurrentFragment(this);
        shoAppBar(getString(R.string.app_name));
        binding.rcvUserTransactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        financialUserListAdapter = new FinancialUserListAdapter(getContext(), transactionReferences, this);
        binding.fbNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(UserListFragment.newInstance(AppConstants.USER_LIST_FOR_TRANSACTION),true);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FinancialUserListViewModel.class);
        mViewModel.setTransactionlistner(this);
        binding.rcvUserTransactionList.setAdapter(financialUserListAdapter);
        getAllClubContacts();
        // TODO: Use the ViewModel
    }

    public void getAllClubContacts() {
        getTransactionReferences().observe(getActivity(), new Observer<List<TransactionReference>>() {
            @Override
            public void onChanged(List<TransactionReference> transactionReferences) {
                financialUserListAdapter.updateListValue(transactionReferences);
            }
        });
   /*     AppContactDB.getInstance(getContext()).appContactDao().getClubContactList(true).observe(getActivity(), new Observer<List<AppContact>>() {
            @Override
            public void onChanged(List<AppContact> appContacts) {
                clubUsers = appContacts;
                for (AppContact con : clubUsers) {
                    Log.e(TAG, "onChanged: Name  " + con.getDisplayName() + "  Numnet  " + con.mobileNumber);
                    financialUserListAdapter.updateListValue(clubUsers);
                }
                //               syncContactWithClubMember();
            }
        });*/

    }

    @Override
    public void onUserClick(String uid) {

        addFragment(TransactionFragment.newInstance(uid),true);

  /*      new Thread(new Runnable() {
            @Override
            public void run() {
                User contact= UserDB.getInstance(getContext()).userDao().getContactDetailByUid(transactionReference.getPartnerUid());
                addFragment(TransactionFragment.newInstance(transactionReference.getPartnerUid()),true);

            }
        }).start();*/
    }

    @Override
    public void renderingBottomItem(int count) {

    }

    @Override
    public void onTransactionReferenceChange(List<TransactionReference> transactionReferences) {
        financialUserListAdapter.updateListValue(transactionReferences);
    }



}