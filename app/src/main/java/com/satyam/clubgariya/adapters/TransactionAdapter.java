package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ChatAdapterCallback;
import com.satyam.clubgariya.callbacks.ChatImageAdapterCallback;
import com.satyam.clubgariya.callbacks.TransactionAdapterCallback;
import com.satyam.clubgariya.callbacks.TransactionImageAdapterCallback;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.Transaction;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;
import com.satyam.clubgariya.utils.UtilFunction;

public class TransactionAdapter extends FirestoreRecyclerAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {
    private static final String TAG = "TransactionAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String uid;
    private TransactionAdapterCallback callback;
    private FirestoreRecyclerOptions<Transaction> options;
    private Context context;
    private TransactionImageAdapter transactionImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private TransactionImageAdapterCallback transactionImageAdapterCallback;
    private String currencySymbol = "";

    public TransactionAdapter(Context context, @NonNull FirestoreRecyclerOptions<Transaction> options, TransactionImageAdapterCallback transactionImageAdapterCallback, TransactionAdapterCallback callback) {
        super(options);
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
        this.transactionImageAdapterCallback = transactionImageAdapterCallback;
        currencySymbol = UtilFunction.getInstance().getLocalCurrencySymbol(context);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.e(TAG, "onDataChanged: " + options.getSnapshots().size());
        callback.renderingBottomItem(options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull Transaction model) {
        Log.e(TAG, "onBindViewHolder: " + model.getTransMessage());


        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.itemCLick(model);
            }
        });
        if (uid.equals(model.getUserId())) {
            holder.layoutRow.setGravity(Gravity.END);
            if (model.getSettlementStatus().equals(AppConstants.TRANSACTION_DISPUTE))
                holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_out_dispute));
            else holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_out));
            holder.ivMessageStatus.setVisibility(View.VISIBLE);
            setImageByStatus(holder, model.getSettlementStatus(), true);
            if (model.getDeliveryStatus() != null)
                holder.ivMessageStatus.setImageDrawable(context.getDrawable(getMessageStatusDrawableId(model.getDeliveryStatus())));
        } else {

            holder.layoutRow.setGravity(Gravity.START);
            if (model.getSettlementStatus().equals(AppConstants.TRANSACTION_DISPUTE))
                holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_in_dispute));
            else holder.layout.setBackground(context.getDrawable(R.drawable.chat_bubble_in));
            holder.ivMessageStatus.setVisibility(View.GONE);
            setImageByStatus(holder, model.getSettlementStatus(), false);
        }
        holder.tvTime.setText(DateTimeUtilityFunctions.getInstance().timeStampToDateTime("dd/MM/yyyy HH:mm", Long.parseLong(model.getTimestamp())));
        if (model.getTransMediaList() != null) {
            if (model.getTransMediaList().size() > 0) {
                holder.imageContainer.setVisibility(View.VISIBLE);
                transactionImageAdapter = new TransactionImageAdapter(holder.imageContainer.getContext(), model, transactionImageAdapterCallback);
                if (model.getTransMediaList().size() > 3) {
                    gridLayoutManager = new GridLayoutManager(context, 2);
                } else {
                    gridLayoutManager = new GridLayoutManager(context, 1);
                }
                holder.imageContainer.setLayoutManager(gridLayoutManager);
                holder.imageContainer.setAdapter(transactionImageAdapter);
            } else holder.imageContainer.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(model.getTransMessage()))
            holder.tvMessage.setVisibility(View.GONE);
        else {
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.tvMessage.setText("Message :" + model.getTransMessage());
        }
        holder.tvTransAmount.setText("Amount  : " + currencySymbol + model.getTransAmount());
        holder.tvMessage.setOnClickListener((view -> {
            callback.itemCLick(model);
        }));
        holder.ivTransSuccess.setOnClickListener((view) -> {
            callback.updateTransactionAccept(model);
        });

        holder.ivTransFailed.setOnClickListener((view) -> {
            model.setSettlementStatus(AppConstants.TRANSACTION_DISPUTE);
            callback.updateTransactionDispute(model);
        });
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
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.transaction_row, parent, false);
        return new TransactionViewHolder(rowView);
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTransAmount;
        TextView tvTime;
        RecyclerView imageContainer;
        LinearLayout layout;
        LinearLayout layoutRow;
        SimpleDraweeView ivMessageStatus;
        Button ivTransFailed;
        Button ivTransSuccess;
        SimpleDraweeView ivTransFinalStatus;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_transaction_message);
            tvTransAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageContainer = itemView.findViewById(R.id.gv_chat_images);
            ivMessageStatus = itemView.findViewById(R.id.iv_message_status);
            layoutRow = itemView.findViewById(R.id.layout_chat_row);
            layout = itemView.findViewById(R.id.layout_chat_container);
            ivTransFailed = itemView.findViewById(R.id.iv_transaction_failed);
            ivTransSuccess = itemView.findViewById(R.id.iv_transaction_success);
            ivTransFinalStatus = itemView.findViewById(R.id.iv_transaction_final_status);
        }
    }


    private void setImageByStatus(TransactionViewHolder holder, String status, boolean isSender) {
        if (isSender) {
            holder.ivTransFailed.setVisibility(View.GONE);
            holder.ivTransSuccess.setVisibility(View.GONE);
            holder.ivTransFinalStatus.setVisibility(View.VISIBLE);
            switch (status) {
                case AppConstants.TRANSACTION_DISPUTE:
                    holder.ivTransFinalStatus.setImageResource(R.drawable.transaction_disputed);
                    break;
                case AppConstants.TRANSACTION_CONFIRM:
                    holder.ivTransFinalStatus.setImageResource(R.drawable.transaction_successful);
                    break;
                case AppConstants.TRANSACTION_INITIATE:
                default:
                    holder.ivTransFinalStatus.setImageResource(R.drawable.transaction_initiated);
                    break;
            }
        } else {

            switch (status) {
                case AppConstants.TRANSACTION_DISPUTE:
                    holder.ivTransFailed.setVisibility(View.GONE);
                    holder.ivTransSuccess.setVisibility(View.GONE);
                    holder.ivTransFinalStatus.setVisibility(View.VISIBLE);
                    holder.ivTransFinalStatus.setImageResource(R.drawable.transaction_disputed);
                    break;
                case AppConstants.TRANSACTION_CONFIRM:
                    holder.ivTransFailed.setVisibility(View.GONE);
                    holder.ivTransSuccess.setVisibility(View.GONE);
                    holder.ivTransFinalStatus.setVisibility(View.VISIBLE);
                    holder.ivTransFinalStatus.setImageResource(R.drawable.transaction_successful);
                    break;
                case AppConstants.TRANSACTION_INITIATE:
                default:
                    holder.ivTransFailed.setVisibility(View.VISIBLE);
                    holder.ivTransSuccess.setVisibility(View.VISIBLE);
                    holder.ivTransFinalStatus.setVisibility(View.GONE);
                    break;
            }
        }

    }
}
