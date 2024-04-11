package com.example.songapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO
                    ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                // All permissions were granted
                             //   Toast.makeText(MainActivity.this, "Runtime permissions given", Toast.LENGTH_SHORT).show();
                                loadSongs();
                             
                            } else {
                                // Some permissions were denied
                                Toast.makeText(MainActivity.this, "Runtime permissions denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }

                    }).check();
        } else {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                            Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
                            loadSongs();
                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(MainActivity.this, "Runtime Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    public ArrayList<File> fetchSongs(File directory) {
        ArrayList<File> songsList = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isHidden() && file.isDirectory()) {
                    songsList.addAll(fetchSongs(file));
                } else {
                    if (file.getName().endsWith("mp3") && !file.getName().startsWith(".")) {
                        songsList.add(file);
                    }
                }
            }
        }

        return songsList;
    }
    private void loadSongs() {
//        ListView songListView;
//       ArrayList<File> mySongs=fetchSongs(Environment.getExternalStorageDirectory());
//     songListView=findViewById(R.id.songListView);
//     String []   items=new String[mySongs.size()];
//     for (int i=0;i<mySongs.size();i++){
//    items[i]=mySongs.get(i).getName().replace("mp3","");
//   }
//        ArrayAdapter<String>adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,items);
//        songListView.setAdapter(adapter);
//        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent=new Intent(MainActivity.this,SongPlayerActivity.class);
//
//                String currentSong=songListView.getItemAtPosition(position).toString();
//                intent.putExtra("songList",mySongs);
//                intent.putExtra("currentSong",currentSong);
//                intent.putExtra("position",position);
//                startActivity(intent);
//            }
//        });
//        }
//      }
        RecyclerView songRecyclerView;
        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());

        songRecyclerView = findViewById(R.id.songRecyclerView);

        SongAdapter adapter = new SongAdapter(mySongs);
        songRecyclerView.setAdapter(adapter);

        songRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, SongPlayerActivity.class);
                String currentSong = mySongs.get(position).getName().replace("mp3", "");
                intent.putExtra("songList", mySongs);
                intent.putExtra("currentSong", currentSong);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}


