package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ChatAdapterCallback;
import com.satyam.clubgariya.callbacks.ChatImageAdapterCallback;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.Comment;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;
import com.stfalcon.frescoimageviewer.ImageViewer;

public class ChatAdapter extends FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder> {
    private static final String TAG = "ChatAdapter";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String uid;
    private ChatAdapterCallback callback;
    private FirestoreRecyclerOptions<Chat> options;
    private Context context;
    private ChatImageAdapter chatImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private ChatImageAdapterCallback chatImageAdapterCallback;

    public ChatAdapter(Context context, @NonNull FirestoreRecyclerOptions<Chat> options, ChatImageAdapterCallback chatImageAdapterCallback,ChatAdapterCallback callback) {
        super(options);
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
        this.chatImageAdapterCallback=chatImageAdapterCallback;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
//        Log.e(TAG, "onDataChanged: " + options.getSnapshots().size());
        callback.renderingBottomItem(options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
//        Log.e(TAG, "onBindViewHolder: " + model.getMsg());

        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.itemCLick(model);
            }
        });
        if (uid.equals(model.getUid())) {
            holder.layoutRow.setGravity(Gravity.END);
            holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_out));
            holder.ivMessageStatus.setVisibility(View.VISIBLE);
            if (model.getChatStatus() != null)
                holder.ivMessageStatus.setImageDrawable(context.getDrawable(getMessageStatusDrawableId(model.getChatStatus())));
        } else {
            holder.layoutRow.setGravity(Gravity.START);
            holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_in));
            holder.ivMessageStatus.setVisibility(View.GONE);
        }
        holder.tvTime.setText(DateTimeUtilityFunctions.getInstance().timeStampToDateTime("dd/MM/yyyy HH:mm",Long.parseLong(model.getTime())));
        if (model.getMediaList() != null) {
            if (model.getMediaList().size() > 0) {
                holder.imageContainer.setVisibility(View.VISIBLE);
                chatImageAdapter = new ChatImageAdapter(holder.imageContainer.getContext(), model,chatImageAdapterCallback);
                if (model.getMediaList().size() > 3) {
                    gridLayoutManager = new GridLayoutManager(context, 2);
                } else {
                    gridLayoutManager = new GridLayoutManager(context, 1);
                }
                holder.imageContainer.setLayoutManager(gridLayoutManager);
                holder.imageContainer.setAdapter(chatImageAdapter);
            } else holder.imageContainer.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(model.getMsg()))
            holder.tvChatMessage.setVisibility(View.GONE);
        else {
            holder.tvChatMessage.setVisibility(View.VISIBLE);
            holder.tvChatMessage.setText(model.getMsg());
        }
        holder.tvChatMessage.setOnClickListener((view -> {
            callback.itemCLick(model);
        }));
    }


    public int getMessageStatusDrawableId(String status) {
        int drawable = R.drawable.icon_loop;
        switch (status) {
            case AppConstants.MESSAGE_DELIVERED:
                drawable = R.drawable.icon_delivered;
                break;
            case AppConstants.MESSAGE_READ:
                drawable = R.drawable.icon_read;
                break;
            case AppConstants.MESSAGE_SEND:
                drawable = R.drawable.icon_send;
                break;
        }

        return drawable;
    }

    ;


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.chat_row, parent, false);
        return new ChatViewHolder(rowView);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatMessage;
        TextView tvTime;
        RecyclerView imageContainer;
        LinearLayout layout;
        LinearLayout layoutRow;
        ImageView ivMessageStatus;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatMessage = itemView.findViewById(R.id.tv_chat_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageContainer = itemView.findViewById(R.id.gv_chat_images);
            ivMessageStatus = itemView.findViewById(R.id.iv_message_status);
            layoutRow = itemView.findViewById(R.id.layout_chat_row);
            layout = itemView.findViewById(R.id.layout_chat_container);
        }
    }


    public ImageView getImageHolder() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        ImageView imageView = new ImageView(context);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(params);
        imageView.setFocusable(false);
        return imageView;
    }

}
