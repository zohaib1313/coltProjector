package com.example.myapplication;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends Application {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isFirstTime", true)) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            final int[] mSongs = new int[]{R.raw.angelanim3, R.raw.anim1, R.raw.anim2};
            for (int i = 0; i < mSongs.length; i++) {
                try {
                    String path = Environment.getExternalStorageDirectory() + "/ColtProjector/Playlist";
                    File dir = new File(path);
                    if (dir.mkdirs() || dir.isDirectory()) {
                        String str_song_name = i + ".mp4";
                        copyToDirectory(mSongs[i], path + File.separator + str_song_name);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /////////////////////

            String clickHerePath = Environment.getExternalStorageDirectory() + "/ColtProjector/ClickHereButton";
            File dirClickHere = new File(clickHerePath);
            try {
                if (dirClickHere.mkdirs() || dirClickHere.isDirectory()) {
                    copyToDirectory(R.raw.clickhere, clickHerePath+ File.separator + "clickHere.mp4");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            String countDownPath = Environment.getExternalStorageDirectory() + "/ColtProjector/CountDownButton";
            File countDownDir = new File(countDownPath);
            try {
                if (countDownDir.mkdirs() || countDownDir.isDirectory()) {
                    copyToDirectory(R.raw.countdown, countDownPath+ File.separator + "countDown.mp4");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void copyToDirectory(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);

        Log.d("loveeeee", path);

        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
}
