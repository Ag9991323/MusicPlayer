package com.example.hp.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView currentSong;

    ArrayList<File> SongsList;
    Button playBtn, previousBtn, nextBtn;
    AudioManager audioManager;
    int songPosition;
    String songName;
    static MediaPlayer mediaPlayer;
    Uri u;
    Boolean isPlaying = false;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //init
        seekBar = findViewById(R.id.seekBar);
        currentSong = findViewById(R.id.currentSong);
        playBtn = findViewById(R.id.playBtn);
        nextBtn = findViewById(R.id.nextBtn);
        previousBtn = findViewById(R.id.previousBtn);
        playBtn.setBackgroundResource(R.drawable.ic_pause_30dp);


        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //new Thread update seekBar

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int TotalDuration = mediaPlayer.getDuration();
                int CurrentTime = 0;
                while (CurrentTime < TotalDuration) {
                    try {
                        sleep(500);
                        CurrentTime = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(CurrentTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }

        };

        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();

        songName = intent.getStringExtra("songName");
        songPosition = intent.getIntExtra("pos", 0);
        SongsList = (ArrayList) intent.getExtras().getParcelableArrayList("songs");

        currentSong.setText(songName);
        currentSong.setSelected(true);
//            if( isPlaying) {
//                mediaPlayer.stop();
//                u = Uri.parse(SongsList.get(songPosition).toString());
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
//                mediaPlayer.start();
//                isPlaying =false;
//
//                seekProgress();
//            }
//
//            else{
        u = Uri.parse(SongsList.get(songPosition).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        mediaPlayer.start();


        seekProgress();
        updateSeekBar.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                nextBtn.performClick();
            }
        });
////            }


// seekBar Listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        //previous btn

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songPosition = ((songPosition - 1) < 0) ? (SongsList.size() - 1) : (songPosition - 1);
                mediaPlayer.stop();
                mediaPlayer.release();
                u = Uri.parse(SongsList.get(songPosition).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                mediaPlayer.start();
                seekProgress();
                currentSong.setText(SongsList.get(songPosition).getName().replace(".mp3", ""));

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songPosition = ((songPosition + 1) % SongsList.size());
                mediaPlayer.stop();
                mediaPlayer.release();
                u = Uri.parse(SongsList.get(songPosition).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                mediaPlayer.start();
                seekProgress();
                currentSong.setText(SongsList.get(songPosition).getName().replace(".mp3", ""));
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekProgress();
                if (mediaPlayer.isPlaying()) {
                    Log.i("playing", "song rukna chhaiye");
                    mediaPlayer.pause();
                    //playBtn.setCompoundDrawables(null,null,PlayActivity.this.getResources().getDrawable( R.drawable.ic_pause_30dp),null);
                    playBtn.setBackgroundResource(R.drawable.ic_play);
                } else {
                    Log.i("playing", "song shuru hona chhaiye");
//                       v.setBackground(null);
                    // playBtn.setCompoundDrawables(null,null,PlayActivity.this.getResources().getDrawable( R.drawable.ic_play),null);
                    playBtn.setBackgroundResource(R.drawable.ic_pause_30dp);
                    mediaPlayer.start();
                }


            }

        });


    }

    public void seekProgress() {
        seekBar.setMax(mediaPlayer.getDuration());


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}