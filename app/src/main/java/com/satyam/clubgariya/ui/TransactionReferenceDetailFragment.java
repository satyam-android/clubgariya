package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.TransactionReferenceUserAdapter;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.TransactionReferenceDetailFragmentBinding;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;

public class TransactionReferenceDetailFragment extends Fragment {

    private static final String TAG = "TransactionReferenceDet";
    private TransactionReferenceDetailViewModel mViewModel;
    private static User userDetail;
    private static TransactionReference transactionReference;
    private TransactionReferenceDetailFragmentBinding binding;

    public static TransactionReferenceDetailFragment newInstance(User user_detail, TransactionReference transaction_Reference) {
        userDetail = user_detail;
        transactionReference = transaction_Reference;
        return new TransactionReferenceDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_reference_detail_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionReferenceDetailViewModel.class);
        if(userDetail.getUserType().equalsIgnoreCase(AppConstants.USER_TYPE_GROUP))
            setHeaderDetail(userDetail.getName(),userDetail.getUserStatus(),userDetail.getImageUrl());
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMembers.setAdapter(new TransactionReferenceUserAdapter(getContext(),transactionReference.getUsers()));
        // TODO: Use the ViewModel
    }

    public void setHeaderDetail(String name, String status, String profileImage) {
        if (!TextUtils.isEmpty(name))
            binding.etName.setText(name);
        if (!TextUtils.isEmpty(status))
            binding.etDetail.setText(status);
        if (!TextUtils.isEmpty(profileImage)) {
            binding.ivProfileImage.setImageURI(profileImage);
            binding.tvCreatedAt.setText(DateTimeUtilityFunctions.getInstance().timeStampToDateTime("dd/MM/yyyy HH:mm", userDetail.getCreationTime()));
        }
        if(userDetail.getUserType().equalsIgnoreCase(AppConstants.USER_TYPE_GROUP)){
            binding.btnSplit.setVisibility(View.VISIBLE);
            binding.tvLabelTotalTrans.setVisibility(View.VISIBLE);
            binding.tvLabelTotalTrans.setText("Total Transaction : "+transactionReference.getTotalCredit());
        }else{
            binding.btnSplit.setVisibility(View.GONE);
            binding.tvLabelTotalTrans.setVisibility(View.GONE);
        }
    }

}