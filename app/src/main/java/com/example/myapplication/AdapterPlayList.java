package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class AdapterPlayList extends RecyclerView.Adapter<AdapterPlayList.MyViewHolder> {


    private List<ModelVideo> videoList;

    public AdapterPlayList(List<ModelVideo> videoList) {
        this.videoList = videoList;
    }

    MyOnClick myOnClick;

    public void setMyOnClick(MyOnClick myOnClick) {
        this.myOnClick = myOnClick;
    }

    interface MyOnClick {
        void onMyItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_playlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {

//            Bitmap thumb = ThumbnailUtils.createVideoThumbnail("/storage/emulated/0/ColtProjector/PlayList/0.mp4", MediaStore.Video.Thumbnails.MINI_KIND);
//            holder.imageView.setImageBitmap(thumb);
//
            Glide
                    .with(holder.itemView.getContext())
                    .asBitmap()
                    .load(Uri.fromFile(new File(videoList.get(position).getPath())))
                    .into(holder.imageView);


        } catch (Exception ignored) {
            Log.d("fukkkk", ignored.getLocalizedMessage());
        }


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivPlaylistVideo);
            if (ActPlayList.isForShow)
                itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            myOnClick.onMyItemClick(getAdapterPosition());
        }
    }


}
