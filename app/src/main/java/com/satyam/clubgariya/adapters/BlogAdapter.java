package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.IBlogCallBack;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.modals.Comment;
import com.satyam.clubgariya.modals.Like;
import com.satyam.clubgariya.viewholders.BlogViewHolder;

import java.util.List;

public class BlogAdapter extends FirestoreRecyclerAdapter<Blog, BlogViewHolder> {
    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    private IBlogCallBack callBack;
    private Context context;


    public BlogAdapter(@NonNull FirestoreRecyclerOptions<Blog> options, Context context,IBlogCallBack callBack) {
        super(options);
        this.context=context;
        this.callBack=callBack;
    }




    @Override
    protected void onBindViewHolder(@NonNull final BlogViewHolder holder, int i, @NonNull final Blog blog) {
        holder.textView.setText(blog.getDescription());
        holder.tvSourceName.setText(blog.getSource());
        if(blog.commentCount>0)
            holder.tvCommentCount.setText(String.valueOf(blog.commentCount));
        if(blog.lastComment!=null){
            holder.layoutCommentUpdate.setVisibility(View.VISIBLE);
            holder.lastCommentText.setText(blog.lastComment);
            holder.lastCommentUser.setText(blog.lastCommentUserName);

        }else holder.layoutCommentUpdate.setVisibility(View.GONE);
  /*      List<Like> likes = blog.getLikes();
        List<Comment> comments = blog.getComments();
        if (likes != null && likes.size() > 0) {
            holder.tvLikeCount.setText(String.valueOf(likes.size()));
        }

        if (comments != null && comments.size() > 0) {
            holder.tvCommentCount.setText(String.valueOf(comments.size()));
        }*/
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(blog.getMedialLink());

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(context).load(task.getResult()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

                } else {
                    Glide.with(context).load(context.getDrawable(R.drawable.common_full_open_on_phone)).into(holder.imageView);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
       holder.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onShareClick(blog);
            }
        });
        holder.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCommentClick(blog);
            }
        });
        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onLikeClick(blog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rowView = layoutInflater.inflate(R.layout.row_my_blog_list, parent, false);
        return new BlogViewHolder(rowView);
    }
}
