package com.example.mymusicplayer;

import static com.example.mymusicplayer.MainActivity.audioFiles;
import static com.example.mymusicplayer.MainActivity.repeat;
import static com.example.mymusicplayer.MainActivity.shuufle;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class SongPlayerActivity extends AppCompatActivity   {




    TextView songName,artisName,durationPalyed,durationTotal;
    ImageView songImage,nextBtn,prevBtn,shuffleBtn,repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int pos=-1;
    ArrayList<MusicFiles>songsList;
    static MediaPlayer mediaPlayer;
    static Uri uri;
    private Handler handler= new Handler();
    private Thread playThread,prevThread,nextThread;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);


            // START

        viewsAllocate();
        getIntentMethod();


        songName.setText(songsList.get(pos).getTitle());
        artisName.setText(songsList.get(pos).getArtist());



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if(mediaPlayer!= null && b)
                {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SongPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer != null)
                {
                    int currentPos= mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(currentPos);

                    durationPalyed.setText(modifiedTime(currentPos));

                    if(currentPos==mediaPlayer.getDuration()/1000)
                    {
                        playPauseBtn.setImageResource(R.drawable.ic_baseline_play);
                        nextBtnClicked();
                    }
                }



                handler.postDelayed(this,1000);

            }
        });


        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(shuufle)
                {
                    shuufle=false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_off);
                }
                else
                {
                    shuufle=true;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_on);

                }
            }
        });



        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(repeat)
                {
                    repeat=false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_off);
                }

                else
                {
                    repeat= true;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_on);
                }
            }
        });


    }




    private void viewsAllocate()
    {
        songName=findViewById(R.id.songName);
        artisName=findViewById(R.id.artistName);
        durationPalyed=findViewById(R.id.durationPlayed);
        durationTotal=findViewById(R.id.totalDuration);
        songImage=findViewById(R.id.song_art);
        nextBtn=findViewById(R.id.next_btn);
        prevBtn=findViewById(R.id.prev_btn);
        shuffleBtn=findViewById(R.id.shuffle_btn);
        repeatBtn=findViewById(R.id.repeat_btn);
        playPauseBtn=findViewById(R.id.play_pause_btn);
        seekBar=findViewById(R.id.seekbar);

    }


    private void getIntentMethod() {
        pos= getIntent().getIntExtra("position",-1);
        songsList=audioFiles;

        if(songsList!=null)
        {
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            uri= Uri.parse(songsList.get(pos).getPath());

        }

        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer= MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        getSongImage(uri);

    }



    private void getSongImage(Uri uri)
    {
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int totalduration= Integer.parseInt(songsList.get(pos).getDuration()) / 1000;

        durationTotal.setText(modifiedTime(totalduration));
        seekBar.setMax(totalduration);
        byte[] art= retriever.getEmbeddedPicture();

        if(art != null)
        {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(songImage);
        }
        else
        {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.back)
                    .into(songImage);
        }

    }



    private String modifiedTime(int songPos)
    {

        String totalout="";
        String  totalnew="";
        String second=String.valueOf(songPos %60);
        String min =String.valueOf(songPos/60);

        if(second.length()==1)
        {
            totalnew = min + ":0" + second;
            return totalnew;
        }
        else
        {
            totalout=min+":"+second;
            return totalout;
        }



    }


    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();


        super.onResume();

    }

    private void playThreadBtn() {

        playThread= new Thread()
        {
            @Override
            public void run() {
                super.run();

                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();

    }


    private void playPauseBtnClicked()
    {
        if(mediaPlayer.isPlaying())
        {
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() /1000);

            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });
        }



        else
        {

            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/ 1000);

            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });
        }
    }




    private void nextThreadBtn() {

        nextThread= new Thread()
        {
            @Override
            public void run() {
                super.run();

                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       nextBtnClicked();
                    }
                });
            }
        };
       nextThread.start();

    }

    private void nextBtnClicked() {

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuufle && !repeat)
            {
                pos= getRandomPos(songsList.size()-1);
            }
            else if(!shuufle && !repeat) {
                pos=( (pos+1) % songsList.size() );
            }


            uri= Uri.parse(songsList.get(pos).getPath());
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            getSongImage(uri);
            songName.setText(songsList.get(pos).getTitle());
            artisName.setText(songsList.get(pos).getArtist());


            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });


            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();


        }

        else
        {

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuufle && !repeat)
            {
                pos= getRandomPos(songsList.size()-1);
            }
            else if(!shuufle && !repeat) {
                pos=( (pos+1) % songsList.size() );
            }


            uri= Uri.parse(songsList.get(pos).getPath());
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            getSongImage(uri);
            songName.setText(songsList.get(pos).getTitle());
            artisName.setText(songsList.get(pos).getArtist());


            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });


            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();


        }
    }

    private int getRandomPos(int i) {

        Random random= new Random();

        return random.nextInt(i+1);

    }


    private void prevThreadBtn() {

        prevThread= new Thread()
        {
            @Override
            public void run() {
                super.run();

                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {


        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuufle && !repeat)
            {
                pos= getRandomPos(songsList.size()-1);
            }
            else if(!shuufle && !repeat) {
                if(pos==0)
                {
                    pos=songsList.size()-1;
                }
                else {
                    pos = ((pos - 1) % songsList.size());
                }
            }


            uri= Uri.parse(songsList.get(pos).getPath());
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            getSongImage(uri);
            songName.setText(songsList.get(pos).getTitle());
            artisName.setText(songsList.get(pos).getArtist());


            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });


            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();


        }

        else
        {

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuufle && !repeat)
            {
                pos= getRandomPos(songsList.size()-1);
            }
            else if(!shuufle && !repeat) {
                if(pos==0)
                {
                    pos=songsList.size()-1;
                }
                else {
                    pos = ((pos - 1) % songsList.size());
                }
            }
            uri= Uri.parse(songsList.get(pos).getPath());
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            getSongImage(uri);
            songName.setText(songsList.get(pos).getTitle());
            artisName.setText(songsList.get(pos).getArtist());


            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer != null)
                    {
                        int currentPos= mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPos);

                    }

                    handler.postDelayed(this,1000);

                }
            });


            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();

        }


    }





}