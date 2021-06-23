package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.greentoad.turtlebody.mediapicker.MediaPicker;
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Permission;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ActSettings extends AppCompatActivity {
    private static final String TAG = "aaaaaa";
    String path;
    SharedPreferences sharedPreferences;
    TextView tvPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_act_settings);

        tvPath = findViewById(R.id.textView3);


        if (!checkIfAlreadyhavePermission()) {
            ActivityCompat.requestPermissions(ActSettings.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        if (checkIfAlreadyhavePermission()) {
            uploadFromRawFolder();
        }
        tvPath.setText("/ColtProjector/Playlist");

    }

    private void uploadFromRawFolder() {


        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstTime", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            final int[] mSongs = new int[]{R.raw.angelanim3, R.raw.anim1, R.raw.anim2};
            for (int i = 0; i < mSongs.length; i++) {
                try {
                    path = Environment.getExternalStorageDirectory() + "/ColtProjector/Playlist";

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
                    copyToDirectory(R.raw.clickhere, clickHerePath + File.separator + "clickHere.mp4");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            String countDownPath = Environment.getExternalStorageDirectory() + "/ColtProjector/CountDownButton";
            File countDownDir = new File(countDownPath);
            try {
                if (countDownDir.mkdirs() || countDownDir.isDirectory()) {
                    copyToDirectory(R.raw.countdown, countDownPath + File.separator + "countDown.mp4");
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

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {//granted
            //not granted
            uploadFromRawFolder();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /////////////////////////
    public void changeCountDown(View view) {
        pickFile("CountDownButton", "countdown.mp4");

    }

    public void changeClickHere(View view) {
        pickFile("ClickHereButton", "clickhere.mp4");

    }

    public void changePlayList(View view) {

        Intent intent = new Intent(ActSettings.this, ActPlayList.class);
        intent.putExtra("isForShow", false);
        startActivity(intent);

    }


    public void gotoHome(View view) {
        Intent intent = new Intent(ActSettings.this, MainActivity.class);
        startActivity(intent);
    }


    private void pickFile(final String pathToSaveIn, final String fileName) {
        MediaPickerConfig pickerConfig = new MediaPickerConfig()
                .setAllowMultiSelection(false)
                .setUriPermanentAccess(true)
                .setShowConfirmationDialog(true)
                .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MediaPicker.with(ActSettings.this, MediaPicker.MediaTypes.VIDEO)
                .setConfig(pickerConfig)

                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        //uris: list of uri
                        for (Uri uri : uris) {
                            copyFileFromUri(ActSettings.this, uri, fileName, pathToSaveIn);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void copyFileFromUri(Context context, Uri fileUri, String fileName, String pathToSaveIn) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            File root = Environment.getExternalStorageDirectory();
            if (root == null) {
                Log.d(TAG, "Failed to get root");
            }

            // create a directory
            File saveDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ColtProjector/" + pathToSaveIn + "/");
            // create direcotory if it doesn't exists
            Log.d("aaaaa", saveDirectory.toString());

            saveDirectory.mkdirs();

            outputStream = new FileOutputStream(saveDirectory + File.separator + fileName); // filename.png, .mp3, .mp4 ...
            Log.e(TAG, "Output Stream Opened successfully");

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            if (inputStream != null) {
                while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                    outputStream.write(buffer, 0, buffer.length);
                }
                inputStream.close();
            }
            outputStream.close();
            Toast.makeText(ActSettings.this, "Added Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception occurred " + e.getMessage());
        } finally {


        }
    }
}