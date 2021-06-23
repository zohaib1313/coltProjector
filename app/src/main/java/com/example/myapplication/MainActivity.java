package com.example.myapplication;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView3);


        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/ColtProjector/ClickHereButton";
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files != null) {
                File files1 = files[0];
                uri = Uri.fromFile(files1);
            } else {
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.countdown);
            }
        } catch (Exception ignored) {
        }

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActPlayList.class);
                intent.putExtra("isForShow", true);
                startActivity(intent);
                finish();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }
}