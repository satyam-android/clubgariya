package com.satyam.clubgariya.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatAdapter;
import com.satyam.clubgariya.adapters.MediaAdapter;
import com.satyam.clubgariya.callbacks.ChatAdapterCallback;
import com.satyam.clubgariya.callbacks.ChatImageAdapterCallback;
import com.satyam.clubgariya.database.UserDB;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.ChatFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment implements ChatAdapterCallback, ChatImageAdapterCallback {

    private static final String TAG = "ChatFragment";
    private ChatViewModel mViewModel;
    private View fragView;
    private ChatFragmentBinding binding;
    private ChatReference chatReference;
    private DocumentReference userChatDocumentReference, partnerChatDocumentReference;
    private CollectionReference chatCollection;
    private Chat chat;
    private boolean hasSendAnyMessage = false;
    private boolean chatUpdated = false;
    private String lastMessage = "";
    private String chatId = "";
    private String message;
    private ChatAdapter mAdapter;
    private Query query;
    private List<String> mediaLocalUri;
    private List<String> mediaStorageUri;
    private MediaAdapter mediaAdapter;
    private int chatCount;
    private int uploadCount;
    private boolean scrollingToBottom;
    private static String partnerUid;
    private User appContact;


    public static ChatFragment newInstance(String partner_uid) {
        partnerUid = partner_uid;
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);
        mediaLocalUri = new ArrayList<>();
        mediaStorageUri = new ArrayList<>();
        setCurrentFragment(this);
        hideMediaList();
        hideAppBar();
        mediaAdapter = new MediaAdapter(getContext(), mediaLocalUri);
        binding.rvMediaList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvMediaList.setAdapter(mediaAdapter);
        fragView = binding.getRoot();
        return fragView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        binding.setChatModel(mViewModel);
        if(!TextUtils.isEmpty(partnerUid))
            getPartnerDetail(partnerUid);

        binding.btnSendChatMsg.setOnClickListener((view) -> {
            message = binding.etSendMessage.getText().toString();
            if (!TextUtils.isEmpty(message) || mediaLocalUri.size() > 0) {
                uploadCount = 0;
                if (mediaLocalUri.size() > 0)
                    showGlobalProgressBar("Uploading Media ..");
                uploadMedia();
            }

        });
        binding.btnSendMedia.setOnClickListener((view) -> {
            openGallery();
        });

    }

    public void uploadMedia() {
        if (mediaLocalUri.size() == uploadCount) {
            if (mediaLocalUri.size() > 0)
                hideGlobalProgressBar();
            chat = new Chat(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid(), message, "" + System.currentTimeMillis(), mediaStorageUri, AppConstants.MESSAGE_SEND);
            chatCollection.document().set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    hasSendAnyMessage = true;
                    lastMessage = message;
                    binding.etSendMessage.setText("");
                    mediaLocalUri.clear();
                    mediaStorageUri.clear();
                    hideMediaList();
                }
            });
        } else {
            for (String uri : mediaLocalUri) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                if (uri != null) {
                    Uri localUri = Uri.parse(uri);
                    StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + UtilFunction.getInstance().getFileExtension(getContext(), localUri));
                    ref.putFile(localUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadCount++;
                            mediaStorageUri.add(taskSnapshot.getMetadata().getReference().toString());
//                            Log.e(TAG, "onSuccess: Imge  " + taskSnapshot.getMetadata().getReference().toString());
                            uploadMedia();
                        }
                    });
                }
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Media(s)"), AppConstants.REQUEST_CODE_SELECT_MEDIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_SELECT_MEDIA) {
                if (data.getClipData() == null)
                    mediaLocalUri.add(data.getData().toString());
                else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaLocalUri.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
                showMediaList();
                mediaAdapter.notifyDataSetChanged();
            }
        }
    }

    public void implementChatAdapter() {
        query = FirebaseObjectHandler.getInstance().getChatCollection(chatId).orderBy("time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, new SnapshotParser<Chat>() {
            @NonNull
            @Override
            public Chat parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                Chat chat = snapshot.toObject(Chat.class);
                if (chat.getChatStatus() != null && !chat.getChatStatus().equalsIgnoreCase("READ")) {
                    if (!chat.getUid().equalsIgnoreCase(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid())) {
                        updateChatStatus(snapshot.getId());
                    }
                }
//                Log.e(TAG, "parseSnapshot: " + chat.getMsg() + "   Postition  " + chatCount);
                return chat;
            }
        }).build();
        mAdapter = new ChatAdapter(getContext(), options, this::onChatImageItemClick, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChatList.setLayoutManager(linearLayoutManager);
        binding.rvChatList.setAdapter(mAdapter);
        mAdapter.startListening();
        View contentView = binding.rvChatList;
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
                        scrollRecyclerViewToBottom(binding.rvChatList);
                    }
                } else {
                    // keyboard is closed
                    scrollingToBottom = false;
                }
            }
        });
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

    public void updateChatStatus(String messageId) {
        Map<String, Object> map = new HashMap<>();
        map.put("chatStatus", "READ");
        FirebaseObjectHandler.getInstance().getChatCollection(chatId).document(messageId).update(map);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter!=null)
        mAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter!=null)
        mAdapter.stopListening();
        shoAppBar("OneClick");
        AppConstants.CURRENT_CHAT_ID = "";
        if (hasSendAnyMessage) {
            updateChatData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.CURRENT_CHAT_ID = "";
        if (!chatUpdated && hasSendAnyMessage) {
            updateChatData();
        }
    }

    public void updateChatData() {
        chatReference = new ChatReference(chatId, "" + System.currentTimeMillis(), lastMessage, appContact.getName(), appContact.getUid(), appContact.getImageUrl());
        userChatDocumentReference.set(chatReference).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatReference = new ChatReference(chatId, "" + System.currentTimeMillis(), lastMessage, CurrentUserData.getInstance().getUserName(), CurrentUserData.getInstance().getUid(), CurrentUserData.getInstance().getUserImageUrl());
                partnerChatDocumentReference.set(chatReference).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        chatUpdated = true;
                    }
                });
            }
        });
    }

    private void setupUpChatDatabase() {
        // Get Collator instance
        int comapareValue = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid().compareTo(appContact.getUid());
        if (comapareValue > 0) {
            //User UID is greater than contact UID
            chatId = appContact.getUid() + FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
//            Log.e(TAG, "setupUpChatDatabase: UID greater  " + chatId);
        } else if (comapareValue == 0) {
            chatId = appContact.getUid() + appContact.getUid();
        } else if (comapareValue < 0) {
            //contact UID is greater than user UID
            chatId = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid() + appContact.getUid();
//            Log.e(TAG, "setupUpChatDatabase: UID Less  " + chatId);
        }
        AppConstants.CURRENT_CHAT_ID = chatId;
        userChatDocumentReference = FirebaseObjectHandler.getInstance().getUserChatCollectionReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).document(chatId);
        partnerChatDocumentReference = FirebaseObjectHandler.getInstance().getUserChatCollectionReference(appContact.getUid()).document(chatId);
        chatCollection = FirebaseObjectHandler.getInstance().getChatCollection(chatId);

    }

    public void showMediaList() {
        binding.rvMediaList.setVisibility(View.VISIBLE);
    }

    public void hideMediaList() {
        binding.rvMediaList.setVisibility(View.GONE);
    }


    @Override
    public void currentItemCount(int count) {
        if (chatCount < count)
            chatCount = count;
//        Log.e(TAG, "currentItemCount: total "+binding.rvChatList.getAdapter().getItemCount()+"    postiton  "+count );
//        if(count>=binding.rvChatList.getAdapter().getItemCount())
    }

    @Override
    public void renderingBottomItem(int count) {
//        Log.e(TAG, "renderingBottomItem: scrolling to "+count );
//        binding.rvChatList.smoothScrollToPosition(count);
        scrollRecyclerViewToBottom(binding.rvChatList);
    }

    @Override
    public void itemCLick(Chat chat) {
//        Log.e(TAG, "itemCLick: " + chat.getMsg());
        if (chat.getMediaList().size() > 0)
            addFragment(ChatDetailFragment.newInstance(chat, 0), true);
    }

    @Override
    public void itemImageListClick(Chat chat) {
//        Log.e(TAG, "itemImageListClick: "+chat.getMsg() );
    }

    @Override
    public void itemLongClick(Chat chat) {
        Log.e(TAG, "itemLongClick: " + chat.getMsg());
    }

    @Override
    public void onChatImageItemClick(Chat chat, int imageGridPosition) {
        if (chat.getMediaList().size() > 0)
            addFragment(ChatDetailFragment.newInstance(chat, imageGridPosition), true);
    }

    private void onUserDataSuccess(User user){
        this.appContact=user;
        if (appContact != null) {
            binding.tvChatWithUsername.setText(appContact.getName());
            binding.tvChatUserStatus.setText(appContact.getStatus());
            binding.layoutFooterSendMessage.setVisibility(View.VISIBLE);
            if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid().equals(appContact.getUid()))
                binding.tvSameUserMessage.setVisibility(View.VISIBLE);
            else binding.tvSameUserMessage.setVisibility(View.GONE);
            setupUpChatDatabase();
            implementChatAdapter();
        } else {
            binding.layoutFooterSendMessage.setVisibility(View.GONE);
        }
    }

    public void getPartnerDetail(String uid) {
        UserDB.getInstance(getContext()).userDao().getUserDetailByUid(uid).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null)
                    onUserDataSuccess(user);
                else
                    getPartnerDetailFromServer(uid);
            }
        });
    }

    private void getPartnerDetailFromServer(String uid){
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    onUserDataSuccess(task.getResult().toObject(User.class));
                } else {
                    appContact = null;
                }
            }
        });
    }
}