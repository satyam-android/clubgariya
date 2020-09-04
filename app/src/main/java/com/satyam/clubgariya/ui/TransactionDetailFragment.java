package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatDetailImageAdapter;
import com.satyam.clubgariya.databinding.ChatDetailFragmentBinding;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.Transaction;
import com.satyam.clubgariya.viewmodels.ChatDetailViewModel;

public class TransactionDetailFragment extends BaseFragment {

    private ChatDetailViewModel mViewModel;
    private ChatDetailFragmentBinding binding;
    private View fragmentView;
    private static Transaction transaction;
    private static int imagePosition;
    private ChatDetailImageAdapter chatDetailImageAdapter;

    public static TransactionDetailFragment newInstance(Transaction trans, int imagePositionData) {
        transaction=trans;
        imagePosition=imagePositionData;
        return new TransactionDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.chat_detail_fragment, container, false);
        fragmentView=binding.getRoot();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatDetailViewModel.class);
        // TODO: Use the ViewModel
        if(transaction!=null)
            binding.tvChatMsgDetail.setText(""+transaction.getTransAmount());

        if(transaction!=null && transaction.getTransMediaList().size()>0){
            chatDetailImageAdapter=new ChatDetailImageAdapter(transaction.getTransMediaList());
            binding.rvChatImageDetail.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvChatImageDetail.setAdapter(chatDetailImageAdapter);
            binding.rvChatImageDetail.smoothScrollToPosition(imagePosition);
        }
    }

}