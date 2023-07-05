package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    // Widgets.
    TextView headingText,timeText,songTitle;
    Button play,pauses, forward,backward;

    // Media Player.
    ImageView playerImage;
    MediaPlayer mediaPlayer;

    SeekBar seekBar;


    Handler handler = new Handler();

    //variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headingText = findViewById(R.id.mainHeading);
        timeText = findViewById(R.id.time_left_text);
        songTitle = findViewById(R.id.song_title);

        play = findViewById(R.id.play);
        pauses = findViewById(R.id.pause);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);

        playerImage = findViewById(R.id.playerimage);

        seekBar = findViewById(R.id.seekBar);

        //media player
        mediaPlayer = MediaPlayer.create(
                this,
                R.raw.ishqam
                );
        seekBar.setClickable(false);

        // song Title name...

        songTitle.setText(getResources().getIdentifier(
                "ishqam",
                "raw",
                getPackageName()
                ));


//        Adding functionalities for the buttons

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pauses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp + forwardTime) <= finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else{
                    Toast.makeText(MainActivity.this,"Can't make the jump",Toast.LENGTH_LONG).show();

                }
            }
        });


        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp - backwardTime) > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can't Go back", Toast.LENGTH_LONG).show();
                }
            }
        });








    }

    private void playMusic() {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(oneTimeOnly == 0){
            seekBar.setMax((int)finalTime);
            oneTimeOnly = 1;

        }

        timeText.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
                ));

        seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime,100);

    }

    // creating the runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            timeText.setText(String.format("%d min,%d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))

                    ));



            seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);



        }
    };


}