package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BlogAdapterOld extends RecyclerView.Adapter<BlogAdapterOld.MyBlogView> {
    private static final String TAG = "MyBlogAdapter";
    private List<Blog> blogList;
    private Context context;
    private IBlogCallBack callBack;

    public BlogAdapterOld(List<Blog> blogList, Context context, IBlogCallBack callBack) {
        this.blogList = blogList;
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyBlogView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rowView = layoutInflater.inflate(R.layout.row_my_blog_list, parent, false);
        return new MyBlogView(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBlogView holder, final int position) {
        Blog blog = blogList.get(position);
        holder.textView.setText(blog.getDescription());
        holder.tvSourceName.setText(blog.getSource());
    /*    List<Like> likes = blog.getLikes();
        List<Comment> comments = blog.getComments(); */
        List<Like> likes = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        if (likes != null && likes.size() > 0) {
            holder.tvLikeCount.setText(String.valueOf(likes.size()));
        }

        if (comments != null && comments.size() > 0) {
            holder.tvCommentCount.setText(String.valueOf(comments.size()));
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(blogList.get(position).getMedialLink());

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
                callBack.onShareClick(blogList.get(position));
            }
        });
        holder.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCommentClick(blogList.get(position));
            }
        });
        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onLikeClick(blogList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (blogList == null)
            return 0;
        else return blogList.size();
    }

    public void updateBlogList(List<Blog> blogList) {
        this.blogList = blogList;
        notifyDataSetChanged();
    }

    class MyBlogView extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView tvSourceName;
        TextView tvLikeCount;
        TextView tvCommentCount;
        RelativeLayout layoutLike;
        RelativeLayout layoutComment;
        RelativeLayout layoutShare;

        public MyBlogView(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_blog_description);
            imageView = itemView.findViewById(R.id.iv_post_Image);
            tvSourceName = itemView.findViewById(R.id.tv_source);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            layoutLike = itemView.findViewById(R.id.rl_layout_like);
            layoutComment = itemView.findViewById(R.id.rl_layout_comment);
            layoutShare = itemView.findViewById(R.id.rl_layout_share);
        }
    }
}
