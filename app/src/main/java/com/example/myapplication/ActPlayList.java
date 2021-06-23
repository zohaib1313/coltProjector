package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DirectAction;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greentoad.turtlebody.mediapicker.MediaPicker;
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ActPlayList extends AppCompatActivity {
    List<ModelVideo> modelVideoList = new ArrayList<>();
    AdapterPlayList adapterPlayList;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;


    private String TAG = "aaaaaaaa";
    public static boolean isForShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_act_play_list);
        isForShow = getIntent().getBooleanExtra("isForShow", true);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        recyclerView = findViewById(R.id.rvPlayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterPlayList = new AdapterPlayList(modelVideoList);
        recyclerView.setAdapter(adapterPlayList);
        modelVideoList.clear();
        adapterPlayList.notifyDataSetChanged();

        loadVideos();
        if (!isForShow) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPickerConfig pickerConfig = new MediaPickerConfig()
                            .setAllowMultiSelection(false)
                            .setUriPermanentAccess(true)
                            .setShowConfirmationDialog(true)
                            .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    MediaPicker.with(ActPlayList.this, MediaPicker.MediaTypes.VIDEO)
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
                                        Log.d("aaaaaaaa", uri.getPath().toString());
                                        String fileName = uri.getPath().substring(uri.getPath().lastIndexOf("/"));
                                        Log.d("aaaaaaaa", fileName);
                                        copyFileFromUri(ActPlayList.this, uri, fileName);
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
            });

            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    if (swipeDir == ItemTouchHelper.LEFT) {
                        //Remove swiped item from list and notify the RecyclerView


                        int position = viewHolder.getAdapterPosition();
                        File file = new File(modelVideoList.get(position).getPath());
                        if (file.delete()) {
                            modelVideoList.remove(position);
                            adapterPlayList.notifyDataSetChanged();
                            Toast.makeText(ActPlayList.this, "Deleted ", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } else {

            adapterPlayList.setMyOnClick(new AdapterPlayList.MyOnClick() {
                @Override
                public void onMyItemClick(int position) {
                    Intent intent = new Intent(ActPlayList.this, ActCountDown.class);
                    intent.putExtra("id", modelVideoList.get(position).getPath());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void loadVideos() {
        try {

            modelVideoList.clear();
            adapterPlayList.notifyDataSetChanged();
            String path = Environment.getExternalStorageDirectory().toString() + "/ColtProjector/PlayList";
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    modelVideoList.add(new ModelVideo(file.getPath()));
                    Log.d("Files", "FileName: " + file.getPath());

                }
            }
            adapterPlayList.notifyDataSetChanged();
        } catch (Exception ignored) {
        }

    }

    public boolean copyFileFromUri(Context context, Uri fileUri, String fileName) {
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
            File saveDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ColtProjector/PlayList/");
            // create direcotory if it doesn't exists
            saveDirectory.mkdirs();

            outputStream = new FileOutputStream(saveDirectory + fileName); // filename.png, .mp3, .mp4 ...
            Log.e(TAG, "Output Stream Opened successfully");

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                outputStream.write(buffer, 0, buffer.length);
            }

            inputStream.close();
            outputStream.close();
            loadVideos();

        } catch (Exception e) {
            Log.e(TAG, "Exception occurred " + e.getMessage());
        } finally {


        }
        return true;
    }
}