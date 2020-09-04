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
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.ChatAdapter;
import com.satyam.clubgariya.adapters.MediaAdapter;
import com.satyam.clubgariya.callbacks.ChatAdapterCallback;
import com.satyam.clubgariya.callbacks.ChatFragmentListner;
import com.satyam.clubgariya.callbacks.ChatImageAdapterCallback;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.databinding.ChatFragmentBinding;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.ChatReferenceUser;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;
import com.satyam.clubgariya.utils.PushChatMessage;
import com.satyam.clubgariya.utils.UtilFunction;
import com.satyam.clubgariya.viewmodels.ChatViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment implements ChatAdapterCallback, ChatImageAdapterCallback, ChatFragmentListner {

    private static final String TAG = "ChatFragment";
    private ChatViewModel mViewModel;
    private View fragView;
    private ChatFragmentBinding binding;
    private static ChatReference chatReference;
    private CollectionReference chatReferenceCollection;
    private CollectionReference chatCollection;
    private Chat chat;
    private boolean hasSendAnyMessage = false;
    private boolean chatReferenceUpdated = false;
    private String lastMessage = "";
    private long lastMessageTime;
    private static String chatId = "";
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
    private List<String> listRawPath;

    public static ChatFragment newInstance(String partner_uid, ChatReference chat_reference) {
        partnerUid = partner_uid;
        chatReference = chat_reference;
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);
        mediaLocalUri = new ArrayList<>();
        mediaStorageUri = new ArrayList<>();
        listRawPath = new ArrayList<>();
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
        chatReferenceCollection = FirebaseObjectHandler.getInstance().getChatReferenceCollection();
        if (!TextUtils.isEmpty(partnerUid)) {
            createChatReferenceId();
            getPartnerDetail(partnerUid);
            chatCollection = FirebaseObjectHandler.getInstance().getChatCollection(chatId);

        }else{
//        if (!TextUtils.isEmpty(chatId)) {
            chatId=chatReference.getChatReferenceId();
            chatCollection = FirebaseObjectHandler.getInstance().getChatCollection(chatId);
//            getChatReferenceDetail();
            onChatReferenceSuccess(chatReference);
        }
        AppConstants.CURRENT_CHAT_ID = chatId;
        binding.layoutHeaderUserDetail.setOnClickListener(view -> addFragment(ChatReferenceDetailFragment.newInstance(appContact, chatReference), true));

        binding.btnSendChatMsg.setOnClickListener((view) -> {
            message = binding.etSendMessage.getText().toString();
            if (!TextUtils.isEmpty(message) || mediaLocalUri.size() > 0) {
                lastMessageTime = System.currentTimeMillis();
                lastMessage = message;
                uploadCount = 0;
                if (chatReference == null) {
                    createChatReferenceData();
                } else {
                    if (mediaLocalUri.size() > 0)
                        showGlobalProgressBar("Uploading Media ..");
//                    uploadMedia();
                    PushChatMessage.getInstance().uploadChatData(getActivity(), chatCollection, lastMessage, mediaLocalUri, new PushChatMessage.UploadChatListner() {
                        @Override
                        public void onChatUploadComplete(Chat chat) {
                            hasSendAnyMessage = true;
                            chatReferenceUpdated = false;
                            lastMessage = message;
                            binding.etSendMessage.setText("");
                            mediaLocalUri.clear();
                            mediaStorageUri.clear();
                            hideMediaList();
                            mViewModel.sendChatNotification(chatReference, chat, appContact);
                        }

                        @Override
                        public void uploadChatProgressCount(int progress) {
                            ((MainActivity) getActivity()).setProgressbarProgress(progress);
                        }
                    });
                }
            }

        });
        binding.btnSendMedia.setOnClickListener((view) -> {
            openGallery();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().setClickable(true);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        hideAppBar();
    }


    public void uploadMedia() {
        if (mediaLocalUri.size() == uploadCount) {
            if (mediaLocalUri.size() > 0)
                hideGlobalProgressBar();
            chat = new Chat(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid(), message, "" + System.currentTimeMillis(), mediaStorageUri, AppConstants.MESSAGE_SEND);

            chatCollection.add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    hasSendAnyMessage = true;
                    chatReferenceUpdated = false;
                    chat.setChatId(task.getResult().getId());
                    lastMessage = message;
                    binding.etSendMessage.setText("");
                    mediaLocalUri.clear();
                    mediaStorageUri.clear();
                    hideMediaList();
                    mViewModel.sendChatNotification(chatReference, chat, appContact);
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
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadCount++;
                                    mediaStorageUri.add(uri.toString());
//                            Log.e(TAG, "onSuccess: Imge  " + taskSnapshot.getMetadata().getReference().toString());
                                    uploadMedia();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            ((MainActivity) getActivity()).setProgressbarProgress(progress);
                        }
                    });
                }
            }
        }
    }

    public void openGallery() {
        Options options = Options.init()
                .setRequestCode(AppConstants.REQUEST_CODE_SELECT_MEDIA)
                .setPreSelectedUrls(new ArrayList<>(listRawPath))//Request code for activity results
                .setCount(5) //Number of images to restict selection count
                .setExcludeVideos(true)
                .setFrontfacing(true)                                         //Front Facing camera on start
//                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/OneClick/media");                                       //Custom Path For media Storage

        Pix.start(getActivity(), options);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Media(s) Max 10"), AppConstants.REQUEST_CODE_SELECT_MEDIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_SELECT_MEDIA) {
                listRawPath.clear();
                mediaLocalUri.clear();
                listRawPath = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//                if (data.getClipData() == null)
//                    mediaLocalUri.add(data.getData().toString());
//                else {
//                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
//                        mediaLocalUri.add(data.getClipData().getItemAt(i).getUri().toString());
//                    }
//                }
                if (listRawPath != null)
                    for (int i = 0; i < listRawPath.size(); i++) {
                        mediaLocalUri.add(Uri.fromFile(new File(listRawPath.get(i))).toString());
                    }

                showMediaList();
                mediaAdapter.updateMediaList(mediaLocalUri);
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
        if (mAdapter != null)
            mAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        binding.getRoot().setClickable(false);
        if (mAdapter != null)
            mAdapter.stopListening();
        shoAppBar("OneClick");
        AppConstants.CURRENT_CHAT_ID = "";
        if (hasSendAnyMessage) {
            if (chatReference == null)
                createChatReferenceData();
            else updateChatReferenceData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.CURRENT_CHAT_ID = "";
        if (!chatReferenceUpdated && hasSendAnyMessage) {
            if (chatReference == null)
                createChatReferenceData();
            else updateChatReferenceData();
        }
    }

    public void updateChatReferenceData() {
        Map<String, Object> referenceUpdate = new HashMap<>();
        referenceUpdate.put("lastMessage", lastMessage);
        referenceUpdate.put("lastMessageTime", lastMessageTime);
        chatReferenceCollection.document(chatId).update(referenceUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatReferenceUpdated = true;
            }
        });
    }

    public void createChatReferenceData() {
        List<ChatReferenceUser> users = new ArrayList<>();
        users.add(new ChatReferenceUser(CurrentUserData.getInstance().getUid(),AppConstants.USER_ROLE_ADMIN));
        users.add(new ChatReferenceUser(appContact.getUid(), AppConstants.USER_ROLE_MEMBER));
        List<String> allowedMobile = new ArrayList<>();
        allowedMobile.add(CurrentUserData.getInstance().getUid());
        allowedMobile.add(appContact.getUid());
        chatReference = new ChatReference(chatId,lastMessageTime, lastMessage, "", CurrentUserData.getInstance().getUid(), AppConstants.REFERENCE_TYPE_INDIVIDUAL, "", users, allowedMobile,"");
        chatReferenceCollection.document(chatId).set(chatReference).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mAdapter.getItemCount() > 0) {
                    chatReferenceUpdated = true;
                }
                if (mediaLocalUri.size() > 0)
                    showGlobalProgressBar("Uploading Media ..");
                uploadMedia();
            }
        });
    }

    private void createChatReferenceId() {
        // Get Collator instance
        int comapareValue = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid().compareTo(partnerUid);
        if (comapareValue > 0) {
            //User UID is greater than contact UID
            chatId = partnerUid + FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
//            Log.e(TAG, "setupUpChatDatabase: UID greater  " + chatId);
        } else if (comapareValue == 0) {
            chatId = partnerUid + partnerUid;
        } else if (comapareValue < 0) {
            //contact UID is greater than user UID
            chatId = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid() + partnerUid;
//            Log.e(TAG, "setupUpChatDatabase: UID Less  " + chatId);
        }
    }

    public void getChatReferenceDetail() {
        chatReferenceCollection.document(chatId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        chatReference = task.getResult().toObject(ChatReference.class);
                        chatReference.setChatReferenceId(task.getResult().getId());
                        onChatReferenceSuccess(chatReference);
                    }
                }
            }
        });
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

    private void onUserDataSuccess(User user) {
        this.appContact = user;
        binding.tvNoteMessage.setVisibility(View.GONE);
        if (appContact != null && getActivity() != null) {
//            if(!new AppSharedPreference(getActivity()).getBooleanData(chatReference.getChatReferenceId())){
//                FirebaseMessaging.getInstance().subscribeToTopic(user.fcm_token);
//                new AppSharedPreference(getActivity()).setBooleanData(chatReference.getChatReferenceId(),true);
//            }
            AppDatabaseHelper.getInstance(getActivity()).getUserByUid(appContact.getUid(), new AppDatabaseHelper.GetUserDetail() {
                @Override
                public void onUserSuccess(User user) {
                    if (user!=null && !TextUtils.isEmpty(user.getName()))
                        appContact.setName(user.getName());
                    changeUserName();
                }
            });
        } else {
            binding.layoutFooterSendMessage.setVisibility(View.GONE);
        }
    }

    private void changeUserName() {
        setHeaderDetail(appContact.getName(), appContact.getUserStatus(), appContact.getImageUrl());
        binding.layoutFooterSendMessage.setVisibility(View.VISIBLE);
        implementChatAdapter();
    }

    private void onChatReferenceSuccess(ChatReference chatReference) {
        if (chatReference != null) {
            if (chatReference.getAllowedUsers() != null && !chatReference.getAllowedUsers().contains(CurrentUserData.getInstance().getUid())) {
                binding.tvNoteMessage.setText("You are removed from this chat");
                binding.tvNoteMessage.setVisibility(View.VISIBLE);
                binding.layoutFooterSendMessage.setVisibility(View.GONE);
            } else {
                String partnerUid = "";
                if (chatReference.getReferenceType().equals(AppConstants.REFERENCE_TYPE_GROUP)) {
//                    partnerUid = chatReference.getChatReferenceId().replace(chatReference.getCreatedBy(), "");
                    partnerUid = chatReference.getReferenceId();
                } else {
                    if (chatReference.getUsers().get(0).getUserUid().equals(CurrentUserData.getInstance().getUid())) {
                        partnerUid = chatReference.getUsers().get(1).getUserUid();
//                        setHeaderDetail(chatReference.getUsers().get(1).getUserName(), chatReference.getUsers().get(1).getUserStatus(), chatReference.getUsers().get(1).getUserProfileImage());
                    } else {
//                        setHeaderDetail(chatReference.getUsers().get(0).getUserName(), chatReference.getUsers().get(0).getUserStatus(), chatReference.getUsers().get(0).getUserProfileImage());
                        partnerUid = chatReference.getUsers().get(0).getUserUid();
                    }
                }
                getPartnerDetail(partnerUid);
//                if(chatReference.getPartnerUid())
//                if (chatReference.getUserUid().equals(CurrentUserData.getInstance().getUid()))
//                    setHeaderDetail(chatReference.getPartnerName(), chatReference.getPartnerStatus(), chatReference.getPartnerProfile_image());
//                else
//                    setHeaderDetail(chatReference.getUserName(), chatReference.getUserStatus(), chatReference.getUserProfile_image());
////                binding.layoutFooterSendMessage.setVisibility(View.VISIBLE);
//                implementChatAdapter();
//                binding.tvNoteMessage.setVisibility(View.GONE);
            }
        } else {
            binding.layoutFooterSendMessage.setVisibility(View.GONE);
        }
    }

    public void setHeaderDetail(String name, String status, String profileImage) {
        if (!TextUtils.isEmpty(name))
            binding.tvChatWithUsername.setText(name);
        if (!TextUtils.isEmpty(status))
            binding.tvChatUserStatus.setText(status);
        if (!TextUtils.isEmpty(profileImage)) {
            binding.ivUserProfile.setImageURI(profileImage);
            Log.e(TAG, "setHeaderDetail: " + profileImage);
        }
    }

    public void getPartnerDetail(String uid) {
        AppDatabaseHelper.getInstance(getActivity()).getUserByUid(uid, new AppDatabaseHelper.GetUserDetail() {
            @Override
            public void onUserSuccess(User user) {
                onUserDataSuccess(user);
            }
        });
//        getPartnerDetailFromServer(uid);
  /*      AppDatabase.getInstance(getContext()).userDao().getUserDetailByUid(uid).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null)
                    onUserDataSuccess(user);
            }
        });*/
    }

    private void getPartnerDetailFromServer(String uid) {
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

    @Override
    public void onUserDataUpdate(ChatReference chatReference) {

    }
}