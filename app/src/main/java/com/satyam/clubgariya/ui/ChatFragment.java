package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.database.AppContactDB;
import com.satyam.clubgariya.database.dao.AppContactDao;
import com.satyam.clubgariya.database.tables.AppContact;
import com.satyam.clubgariya.databinding.ChatFragmentBinding;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.ChatViewModel;

import java.util.List;

public class ChatFragment extends BaseFragment {

    private static final String TAG = "ChatFragment";
    private ChatViewModel mViewModel;
    private View fragView;
    private ChatFragmentBinding binding;
    private static AppContact appUserContact;


    public static ChatFragment newInstance(AppContact appContact) {
        appUserContact=appContact;
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);
        fragView = binding.getRoot();
        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        binding.setChatModel(mViewModel);
        if(appUserContact!=null){
            binding.tvChatWithUsername.setText(appUserContact.displayName);
        }
        if(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid().equals(appUserContact.uid))
            binding.tvSameUserMessage.setVisibility(View.VISIBLE);
        else binding.tvSameUserMessage.setVisibility(View.GONE);
    }

}