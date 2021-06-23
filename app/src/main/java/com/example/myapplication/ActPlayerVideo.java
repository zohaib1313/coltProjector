package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.File;

public class ActPlayerVideo extends AppCompatActivity {
    private VideoView videoView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_act_player_video);
        id = getIntent().getStringExtra("id");


        videoView = findViewById(R.id.videoView);
      //  Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.angelanim3);
        videoView.setVideoURI(Uri.fromFile(new File(id)));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(ActPlayerVideo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}