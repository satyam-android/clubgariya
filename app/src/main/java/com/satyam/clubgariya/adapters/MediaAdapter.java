package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satyam.clubgariya.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaAdapterHolder> {

    private Context context;
    private List<String> mediaUriList;

    public MediaAdapter(Context context, List<String> mediaUriList) {
        this.context = context;
        this.mediaUriList = mediaUriList;
    }

    @NonNull
    @Override
    public MediaAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_row, null, false);
        return new MediaAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAdapterHolder holder, int position) {
        Glide.with(context).load(mediaUriList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mediaUriList.size();
    }

    class MediaAdapterHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MediaAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_media);
        }
    }
}
