package com.example.mymusicplayer;

import static com.example.mymusicplayer.MainActivity.audioFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SongFragmentSecond extends Fragment {

    RecyclerView recyclerView;
    GridSongAdapter gridSongAdapter;


    public SongFragmentSecond() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_song_second, container, false);

        recyclerView= view.findViewById(R.id.recyclerView_2);

        recyclerView.setHasFixedSize(true);

        if(!(audioFiles.size()<1))
        {
            gridSongAdapter = new GridSongAdapter(getContext(),audioFiles);
            recyclerView.setAdapter(gridSongAdapter);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));


        }

        return view;
    }
}