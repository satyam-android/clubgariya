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
import androidx.recyclerview.widget.RecyclerView;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatReferenceAdapterNew;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.tables.AppChatReference;
import com.satyam.clubgariya.databinding.ChatListFragmentBinding;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewmodels.ChatListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatUserListFragment extends BaseFragment implements UserListListner {
    private static final String TAG = "ChatUserListFragment";

    private ChatListFragmentBinding binding;
    private View fragView;
    private ChatListViewModel mViewModel;
    private ChatReferenceAdapterNew mAdapter;
    private boolean scrollingToBottom;
    private List<ChatReference> options;


    public static ChatUserListFragment newInstance() {
        return new ChatUserListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_list_fragment, container, false);
        fragView = binding.getRoot();
        options=new ArrayList<>();
        setCurrentFragment(this);
        shoAppBar(getString(R.string.app_name));
        binding.fbNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(UserListFragment.newInstance(AppConstants.USER_LIST_FOR_CHAT),true);
            }
        });
        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatListViewModel.class);
        implementChatListAdapter();

        getChatReferences().observe(getActivity(), new Observer<List<ChatReference>>() {
            @Override
            public void onChanged(List<ChatReference> chatReferences) {
                options= chatReferences;
                Log.e(TAG, "onChanged: "+options.size() );
                mAdapter.updateData(options);
            }
        });
    }


    public void implementChatListAdapter() {
//        Log.e(TAG, "implementChatListAdapter: ");


    /*    FirebaseObjectHandler.getInstance().getUserChatCollectionReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).orderBy("lastMessageTime", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    UserChatReference userChatReference=documentSnapshot.toObject(UserChatReference.class);
                    options.add(userChatReference);
                }
                mAdapter.notifyDataSetChanged();
            }
        });*/


     /*   query = FirebaseObjectHandler.getInstance().getUserChatCollectionReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid());
        FirestoreRecyclerOptions<UserChatReference> options = new FirestoreRecyclerOptions.Builder<UserChatReference>().setQuery(query, new SnapshotParser<UserChatReference>() {
            @NonNull
            @Override
            public UserChatReference parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                UserChatReference userChatReference = snapshot.toObject(UserChatReference.class);
                Log.e(TAG, "parseSnapshot: " + userChatReference.getChattingWith());
                return userChatReference;
            }
        }).build();*/
        mAdapter = new ChatReferenceAdapterNew(getContext(), options, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvUserChatList.setLayoutManager(linearLayoutManager);
        binding.rvUserChatList.setAdapter(mAdapter);
      /*  View contentView = binding.rvUserChatList;
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (!scrollingToBottom) {
                        scrollingToBottom = true;
                        scrollRecyclerViewToBottom(binding.rvUserChatList);
                    }
                } else {
                    // keyboard is closed
                    scrollingToBottom = false;
                }
            }
        });*/
       /*
       binding.rvChatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           @Override
           public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
               scrollRecyclerViewToBottom(binding.rvChatList);
           }
       });*/


    }

    private void scrollRecyclerViewToBottom(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void onUserClick(String userUid) {
//        if(TextUtils.isEmpty(appContact.getUid()))
//            appContact.setUid(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid());
        addFragment(ChatFragment.newInstance(userUid),true);
    }

    @Override
    public void renderingBottomItem(int count) {

    }
}