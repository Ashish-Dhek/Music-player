package com.example.mymusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

        public static final int REQUEST_CODE =1;
        ViewPager viewPager;
       TabLayout tabLayout;

       static ArrayList<MusicFiles>audioFiles;
    static boolean shuufle=false,repeat=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getStoragePermission();


    }






    private void getStoragePermission()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            , REQUEST_CODE);
        }

        else
        {
            audioFiles=getAllSongs(this);
            setViewPagerContent();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {


                audioFiles=getAllSongs(this);

                setViewPagerContent();

            }
            else
            {

                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);

            }


        }

    }


    private void setViewPagerContent()
    {
        viewPager= findViewById(R.id.viewpager);
        tabLayout= findViewById(R.id.tab_layout);
      FragmentAdapter viewPagerAdapter= new FragmentAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(new SongFragmentFirst(),"LINEAR");
        viewPagerAdapter.AddFragment(new SongFragmentSecond(), "GRID");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    public static class FragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment>fragments;
        private ArrayList<String>titles;


        public FragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);

            this.fragments=new ArrayList<>();
            this.titles= new ArrayList<>();
        }

        void AddFragment(Fragment fm, String t)
        {
            fragments.add(fm);
            titles.add(t);

        }



        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }




    public static ArrayList<MusicFiles> getAllSongs(Context context)
    {

        // Function to store all the audios in an array



        ArrayList<MusicFiles> temp= new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String [] projection= { MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.DATA,
                                MediaStore.Audio.Media.DURATION,
        };


        Cursor cursor = context.getContentResolver().query(uri, projection,null,null,null);

                if(cursor != null)
                {
                    while (cursor.moveToNext())
                    {
                        String album= cursor.getString(0);
                        String title= cursor.getString(1);
                        String artist= cursor.getString(2);
                        String path= cursor.getString(3);
                        String duration= cursor.getString(4);


                        MusicFiles songFile= new MusicFiles(path,title,album,artist,duration);

                        temp.add(songFile);

//                        Log.e("Path: "+ path,"Album: "+ album);


                    }
                    cursor.close();
                }

                return temp;



    }
}