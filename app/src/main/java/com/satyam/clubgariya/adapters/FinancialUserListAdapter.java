package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.List;

public class FinancialUserListAdapter extends RecyclerView.Adapter<FinancialUserListAdapter.UserListModel> implements Filterable {
    private static final String TAG = "ChatUsersListAdapter";
    private Context context;
    private List<TransactionReference> appContacts;
    private List<TransactionReference> mOriginalValues;
    private List<TransactionReference> appContactsFilteredList;
    private UserListListner userListListner;

    public FinancialUserListAdapter(Context context, List<TransactionReference> appContacts, UserListListner userListListner) {
        this.context = context;
        this.appContacts = appContacts;
        this.userListListner = userListListner;
        this.appContactsFilteredList = appContacts;
    }

    @NonNull
    @Override
    public UserListModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.financial_list_user_row, parent, false);

        return new UserListModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListModel holder, final int position) {
        holder.tvUserName.setText(appContacts.get(position).getPartnerName());
        if(!TextUtils.isEmpty(appContacts.get(position).getUserProfile_image()))
        holder.profileImage.setImageURI(appContacts.get(position).getUserProfile_image());
//        holder.tvUserLastTransaction.setText(appContacts.get(position).getLastMessage());
        UtilFunction.getInstance().getTransactionStatus(appContacts.get(position),holder.tvUserLastTransaction);
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListListner.onUserClick(appContacts.get(position).getPartnerUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return appContacts.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values

                if (mOriginalValues == null)
                    mOriginalValues = new ArrayList<>(appContacts); // saves the original data in mOriginalValues

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    appContactsFilteredList.clear();
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        TransactionReference data = mOriginalValues.get(i);
                        if (data.getPartnerName().toLowerCase().contains(constraint.toString())) {
                            appContactsFilteredList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = appContactsFilteredList.size();
                    results.values = appContactsFilteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appContacts = (ArrayList<TransactionReference>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class UserListModel extends RecyclerView.ViewHolder {
        ConstraintLayout layoutMain;
        TextView tvUserName;
        TextView tvUserLastTransaction;
        SimpleDraweeView profileImage;

        public UserListModel(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserLastTransaction = itemView.findViewById(R.id.tv_transaction_status);
            layoutMain = itemView.findViewById(R.id.ll_chat_list_user_row);
            profileImage = itemView.findViewById(R.id.iv_user_profile);
        }
    }

    public void updateListValue(List<TransactionReference> appContacts) {
        this.appContacts = appContacts;
        notifyDataSetChanged();
    }
}
