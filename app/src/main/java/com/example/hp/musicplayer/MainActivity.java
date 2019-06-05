package com.example.hp.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String[] songs;
    ListView songList;
  ArrayList<File > mySongs;
  AutoCompleteTextView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songList=findViewById(R.id.songsList);
        searchView = findViewById(R.id.searchBar);


        runtimePermission();
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(MainActivity.this,PlayActivity.class)
                        .putExtra("pos",position)
                        .putExtra("songName",mySongs.get(position).getName().toString())
                        .putExtra("songs",mySongs)



                );
//                 PlayActivity.mediaPlayer.stop();
//                 PlayActivity.mediaPlayer.release();




//
//                Uri u = Uri.parse(mySongs.get(position).toString());
//                MediaPlayer mediaPlayer =MediaPlayer.create(getApplicationContext(),u);
//                mediaPlayer.start();

            }
        });


    }

    public void runtimePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {



                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File> SongFinder(File file) {

        Log.i("error",file.toString());
        ArrayList<File> arrayList = new ArrayList<>();
        try {
            File[] files = file.listFiles();
            Log.i("error", Arrays.toString(files));
int i=0;

            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    Log.i("error- if wala",singleFile.toString());
                    arrayList.addAll(SongFinder(singleFile));
                }
                else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        Log.i("error-- else wala",singleFile.toString());
                        Log.i("songs", " chal ku nahi raha"+Integer.toString(i));
                        arrayList.add(singleFile);
                    }

                }
                i=i+1;
            }

            return arrayList;
        }
        catch (Exception e){
            e.getMessage();
            return  new ArrayList<>();
        }
    }


    public void display() {

        Log.i("error","lets see");
//         mySongs =SongFinder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
//         mySongs=SongFinder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
         mySongs=SongFinder(Environment.getExternalStorageDirectory());

         Log.i("error",Integer.toString(mySongs.size()));
         songs=new String[mySongs.size()];

        Log.i("error","plrease ho jaa");
        for(int i=0;i<mySongs.size();i++){

           songs[i] =(mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav",""));

        }

        Log.i("error dikha de",songs.toString());
        ArrayAdapter<String > arrayAdapter =new ArrayAdapter<>(this,R.layout.list_view,R.id.listItem,songs);

        songList.setAdapter(arrayAdapter);

  searchView.setAdapter(arrayAdapter);
  searchView.setThreshold(1);
 searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         String selection = (String) parent.getItemAtPosition(position);
         int pos = -1;

         for (int i = 0; i < songs.length; i++) {
             if (songs[i].equals(selection)) {
                 pos = i;
                 break;
             }
         }
         System.out.println("Position " + pos); //check it now in Logcat
         startActivity(new Intent(MainActivity.this,PlayActivity.class)
                 .putExtra("pos",pos)
                 .putExtra("songName",mySongs.get(pos).getName().toString())
                 .putExtra("songs",mySongs)



         );

     }
 });

    }

}