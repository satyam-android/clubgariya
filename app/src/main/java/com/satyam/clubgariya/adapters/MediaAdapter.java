package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satyam.clubgariya.R;

import java.io.File;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaAdapterHolder> {
    private static final String TAG = "MediaAdapter";
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
        String imageUri= mediaUriList.get(position);
//        String imageUri= new File(mediaUriList.get(position)).getAbsolutePath();
        Log.e(TAG, "onBindViewHolder: "+imageUri );
        Glide.with(context).load(imageUri).into(holder.imageView);

    }
public void updateMediaList(List<String> mediaUriList)
{
    this.mediaUriList=mediaUriList;
    notifyDataSetChanged();
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
