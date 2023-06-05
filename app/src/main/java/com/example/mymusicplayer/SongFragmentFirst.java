package com.example.mymusicplayer;

import static com.example.mymusicplayer.MainActivity.audioFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SongFragmentFirst extends Fragment {

    RecyclerView recyclerView;
    songAdapter songAdapter;

    public SongFragmentFirst() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_song_first, container, false);

        recyclerView= view.findViewById(R.id.recyclerView_1);

        recyclerView.setHasFixedSize(true);

        if(!(audioFiles.size()<1))
        {
            songAdapter = new songAdapter(getContext(),audioFiles);
            recyclerView.setAdapter(songAdapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));


        }


        return view;
    }
}