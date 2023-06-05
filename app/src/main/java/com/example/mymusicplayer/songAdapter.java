package com.example.mymusicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class songAdapter extends RecyclerView.Adapter<songAdapter.MyViewHolder> {


        private Context sContext;
        private ArrayList<MusicFiles>sFiles;

    public songAdapter(Context sContext, ArrayList<MusicFiles> sFiles) {
        this.sContext = sContext;
        this.sFiles = sFiles;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(sContext).inflate(R.layout.song_items,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.songName.setText(sFiles.get(position).getTitle());

        byte[] image= getSongArt(sFiles.get(position).getPath());
        if(image!= null)
        {
            Glide.with(sContext).asBitmap()
                    .load(image)
                    .into(holder.songImage);
        }
        else
        {
            Glide.with(sContext).asBitmap()
                    .load(R.drawable.back)
                    .into(holder.songImage);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(sContext,SongPlayerActivity.class);
                intent.putExtra("position", position);
                sContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sFiles.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder

    {
       ImageView songImage;
       TextView songName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            songImage= itemView.findViewById(R.id.song_img);
            songName= itemView.findViewById(R.id.song_name);
        }
    }



    private byte[] getSongArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art= retriever.getEmbeddedPicture();
        retriever.release();

        return art;

    }
}
