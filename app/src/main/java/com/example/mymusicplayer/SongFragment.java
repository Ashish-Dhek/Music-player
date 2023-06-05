package com.example.mymusicplayer;

import static com.example.mymusicplayer.MainActivity.audioFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class SongFragment extends Fragment {

    Button btn1,btn2;
    LinearLayout linearLayout;

    public SongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_song, container, false);


    btn1= view.findViewById(R.id.button);
    btn2=view.findViewById(R.id.button2);
    linearLayout=view.findViewById(R.id.linearLayout_songs);


        SongFragmentFirst songFragmentFirst= new SongFragmentFirst();

        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayout_songs, songFragmentFirst);
        transaction.commit();

    btn1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SongFragmentFirst songFragmentFirst= new SongFragmentFirst();

            FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.linearLayout_songs, songFragmentFirst);
            transaction.commit();
        }
    });



        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SongFragmentSecond songFragmentSecond= new SongFragmentSecond();

                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayout_songs, songFragmentSecond);
                transaction.commit();

            }
        });


        return view;
    }
}