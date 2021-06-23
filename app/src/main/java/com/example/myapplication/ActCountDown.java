package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.File;

public class ActCountDown extends AppCompatActivity {
    private VideoView videoView;
    private String id;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_act_count_down);

        id = getIntent().getStringExtra("id");
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/ColtProjector/CountDownButton";
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
        videoView = findViewById(R.id.videoView2);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent intent = new Intent(ActCountDown.this, ActPlayerVideo.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

    }
}