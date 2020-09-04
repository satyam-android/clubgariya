package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserGroupListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.tables.User;

import java.util.ArrayList;
import java.util.List;

public class UsersListGroupAdapter extends RecyclerView.Adapter<UsersListGroupAdapter.UserListModel> implements Filterable {
    private static final String TAG = "ChatUsersListAdapter";
    private Context context;
    private List<User> appContacts;
    private List<User> mOriginalValues;
    private List<User> appContactsFilteredList;
    private UserGroupListner listner;

    public UsersListGroupAdapter(Context context, List<User> appContacts, UserGroupListner listner) {
        this.context = context;
        this.listner=listner;
        this.appContacts = appContacts;
        this.appContactsFilteredList = appContacts;
    }

    @NonNull
    @Override
    public UserListModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_user_group_row, parent, false);

        return new UserListModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListModel holder, final int position) {
        holder.tvUserName.setText(appContacts.get(position).getName());
        holder.tvUserLastChatMsg.setText(appContacts.get(position).getMobile());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    listner.onCHatUserChecked(appContacts.get(position));
                else listner.onCHatUserUnChecked(appContacts.get(position));
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
                        User data = mOriginalValues.get(i);
                        if (data.getName().toLowerCase().contains(constraint.toString())) {
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
                appContacts = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class UserListModel extends RecyclerView.ViewHolder {
        ConstraintLayout layoutMain;
        TextView tvUserName;
        TextView tvUserLastChatMsg;
        CheckBox checkBox;

        public UserListModel(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserLastChatMsg = itemView.findViewById(R.id.tv_last_chat_message);
            checkBox = itemView.findViewById(R.id.cb_check_user);
            layoutMain = itemView.findViewById(R.id.ll_chat_list_user_row);
        }
    }

    public void updateListValue(List<User> appContacts) {
        this.appContacts = appContacts;
        notifyDataSetChanged();
    }
}
