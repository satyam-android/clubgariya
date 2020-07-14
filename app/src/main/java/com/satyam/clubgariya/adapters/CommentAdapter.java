package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.modals.Comment;

import java.util.List;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment,CommentAdapter.ViewHolder> {

    private Context context;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options) {
        super(options);
    }

   /* public CommentAdapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.row_blog_comment, parent, false);
        return new ViewHolder(rowView);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {
        holder.tvSource.setText(comment.getUserName());
        holder.tv_comment.setText(comment.getComment());
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSource;
        TextView tv_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tv_source);
            tv_comment = itemView.findViewById(R.id.tv_comment_data);
        }
    }

}
