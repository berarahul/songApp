package com.example.songapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SongPlayerActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    ImageButton playpausebutton,previous,next;
ArrayList<File>songs;
MediaPlayer mediaPlayer;
int position;

String textcontent;
TextView textView;
SeekBar seekBar;
Thread updateSeek;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
playpausebutton=findViewById(R.id.playPauseButton);
previous=findViewById(R.id.prevButton);
  next=findViewById(R.id.nextButton);
  textView=findViewById(R.id.textView);
  seekBar=findViewById(R.id.seekBar);
Intent intent=getIntent();
Bundle bundle=intent.getExtras();
songs=(ArrayList) bundle.getParcelableArrayList("songList");
       textcontent=intent.getStringExtra("currentSong");
       textView.setText(textcontent);
       textView.setSelected(true);
       position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek =new Thread(){
            @Override
            public void run() {
                int currentPosition=0;
                try {
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(100);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();


        playpausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    playpausebutton.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else {
                    playpausebutton.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=0){
                position=position-1;
                }else {
                position=songs.size()-1;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                playpausebutton.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textcontent=songs.get(position).getName().toString();
                textView.setText(textcontent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size()-1){
                    position=position+1;
                }else {
                    position=0;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                playpausebutton.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textcontent=songs.get(position).getName().toString();
                textView.setText(textcontent);
            }
        });



    }
    }
