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

public class GridSongAdapter extends RecyclerView.Adapter<GridSongAdapter.GridViewHolder> {



    private Context gContext;
    private ArrayList<MusicFiles> gFiles;
    View view;

    public GridSongAdapter(Context gContext, ArrayList<MusicFiles> gFiles) {
        this.gContext = gContext;
        this.gFiles = gFiles;
    }

    @NonNull
    @Override
    public GridSongAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view= LayoutInflater.from(gContext).inflate(R.layout.grid_song_item,parent,false);

        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridSongAdapter.GridViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.gridSongName.setText(gFiles.get(position).getTitle());

        byte[] image= getSongArt(gFiles.get(position).getPath());
        if(image!= null)
        {
            Glide.with(gContext).asBitmap()
                    .load(image)
                    .into(holder.gridSongImage);
        }
        else
        {
            Glide.with(gContext).asBitmap()
                    .load(R.drawable.back)
                    .into(holder.gridSongImage);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(gContext,SongPlayerActivity.class);
                intent.putExtra("position", position);
                gContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return gFiles.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView gridSongImage;
        TextView gridSongName;



        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            gridSongImage=itemView.findViewById(R.id.grid_song_img);
            gridSongName=itemView.findViewById(R.id.grid_song_name);
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
