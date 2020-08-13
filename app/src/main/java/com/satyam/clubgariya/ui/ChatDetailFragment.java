package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatDetailImageAdapter;
import com.satyam.clubgariya.databinding.ChatDetailFragmentBinding;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.viewmodels.ChatDetailViewModel;

public class ChatDetailFragment extends BaseFragment {

    private ChatDetailViewModel mViewModel;
    private ChatDetailFragmentBinding binding;
    private View fragmentView;
    private static Chat chat;
    private static int imagePosition;
    private ChatDetailImageAdapter chatDetailImageAdapter;

    public static ChatDetailFragment newInstance(Chat chatData,int imagePositionData) {
        chat=chatData;
        imagePosition=imagePositionData;
        return new ChatDetailFragment();
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
        if(chat!=null && !TextUtils.isEmpty(chat.getMsg()))
            binding.tvChatMsgDetail.setText(chat.getMsg());

        if(chat!=null && chat.getMediaList().size()>0){
            chatDetailImageAdapter=new ChatDetailImageAdapter(chat.getMediaList());
            binding.rvChatImageDetail.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvChatImageDetail.setAdapter(chatDetailImageAdapter);
            binding.rvChatImageDetail.smoothScrollToPosition(imagePosition);
        }
    }

}