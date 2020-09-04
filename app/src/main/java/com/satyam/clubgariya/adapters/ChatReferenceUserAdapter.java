package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ChatReferenceListFragmentListner;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.ChatReferenceUser;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;

import java.util.List;

public class ChatReferenceUserAdapter extends RecyclerView.Adapter<ChatReferenceUserAdapter.ChatReferenceViewHolder> {
    private static final String TAG = "ChatReferenceAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String uid;
    private ChatReferenceListFragmentListner callback;
    private List<ChatReferenceUser> options;
    private Context context;
    private StorageReference storageReference;

    public ChatReferenceUserAdapter(Context context, List<ChatReferenceUser> options, ChatReferenceListFragmentListner callback) {
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
    }

/*
    public ChatReferenceAdapterNew(Context context, @NonNull FirestoreRecyclerOptions<UserChatReference> options, UserListListner callback) {
        super(options);
        Log.e(TAG, "ChatReferenceAdapter: " +options.getSnapshots().size());
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.e(TAG, "onDataChanged: " + options.getSnapshots().size());
        callback.renderingBottomItem(options.getSnapshots().size());


    @Override
    protected void onBindViewHolder(@NonNull ChatReferenceViewHolder holder, int position, @NonNull UserChatReference model) {

        if(!TextUtils.isEmpty(model.getProfile_image_url()))
        setImageFromUrl(model.getProfile_image_url(),holder.ivProfile);
        holder.tvMobile.setText(model.getMobileNumber());
        holder.tvLastMessage.setText(model.getLastMessage());
        holder.tvName.setText(model.getChattingWith());
        holder.layoutRow.setOnClickListener((view)->{
            callback.onUserClick(new AppContact(model.getMobileNumber(),model.getChattingWith(),"","","",model.getProfile_image_url(),true));
        });
    }
 }*/


    @NonNull
    @Override
    public ChatReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.chat_reference_user_row, parent, false);
        return new ChatReferenceViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatReferenceViewHolder holder, int position) {
        ChatReferenceUser model = options.get(position);
        if (model != null) {
//            holder.tvLastMessage.setText(model.getUserMobile());
            AppDatabaseHelper.getInstance(context).getUserByUid(model.getUserUid(), new AppDatabaseHelper.GetUserDetail() {
                @Override
                public void onUserSuccess(User user) {
                    if (user != null) {
                        holder.tvName.setText(user.getName());
                        if(!TextUtils.isEmpty(user.getImageUrl()))
                            holder.ivProfile.setImageURI(user.getImageUrl());

                    }
                }
            });

            if (model.getUserRole().equals(AppConstants.USER_ROLE_ADMIN)) {
                holder.ivDelete.setVisibility(View.GONE);
                holder.tvAdmin.setVisibility(View.VISIBLE);
            } else {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.tvAdmin.setVisibility(View.GONE);
            }
        }
        holder.layoutRow.setOnClickListener((view) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    User user = UserDB.getInstance(context).userDao().getContactDetail(model.getMobileNumber());
//                    callback.onUserClick(model.getChatReferenceId());
                }
            }).start();
        });
    }

    public void updateData(List<ChatReferenceUser> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ChatReferenceViewHolder extends RecyclerView.ViewHolder {
        TextView tvLastMessage;
        SimpleDraweeView ivProfile;
        TextView tvName;
        TextView tvAdmin;
        ConstraintLayout layoutRow;
        ImageView ivDelete;

        public ChatReferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAdmin = itemView.findViewById(R.id.tv_admin);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            layoutRow = itemView.findViewById(R.id.layout_chat_reference_row);
        }
    }

}
